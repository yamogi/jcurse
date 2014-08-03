package org.bitbucket.keiki.jcurse;

public interface Configuration {

    void load();

    void save();

    String getWowAddonFolder();

    void setWowFolder(String wowFolder);

}