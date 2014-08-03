package org.bitbucket.keiki.jcurse.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.bitbucket.keiki.jcurse.data.BusinessException;

public class AddonFileHandler {

    private static final int DOWNLOAD_BUFFER_SIZE = 4096;
    
    private String addonFolderName;

    public AddonFileHandler(String addonFolderName) {
        this.addonFolderName = addonFolderName;
    }

    Set<String> download(InputStream zippedInputstream) throws IOException {
        //create output directory is not exists
        File folder = new File(addonFolderName);
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IOException("Could not create parent directories for folder '" + addonFolderName + "'.");
        }
        try (ZipInputStream zis = new ZipInputStream(zippedInputstream)) {
            return downloadZipInputstream(zis);
        }
    }

    private Set<String> downloadZipInputstream(ZipInputStream zis) throws IOException {
        Set<String> addonFolders = new HashSet<>();
        byte[] buffer = new byte[DOWNLOAD_BUFFER_SIZE];
        ZipEntry ze;
        while ((ze = zis.getNextEntry()) != null) {
            if (ze.isDirectory()) {
                continue;
            }
            String fileName = ze.getName();
            int index = fileName.indexOf('/');
            addonFolders.add(fileName.substring(0, index));

            writeFile(zis, buffer, fileName);
        }
        return addonFolders;
    }

    private File writeFile(ZipInputStream zis, byte[] buffer, String fileName) throws IOException {
        File newFile = new File(addonFolderName + File.separator + fileName);
        if (!newFile.getParentFile().exists() && !newFile.getParentFile().mkdirs()) {
            throw new IOException("Couldn't create directories.");
        }
        try (FileOutputStream fos = new FileOutputStream(newFile)) {             
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        }
        return newFile;
    }
    
    void removeAddonFolders(Collection<String> toDelete) {
        try {
            for (String folderName : toDelete) {
                String path = addonFolderName + folderName;
                FileUtils.deleteDirectory(new File(path));
            }
        } catch (IOException e) {
            throw new BusinessException("Error removing Addon folders " + toDelete, e);
        }
    }
}
