package org.bitbucket.keiki.jcurse;

import java.util.Collection;

import org.bitbucket.keiki.jcurse.data.Addon;

interface AddonRepoPersistence {

    Collection<Addon> loadInstalledAddons();

    void saveInstalledAddons(Collection<Addon> addons);

}