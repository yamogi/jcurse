package org.bitbucket.keiki.jcurse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Configuration {
    
    static final String CONFIG_PATH = System.getProperty("user.home") + File.separator 
                                                            + ".jcurse" + File.separator;
    
    public static final Charset CHARSET = Charset.forName("ASCII");
    
    private static final Logger LOG = LoggerFactory.getLogger(Configuration.class); 
    
    private static final String CONFIG_FILE_LOCATION = CONFIG_PATH + "config";
    
    private static final String WOW_FOLDER_KEY = "wow.folder";
    
    private static final Configuration INSTANCE = new Configuration();
    
    private String wowFolder;

    private String curseBaseUrl = "http://www.curse.com/addons/wow/"; 
    
    private static boolean toInitialize = true;
    
    private Configuration() {
    }
    
    public static synchronized Configuration getConfiguration() {
        if (toInitialize) {
            loadProperties(CONFIG_FILE_LOCATION);
            toInitialize = false;
        }
        return INSTANCE;
    }
    
    static Configuration loadProperties(String fileLocation) {
        File propertyFile = new File(fileLocation);
        Properties properties = new Properties();
        if (!propertyFile.exists()) {
            File parentDirectory = propertyFile.getParentFile();
            if (!parentDirectory.exists() && !parentDirectory.mkdirs()) {
                LOG.error("Couldn't create parent directories of config file in '" + propertyFile + "'");
            } else {
                properties.put(WOW_FOLDER_KEY, "");
                try (FileOutputStream fos = new FileOutputStream(propertyFile)) {
                    properties.store(fos, null);
                } catch (IOException e) {
                    throw new BusinessException("Can't create config file at '" 
                            + propertyFile.getAbsolutePath() + "'", e);
                }
            }
        } else {
            try (FileInputStream fis = new FileInputStream(propertyFile)) {
                properties.load(fis);
            } catch (IOException e) {
                throw new BusinessException("Can't read existing config file at '"
                        + propertyFile + "'", e);
            }
        }
        INSTANCE.wowFolder = properties.getProperty(WOW_FOLDER_KEY);
        return INSTANCE;
    }

    private String getWowFolder() {
        if (wowFolder.isEmpty()) {
            throw new BusinessException("No folder for WoW given in config file (home/.jcurse/config)");
        }
        return wowFolder;
    }
    
    public String getWowAddonFolder() {
        return getWowFolder() 
        + File.separator + "Interface" + File.separator + "AddOns" + File.separator;
    }

    public String getCurseBaseUrl() {
        return curseBaseUrl;
    }

    public void setCurseBaseUrl(String curseBaseUrl) {
        this.curseBaseUrl = curseBaseUrl;
    }

    void setWowFolder(String wowFolder) {
        this.wowFolder = wowFolder;
    }
}
