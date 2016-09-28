package org.bitbucket.keiki.jcurse.io;

import static org.bitbucket.keiki.jcurse.io.Constants.CHARSET_WEBSITE;
import static org.bitbucket.keiki.jcurse.io.Constants.USER_AGENT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;
import org.bitbucket.keiki.jcurse.data.Addon;
import org.bitbucket.keiki.jcurse.data.BusinessException;
import org.bitbucket.keiki.jcurse.data.ReleaseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurseImpl implements Curse {
    
    private static final String HTML_ATTRIBUTE_DOWN_URL = "data-href";
    private static final String HTML_ATTRIBUTE_ADDON_ID = "data-project";
    private static final int URL_ID_PART_END = 2;
    private static final int URL_ID_PART_START = 3;

    private static final Logger LOG = LoggerFactory.getLogger(CurseImpl.class);
    private final String curseBaseUrl;
    private AddonFileHandler folderHandler;
    
    public CurseImpl(String addonFolderName, String curseBaseUrl) {
        this.folderHandler = new AddonFileHandler(addonFolderName);
        this.curseBaseUrl = curseBaseUrl;
    }
    
    @Override
    public void downloadToWow(Addon newAddon, String downloadUrl) {
        Set<String> addonFolders = downloadAndExtract(downloadUrl);
        String[] split = StringUtils.split(downloadUrl, '/');
        String zipFilename = split[split.length - 1];
        int fileId = extractFileId(split);
        newAddon.setVersionId(fileId);
        newAddon.setLastZipFileName(zipFilename);
        newAddon.setFolders(addonFolders);
    }
    
    
    private boolean downloadToWow(Addon newAddon) {
        try {
            String downloadUrl = getDownloadUrl(newAddon);
            downloadToWow(newAddon, downloadUrl);
            return true;
        } catch (NoSuchElementException e) {
            LOG.warn("No addon found with the name '" + newAddon.getAddonNameId() + "'. Skipping.");
            LOG.debug("No addon found", e);
        }
        return false;
    }

    protected Set<String> downloadAndExtract(String downloadUrl) {
        try {
            URL website = new URL(downloadUrl);
            URLConnection connection = website.openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);

            String location = connection.getHeaderField("Location");
            if (location == null) {
                return folderHandler.download(connection.getInputStream());
            } else {
                return downloadAndExtract(location);
            }
        } catch(IOException e) {
            throw new BusinessException("Problems reading data from Curse.", e);
        }
    }



    public static String extractZipFileName(String downloadUrl) {
        int lastIndexOf = downloadUrl.lastIndexOf('/');
        if (lastIndexOf == -1) {
            throw new BusinessException("Download url wrong");
        }
        return downloadUrl.substring(lastIndexOf + 1);
    }
    
    public static int extractFileId(String[] split) {
        String join = StringUtils.join(split[split.length-URL_ID_PART_START],
                split[split.length-URL_ID_PART_END]);
        return Integer.parseInt(join);
    }

    @Override
    public String getDownloadUrl(Addon addon) {
        if (addon.getReleaseStatus() != ReleaseStatus.RELEASE) {
            CurseForge curseForgeHandler = new CurseForge();
            return curseForgeHandler.getDownloadUrl(addon);
        } else {
            return getDownloadUrlStable(addon);
        }
    }

    private String getDownloadUrlStable(Addon addon) {
        String url = curseBaseUrl + addon.getAddonNameId() + "/download";
        try {
            LOG.debug("accessing {}", url);
            HttpClient httpClient = new HttpClient();
            GetMethod method = new GetMethod(url);
            method.setRequestHeader("user-agent", USER_AGENT);
            int status = executeHttpMethod(httpClient, method);
            LOG.debug("state {}", status);
            String downloadUrl = "";
            try (BufferedReader reader = new BufferedReader
                    (new InputStreamReader(getDownloadStream(method), CHARSET_WEBSITE))) {
                downloadUrl = readLines(addon, reader);
            }
            if (downloadUrl.isEmpty()) {
                throw new NoSuchElementException("Addon couldn't be found");
            }
            return downloadUrl;
        } catch (IOException e) {
            throw new BusinessException("Can't access " + url, e);
        } catch (NumberFormatException e) {
            throw new BusinessException("Can't parse addon numerical id.", e);
        }
    }

    private String readLines(Addon addon, BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (addon.getAddonId() == 0) {
                String parseAttribute = WebsiteHelper.parseAttribute(line, HTML_ATTRIBUTE_ADDON_ID);
                if (!parseAttribute.isEmpty()) {
                    addon.setAddonId(Integer.parseInt(parseAttribute));
                }
            }
            String downloadUrl = WebsiteHelper.parseAttribute(line, HTML_ATTRIBUTE_DOWN_URL);
            if (addon.getAddonId() != 0 && !downloadUrl.isEmpty()) {
                return downloadUrl;
            }
        }
        return "";
    }

    protected InputStream getDownloadStream(GetMethod method)
            throws IOException {
        return method.getResponseBodyAsStream();
    }

    protected int executeHttpMethod(HttpClient httpClient, GetMethod method)
            throws IOException {
        return httpClient.executeMethod(method);
    }

    @Override
    public void removeAddons(Collection<Addon> toDelete) {
        for (Addon addon : toDelete) {
            removeAddon(addon);
        }
    }
    
    @Override
    public void removeAddon(Addon toDelete) {
        folderHandler.removeAddonFolders(toDelete.getFolders());
    }

    @Override
    public List<Addon> downloadToWow(List<Addon> toDownload) {
        List<Addon> downloadedAddons = new ArrayList<>();
        for (Addon addon : toDownload) {
            LOG.info("adding " + addon.getAddonNameId());
            if (downloadToWow(addon)) {
                downloadedAddons.add(addon);
                LOG.info("addon " + addon.getAddonNameId() + " was added");
            }
        }
        return downloadedAddons;
    }
}
