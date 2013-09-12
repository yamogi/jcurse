package org.bitbucket.keiki.jcurse;

import java.util.Collection;

public interface AddonRepoPersistence {

	public abstract Collection<Addon> loadInstalledAddons();

	public abstract void saveInstalledAddons(Collection<Addon> addons);

}