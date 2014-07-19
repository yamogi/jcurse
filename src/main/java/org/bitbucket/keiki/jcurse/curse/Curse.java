package org.bitbucket.keiki.jcurse.curse;

import java.util.Collection;
import java.util.List;

import org.bitbucket.keiki.jcurse.data.Addon;

public interface Curse {

    boolean downloadToWow(Addon newAddon);

    void removeAddons(Collection<Addon> toDelete);

    List<Addon> downloadToWow(List<Addon> toDownload);

    void downloadToWow(Addon newAddon, String downloadUrl);

    String getDownloadUrl(Addon gameAddonNameId);

    void removeAddon(Addon toDelete);

}