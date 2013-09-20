package org.bitbucket.keiki.jcurse;

import java.util.Collection;

public interface AddonRepoPersistence {

    Collection<Addon> loadInstalledAddons();

    void saveInstalledAddons(Collection<Addon> addons);

}