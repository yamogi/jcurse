package org.bitbucket.keiki.jcurse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class AddonRepositoryManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(AddonRepositoryManager.class);

	private final AddonRepoPersistence persistence;
	
	private final AddonFileHandler curse;

	private final TreeMap<Addon, Addon> repository;
	
	public AddonRepositoryManager() {
		this(new AddonRepoPersistenceImpl(Configuration.CONFIG_PATH + "repository"),
				new CurseAddonFileHandler());
	}
	
	public AddonRepositoryManager(AddonRepoPersistence persistence,
			AddonFileHandler addonFileHandler) {
		this.persistence = persistence;
		this.curse = addonFileHandler;
		Collection<Addon> addons = persistence.loadInstalledAddons();
		TreeMap<Addon, Addon> tmpTree = new TreeMap<>();
		for (Addon addon : addons) {
			tmpTree.put(addon, addon);
		}
		repository = tmpTree;
	}
	
	public void add(Collection<String> addonName) {
		List<Addon> newAddons = Addon.newInstance(addonName);

		List<Addon> toDownload = checkAddonAlreadyExists(newAddons, false);
		
		curse.downloadToWow(toDownload);
		
		updateRepository(toDownload);
	}

	private void updateRepository(List<Addon> toDownload) {
		for (Addon addon : toDownload) {
			repository.put(addon, addon);
		}
		persistence.saveInstalledAddons(repository.values());
	}

	private List<Addon> checkAddonAlreadyExists(List<Addon> newAddons, boolean getExistingAddons) {
		List<Addon> toDownload = new ArrayList<>();
		List<Addon> toUpdate = new ArrayList<>();
		for (Addon addon : newAddons) {
			Addon repoAddon = repository.get(addon);
			if (repoAddon != null) {
				toUpdate.add(repoAddon);
			} else {
				toDownload.add(addon);
			}
		}
		if (getExistingAddons) {
			if (!toDownload.isEmpty()) {
				LOG.info("The Addon(s) " + toDownload + "are not added.");
			}
			return toUpdate;
		} else {
			if (!toUpdate.isEmpty()) {
				LOG.info("The Addon(s) " + toUpdate + " are already installed.");
			}
			return toDownload;
		}
	}



	public void remove(List<String> addons) {
		List<Addon> newAddons = Addon.newInstance(addons);
		List<Addon> repoAddons = getCheckAddons(newAddons);
		curse.removeAddons(repoAddons);
		removeAddonsFromRepo(repoAddons);
	}

	private void removeAddonsFromRepo(List<Addon> repoAddons) {
		for (Addon addon : repoAddons) {
			repository.remove(addon);
		}	
		persistence.saveInstalledAddons(repository.values());
	}

	private List<Addon> getCheckAddons(List<Addon> newAddons) {
		List<Addon> addons = new ArrayList<>();
		for (Addon addon : newAddons) {
			Addon repoAddon = repository.get(addon);
			if (repoAddon == null) {
				throw new RuntimeException("The addon " + addon.getAddonNameId() + " is not in our repository.");
				//XXX better error handling
			}
			addons.add(repoAddon);
		}
		return addons;
	}

	public void updateAll() {
		LOG.info("updating all addons");
		for (Addon addon : repository.values()) {
			updateInternal(addon);
		}
		persistence.saveInstalledAddons(repository.values());
		LOG.info("done updating all addons");
	}


	private void updateInternal(List<Addon> repoAddons) {
		for (Addon addon : repoAddons) {
			updateInternal(addon);	
		}
	}

	private void updateInternal(Addon addon) {
		String fileName = curse.getCompressedFileName(addon.getAddonNameId());
		if (addon.getLastZipFileName().equals(fileName)) {
			LOG.info(addon.getAddonNameId() + " already up2date");
			return;
		}
		LOG.info("updating " + addon.getAddonNameId());
		curse.removeAddonFolders(repository.get(addon).getFolders());
		curse.downloadToWow(addon);
		
		repository.put(addon, addon);
		LOG.info("updated " + addon.getAddonNameId());
	}

	public void update(List<String> addons) {
		LOG.info("updating " + addons);
		List<Addon> newAddon = Addon.newInstance(addons);
		List<Addon> repoAddons = checkAddonAlreadyExists(newAddon, true);
		updateInternal(repoAddons);
		persistence.saveInstalledAddons(repository.values());
		LOG.info("done updating " + addons);
	}

	public Collection<Addon> getAddons() {
		return repository.values();
	}
}
