package org.bitbucket.keiki.jcurse;

import java.util.Collection;

interface AddonRepoPersistence {

    Collection<Addon> loadInstalledAddons();

    void saveInstalledAddons(Collection<Addon> addons);

}