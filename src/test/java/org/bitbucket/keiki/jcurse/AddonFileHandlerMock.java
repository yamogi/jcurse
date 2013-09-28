package org.bitbucket.keiki.jcurse;

import java.util.Collection;
import java.util.List;

public class AddonFileHandlerMock implements AddonFileHandler {

    @Override
    public boolean downloadToWow(Addon newAddon) {
        newAddon.setLastZipFileName(newAddon.getAddonNameId() + "-1.0.zip");
        return true;
    }

    @Override
    public void removeAddons(Collection<Addon> toDelete) {
    }

    @Override
    public void removeAddonFolders(Collection<String> toDpublicelete) {
    }

    @Override
    public String getCompressedFileName(String addonNameId) {
        return addonNameId + "-1.0.zip";
    }

    @Override
    public List<Addon> downloadToWow(List<Addon> toDownload) {
        if (!toDownload.isEmpty() && toDownload.get(0).getAddonNameId().equals("unknownAddon")) {
            return toDownload.subList(1, toDownload.size());
        }
        return toDownload;
    }

    @Override
    public void downloadToWow(Addon newAddon, String downloadUrl) {
        newAddon.setLastZipFileName(newAddon.getAddonNameId() + "-1.0.zip");
    }

    @Override
    public String getDownloadUrl(String gameAddonNameId) {
        return "http://localhost/" + gameAddonNameId + "-1.0.zip";
    }

}
