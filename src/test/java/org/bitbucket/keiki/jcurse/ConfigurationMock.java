package org.bitbucket.keiki.jcurse;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public class ConfigurationMock implements Configuration {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Override
    public void load() {
        
    }

    @Override
    public void save() {
    }

    @Override
    public String getWowAddonFolder() {
        return folder.getRoot().getAbsolutePath();
    }

    @Override
    public void setWowFolder(String wowFolder) {
    }

}
