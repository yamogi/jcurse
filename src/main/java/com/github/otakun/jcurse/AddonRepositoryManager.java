package com.github.otakun.jcurse;

import java.util.Collection;
import java.util.TreeSet;


public class AddonRepositoryManager {

	private static final AddonRepositoryManager INSTANCE = new AddonRepositoryManager();
	
	private TreeSet<Addon> repository;
	
	private static boolean toInitialize = true;
	
	public static synchronized AddonRepositoryManager getInstance() {
		if (toInitialize) {
			INSTANCE.repository = AddonRepoPersistence.loadInstalledAddons();
			toInitialize = false;
		}	
		return INSTANCE;
	}



	public void add(String addonName) {
		
		Addon newAddon = Addon.newWowInstance(addonName);

		if (repository.contains(newAddon)) {
			throw new RuntimeException("Addon '" + addonName + "' already added.");
			//XXX better error handling
		}
		
		String zipFilename = CurseHandler.downloadToWow(newAddon);
		newAddon.setLastZipFileName(zipFilename);
		
		repository.add(newAddon);
		
		AddonRepoPersistence.saveInstalledAddons(repository);
	}



	public void remove(String string) {
		// TODO Auto-generated method stub
		
	}



	public void updateAll() {
		// TODO Auto-generated method stub
		
	}



	public void update(String string) {
		// TODO Auto-generated method stub
		
	}

	public Collection<Addon> getAddons() {
		return repository;
	}

}
