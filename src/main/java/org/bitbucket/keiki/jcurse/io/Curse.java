package org.bitbucket.keiki.jcurse.io;

import java.util.Collection;
import java.util.List;

import org.bitbucket.keiki.jcurse.data.Addon;

public interface Curse {

    void removeAddons(Collection<Addon> toDelete);

    List<Addon> downloadToWow(List<Addon> toDownload);

    void downloadToWow(Addon newAddon, String downloadUrl);

    String getDownloadUrl(Addon gameAddonNameId);

    void removeAddon(Addon toDelete);

}