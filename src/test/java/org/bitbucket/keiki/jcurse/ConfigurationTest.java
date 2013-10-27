package org.bitbucket.keiki.jcurse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ConfigurationTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Test (expected = BusinessException.class)
    public void testLoadPropertiesNotExisting() {
        File root = folder.getRoot();
        String configFile = root.getAbsolutePath() + File.separator + "config" + File.separator + "configFile";
        
        ConfigurationImpl config = new ConfigurationImpl();
        config.load(configFile);
    }
    
    @Test
    public void testLoadProperties() throws IOException {
        File newFile = folder.newFile();
        FileOutputStream fos = new FileOutputStream(newFile);
        fos.write("wow.folder=this is a test".getBytes());
        fos.close();
        ConfigurationImpl configuration = new ConfigurationImpl();
        configuration.load(newFile.getAbsolutePath());
        
        String wowAddonFolder = configuration.getWowAddonFolder();
        assertEquals("this is a test/Interface/AddOns/", wowAddonFolder);
    }
    
    @Test (expected = BusinessException.class)
    public void testEmptyWowFolder() {
        Configuration config = new ConfigurationImpl();
        config.setWowFolder("");
        assertTrue(config.getWowAddonFolder().isEmpty());
    }
    
    @Test
    public void testSaveWowDirectory() {
        ConfigurationImpl config = new ConfigurationImpl();
        config.setWowFolder("mytestfolder");
        String absolutePath = folder.getRoot().getAbsolutePath() + File.separator + "config" + File.separator + "configFile";;
        config.save(absolutePath);
        config.load(absolutePath);
        assertEquals("mytestfolder" + File.separator + "Interface" + File.separator + "AddOns" + File.separator, config.getWowAddonFolder());
    }
}
