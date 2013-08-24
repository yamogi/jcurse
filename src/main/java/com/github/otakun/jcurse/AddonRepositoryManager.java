package com.github.otakun.jcurse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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



	public void add(Collection<String> addonName) {
		List<Addon> newAddons = Addon.newInstance(addonName);

		List<Addon> toDownload = checkAddonAlreadyExists(newAddons);
		
		CurseAddonFileHandler.downloadToWow(toDownload);
		
		updateRepository(toDownload);
	}

	private void updateRepository(List<Addon> toDownload) {
		for (Addon addon : toDownload) {
			repository.put(addon, addon);
		}
		AddonRepoPersistence.saveInstalledAddons(repository.values());
	}

	private List<Addon> checkAddonAlreadyExists(List<Addon> newAddons) {
		List<Addon> toDownload = new ArrayList<>();
		for (Addon addon : newAddons) {
			if (repository.containsKey(addon)) {
				System.out.println("The Addon '" + addon.getAddonNameId() + "' is already installed.");
			} else {
				toDownload.add(addon);
			}
		}
		return toDownload;
	}



	public void remove(List<String> addons) {
		List<Addon> newAddons = Addon.newInstance(addons);
		List<Addon> repoAddons = getCheckAddons(newAddons);
		CurseAddonFileHandler.removeAddons(repoAddons);
		removeAddonsFromRepo(repoAddons);
		System.out.println("Removed " + newAddons);
	}

	private void removeAddonsFromRepo(List<Addon> repoAddons) {
		for (Addon addon : repoAddons) {
			repository.remove(addon);
		}	
		AddonRepoPersistence.saveInstalledAddons(repository.values());
	}

	private List<Addon> getCheckAddons(List<Addon> newAddons) {
		List<Addon> addons = new ArrayList<>();
		for (Addon addon : newAddons) {
			Addon repoAddon = repository.get(addon);
			if (repoAddon == null) {
				throw new RuntimeException("The addon " + addon.getAddonNameId() + "is not in our repository.");
				//XXX better error handling
			}
			addons.add(repoAddon);
		}
		return addons;
	}



	public void updateAll() {
		System.out.println("updating all addons");
		for (Addon addon : repository.values()) {
			updateInternal(addon);
		}
		AddonRepoPersistence.saveInstalledAddons(repository.values());
		System.out.println("done updating all addons");
	}


	private void updateInternal(List<Addon> repoAddons) {
		for (Addon addon : repoAddons) {
			updateInternal(addon);	
		}
	}

	private void updateInternal(Addon addon) {
		String fileName = CurseAddonFileHandler.getCompressedFileName(addon.getAddonNameId());
		if (addon.getLastZipFileName().equals(fileName)) {
			System.out.println(addon.getAddonNameId() + " already up2date");
			return;
		}
		System.out.println("updating " + addon.getAddonNameId());
		CurseAddonFileHandler.removeAddonFolders(repository.get(addon).getFolders());
		CurseAddonFileHandler.downloadToWow(addon);
		
		repository.put(addon, addon);
		System.out.println("updated " + addon.getAddonNameId());
	}

	public void update(List<String> addons) {
		System.out.println("updating " + addons);
		List<Addon> newAddon = Addon.newInstance(addons);
		List<Addon> repoAddons = checkAddonAlreadyExists(newAddon);
		updateInternal(repoAddons);
		AddonRepoPersistence.saveInstalledAddons(repository.values());
		System.out.println("done updating " + addons);
	}

	public Collection<Addon> getAddons() {
		return repository.values();
	}

}
