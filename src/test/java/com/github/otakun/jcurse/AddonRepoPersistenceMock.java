package com.github.otakun.jcurse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class AddonRepoPersistenceMock implements AddonRepoPersistence {

	private final List<Addon> list = new ArrayList<>();
	
	public AddonRepoPersistenceMock() {
		list.addAll(Addon.newInstance(Arrays.asList("test1", "test2")));
	}
	
	@Override
	public Collection<Addon> loadInstalledAddons() {
		return list;
	}

	@Override
	public void saveInstalledAddons(Collection<Addon> addons) {
		list.clear();
		list.addAll(addons);
	}

}
