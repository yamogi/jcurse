package com.github.otakun.jcurse.api;

import java.util.Collection;

import com.github.otakun.jcurse.Addon;

public interface AddonRepoPersistence {

	public abstract Collection<Addon> loadInstalledAddons();

	public abstract void saveInstalledAddons(Collection<Addon> addons);

}