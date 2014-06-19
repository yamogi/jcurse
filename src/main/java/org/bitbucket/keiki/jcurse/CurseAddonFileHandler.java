package org.bitbucket.keiki.jcurse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang3.StringUtils;
import static org.bitbucket.keiki.jcurse.Constants.*;

public class CurseAddonFileHandler implements AddonFileHandler {
    
    private static final String HTML_ATTRIBUTE_DOWN_URL = "data-href";
	private static final String HTML_ATTRIBUTE_ADDON_ID = "data-project";
    private static final int DOWNLOAD_BUFFER_SIZE = 4096;

    private static final Logger LOG = LoggerFactory.getLogger(CurseAddonFileHandler.class);
    private final String curseBaseUrl;
    private final String addonFolderName;  

    
    public CurseAddonFileHandler(String addonFolderName, String curseBaseUrl) {
        this.addonFolderName = addonFolderName;
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
    
    
    @Override
    public boolean downloadToWow(Addon newAddon) {
        try {
            String downloadUrl = getDownloadUrl(newAddon);
            downloadToWow(newAddon, downloadUrl);
            return true;
        } catch (NoSuchElementException e) {
            LOG.warn("No addon found with the name '" + newAddon.getAddonNameId() + "'. Skipping.");
        }
        return false;
    }

    private Set<String> downloadAndExtract(String downloadUrl) {
        try {
            URL website = new URL(downloadUrl);
            URLConnection connection = website.openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            byte[] buffer = new byte[DOWNLOAD_BUFFER_SIZE];

            //create output directory is not exists
            File folder = new File(addonFolderName);
            if (!folder.exists()){
                folder.mkdirs();
            }

            try (ZipInputStream zis = new ZipInputStream(connection.getInputStream())) {

                Set<String> addonFolders = new HashSet<>();
                
                ZipEntry ze;
                while ((ze = zis.getNextEntry()) != null) {
                    if (ze.isDirectory()) {
                        continue;
                    }
                    String fileName = ze.getName();
                    int index = fileName.indexOf('/');
                    addonFolders.add(fileName.substring(0, index));


                    File newFile = new File(addonFolderName + File.separator + fileName);
                    System.out.println(newFile.getAbsoluteFile());
                    newFile.getParentFile().mkdirs();

                    try (FileOutputStream fos = new FileOutputStream(newFile)) {             
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                return addonFolders;
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
    private static final int URL_ID_PART_END = 2;
    private static final int URL_ID_PART_START = 3;

    @Override
    public String getDownloadUrl(Addon addon) {
    	if (addon.getReleaseStatus() != ReleaseStatus.RELEASE) {
    		CurseForgeHandler curseForgeHandler = new CurseForgeHandler();
    		return curseForgeHandler.getDownloadUrl(addon);
    	} else {
    		return getDownloadUrlRelease(addon);
    	}
    }

	private String getDownloadUrlRelease(Addon addon) {
		String url = curseBaseUrl + addon.getAddonNameId() + "/download";
        try {

            LOG.debug("accessing {}", url);
            HttpClient httpClient = new HttpClient();
            GetMethod method = new GetMethod(url);
            method.setRequestHeader("user-agent", USER_AGENT);
            int status = httpClient.executeMethod(method);
            LOG.debug("state {}", status);
            String downloadUrl = "";
            try (BufferedReader reader = new BufferedReader
                    (new InputStreamReader(method.getResponseBodyAsStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
					if (addon.getAddonId() == 0) {
						String parseAttribute = WebsiteHelper.parseAttribute(line, HTML_ATTRIBUTE_ADDON_ID);
						if (!parseAttribute.isEmpty()) {
							addon.setAddonId(Integer.parseInt(parseAttribute));
						}
					}
					downloadUrl = WebsiteHelper.parseAttribute(line, HTML_ATTRIBUTE_DOWN_URL);
                    if (addon.getAddonId() != 0 && !downloadUrl.isEmpty()) {
                    	break;
                    }
                }
            }
            if (downloadUrl.isEmpty()) {
                throw new NoSuchElementException("Addon couldn't be found");
            }
            return downloadUrl;
        } catch (IOException e) {
            throw new BusinessException("Can't access " + url, e);
        } catch (NumberFormatException e) {
        	throw new BusinessException("Can't parse addon numerical id.");
        }
	}

    @Override
    public void removeAddons(Collection<Addon> toDelete) {
        for (Addon addon : toDelete) {
            removeAddonFolders(addon.getFolders());
        }
    }

    
    
    @Override
    public void removeAddonFolders(Collection<String> toDelete) {
        try {
            for (String folderName : toDelete) {
                String path = addonFolderName + folderName;
                FileUtils.deleteDirectory(new File(path));
            }
        } catch (IOException e) {
            throw new BusinessException("Error removing Addon folders " + toDelete, e);
        }
        
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
