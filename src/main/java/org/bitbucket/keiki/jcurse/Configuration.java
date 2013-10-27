package org.bitbucket.keiki.jcurse;

public interface Configuration {

    void load();

    void save();

    String getWowAddonFolder();

    String getCurseBaseUrl();

    void setCurseBaseUrl(String curseBaseUrl);

    void setWowFolder(String wowFolder);

}