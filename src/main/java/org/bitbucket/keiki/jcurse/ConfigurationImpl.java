package org.bitbucket.keiki.jcurse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.bitbucket.keiki.jcurse.data.BusinessException;

public final class ConfigurationImpl implements Configuration {
    
    static final String CONFIG_PATH = System.getProperty("user.home") + File.separator 
                                                            + ".jcurse" + File.separator;
    
    private static final String CONFIG_FILE_LOCATION = CONFIG_PATH + "config";
    
    private static final String WOW_FOLDER_KEY = "wow.folder";
    
    private volatile String wowFolder;

    @Override
    public void load() {
        load(CONFIG_FILE_LOCATION);
    }
    
    void load(String fileLocation) {
        File propertyFile = new File(fileLocation);
        Properties properties = new Properties();
        if (!propertyFile.exists()) {
            throw new BusinessException("No config exists. Please set the wow directory first.");
        } else {
            try (FileInputStream fis = new FileInputStream(propertyFile)) {
                properties.load(fis);
            } catch (IOException e) {
                throw new BusinessException("Can't read existing config file at '"
                        + propertyFile + "'", e);
            }
        }
        wowFolder = properties.getProperty(WOW_FOLDER_KEY);
    }
    
    @Override
    public void save() {
        save(CONFIG_FILE_LOCATION);
    }
    
    synchronized void save(String fileLocation) {
        File file = new File(fileLocation);
        File parentFile = file.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            throw new BusinessException("Can't create the directory pointing to the path '" + file.getParent() + "'.");
        }
        try (FileOutputStream fos = new FileOutputStream(file)) {
            Properties properties = new Properties();
            properties.setProperty(WOW_FOLDER_KEY, wowFolder);
            properties.store(fos, "jcurse config");
        } catch (IOException e) {
            throw new BusinessException("Can't write configuration to its file at '"
                    + file + "'. Aborting.", e);
        }
    }

    private String getWowFolder() {
        return wowFolder;
    }
    
    @Override
    public String getWowAddonFolder() {
        return getWowFolder() + File.separator + "Interface" + File.separator + "AddOns" + File.separator; 
    }
    
    @Override
    public void setWowFolder(String wowFolder) {
        File wowPath = new File(wowFolder);
        if (!wowPath.isDirectory()) {
            throw new BusinessException("WoW path given is no directory.");
        }
        this.wowFolder = wowPath.getAbsolutePath();
    }

    

}
