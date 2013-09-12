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
    
    @Test
    public void testLoadPropertiesNotExisting() {
        File root = folder.getRoot();
        String configFile = root.getAbsolutePath() + File.separator + "config" + File.separator + "configFile";
        
        Configuration.loadProperties(configFile);
        
        assertTrue(new File(configFile).exists());
    }
    
    @Test
    public void testLoadProperties() throws IOException {
        File newFile = folder.newFile();
        FileOutputStream fos = new FileOutputStream(newFile);
        fos.write("wow.folder=this is a test".getBytes());
        fos.close();
        
        Configuration configuration = Configuration.loadProperties(newFile.getAbsolutePath());
        
        String wowAddonFolder = configuration.getWowAddonFolder();
        assertEquals("this is a test/Interface/AddOns/", wowAddonFolder);
    }
}
