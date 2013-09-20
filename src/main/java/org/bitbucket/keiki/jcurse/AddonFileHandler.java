package org.bitbucket.keiki.jcurse;

import java.util.Collection;
import java.util.List;

public interface AddonFileHandler {

    boolean downloadToWow(Addon newAddon);

    void removeAddons(Collection<Addon> toDelete);

    void removeAddonFolders(Collection<String> toDelete);

    String getCompressedFileName(String gameAddonNameId);

    List<Addon> downloadToWow(List<Addon> toDownload);

    boolean downloadToWow(Addon newAddon, String downloadUrl);

    String getDownloadUrl(String gameAddonNameId);

}