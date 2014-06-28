package org.bitbucket.keiki.jcurse.curse;

import java.util.Collection;
import java.util.List;

import org.bitbucket.keiki.jcurse.Addon;

public interface AddonFileHandler {

    boolean downloadToWow(Addon newAddon);

    void removeAddons(Collection<Addon> toDelete);

    void removeAddonFolders(Collection<String> toDelete);

    List<Addon> downloadToWow(List<Addon> toDownload);

    void downloadToWow(Addon newAddon, String downloadUrl);

    String getDownloadUrl(Addon gameAddonNameId);

}