package com.github.otakun.jcurse;

import java.util.Collection;
import java.util.TreeMap;


public final class AddonRepositoryManager {

	private static final AddonRepositoryManager INSTANCE = new AddonRepositoryManager();
	
	private TreeMap<Addon, Addon> repository;
	
	private static boolean toInitialize = true;
	
	private AddonRepositoryManager() {
	}
	
	public static synchronized AddonRepositoryManager getInstance() {
		if (toInitialize) {
			Collection<Addon> addons = AddonRepoPersistence.loadInstalledAddons();
			TreeMap<Addon, Addon> tmpTree = new TreeMap<>();
			for (Addon addon : addons) {
				tmpTree.put(addon, addon);
			}
			INSTANCE.repository = tmpTree;
			toInitialize = false;
		}	
		return INSTANCE;
	}



	public void add(String addonName) {
		
		Addon newAddon = Addon.newWowInstance(addonName);

		if (repository.containsKey(newAddon)) {
			throw new RuntimeException("Addon '" + addonName + "' already added.");
			//XXX better error handling
		}
		
		CurseAddonFileHandler.downloadToWow(newAddon);
		
		repository.put(newAddon, newAddon);
		
		AddonRepoPersistence.saveInstalledAddons(repository.values());
	}



	public void remove(String addonName) {
		System.out.println("Remove addon " + addonName);
		Addon newAddon = Addon.newWowInstance(addonName);
		if (!repository.containsKey(newAddon)) {
			throw new RuntimeException("Addon '" + addonName + "' does not exist to begin with.");
			//XXX better error handling
		}
		CurseAddonFileHandler.removeAddonFolders(repository.get(newAddon).getFolders());
		repository.remove(newAddon);
		AddonRepoPersistence.saveInstalledAddons(repository.values());
		System.out.println("Removed " + addonName);
	}



	public void updateAll() {
		System.out.println("updating all addons");
		for (Addon addon : repository.values()) {
			updateInternal(addon);
		}
		AddonRepoPersistence.saveInstalledAddons(repository.values());
		System.out.println("done updating all addons");
	}


	private void updateInternal(Addon addon) {
		String fileName = CurseAddonFileHandler.getCompressedFileName(addon.getGameAddonNameId());
		if (addon.getLastZipFileName().equals(fileName)) {
			System.out.println(addon.getGameAddonNameId() + " already up2date");
			return;
		}
		System.out.println("updating " + addon.getGameAddonNameId());
		CurseAddonFileHandler.removeAddonFolders(repository.get(addon).getFolders());
		CurseAddonFileHandler.downloadToWow(addon);
		
		repository.put(addon, addon);
		System.out.println("updated " + addon.getGameAddonNameId());
	}

	public void update(String addonName) {
		System.out.println("updating " + addonName);
		Addon newAddon = Addon.newWowInstance(addonName);
		if (!repository.containsKey(newAddon)) {
			throw new RuntimeException("Addon '" + addonName + "' is not installed, use the add command to add it.");
			//XXX better error handling
		}
		updateInternal(repository.get(newAddon));
		AddonRepoPersistence.saveInstalledAddons(repository.values());
		System.out.println("done updating " + addonName);
	}

	public Collection<Addon> getAddons() {
		return repository.values();
	}

}
