package org.bitbucket.keiki.jcurse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurseAddonFileHandler implements AddonFileHandler {
    
    private static final int DOWNLOAD_BUFFER_SIZE = 4096;
    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/28.0.1500.71 Chrome/28.0.1500.71 Safari/537.36";
    private static final Logger LOG = LoggerFactory.getLogger(CurseAddonFileHandler.class);  

    @Override
    public void downloadToWow(Addon newAddon, String downloadUrl) {
        String zipFilename = extractZipFileName(downloadUrl);

        Set<String> addonFolders = downloadAndExtract(downloadUrl);
        newAddon.setLastZipFileName(zipFilename);
        newAddon.setFolders(addonFolders);

        LOG.info("Done unzipping");
    }
    
    
    @Override
    public boolean downloadToWow(Addon newAddon) {
        try {
            String downloadUrl = getDownloadUrl(newAddon.getAddonNameId());
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
            String outputFolder = Configuration.getConfiguration().getWowAddonFolder();
            File folder = new File(outputFolder);
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


                    File newFile = new File(outputFolder + File.separator + fileName);

                    newFile.getParentFile().mkdirs();

                    try (FileOutputStream fos = new FileOutputStream(newFile)){
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

    @Override
    public String getDownloadUrl(String gameAddonNameId) {
        String url = Configuration.getConfiguration().getCurseBaseUrl() + gameAddonNameId + "/download";
        try {
            LOG.debug("accessing {}", url);
            URL downloadWebpage = new URL(url);
            URLConnection connection = downloadWebpage.openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            String downloadUrl = "";
            try (BufferedReader reader = new BufferedReader
                    (new InputStreamReader(downloadWebpage.openStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    int indexOf = line.indexOf("data-href");
                    if (indexOf >= 0) {
                        int indexOf2 = line.indexOf('\"', indexOf + 13);
                        downloadUrl = line.substring(indexOf + 11, indexOf2);
                        LOG.info(downloadUrl);
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
                String path = Configuration.getConfiguration().getWowAddonFolder() + folderName;
                FileUtils.deleteDirectory(new File(path));
            }
        } catch (IOException e) {
            throw new BusinessException("Error removing Addon folders " + toDelete, e);
        }
        
    }

    @Override
    public String getCompressedFileName(String gameAddonNameId) {
        String downloadUrl = getDownloadUrl(gameAddonNameId);
        return extractZipFileName(downloadUrl);
    }

    @Override
    public List<Addon> downloadToWow(List<Addon> toDownload) {
        List<Addon> downloadedAddons = new ArrayList<>();
        for (Addon addon : toDownload) {
            if (downloadToWow(addon)) {
                downloadedAddons.add(addon);
            }
        }
        return downloadedAddons;
    }
}
