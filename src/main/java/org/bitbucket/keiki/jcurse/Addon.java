package com.github.otakun.jcurse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public final class Addon implements Comparable<Addon> {
	
	private String addonNameId;
	
	private String lastZipFileName;
	
	private Set<String> folders;
	
	/**
	 * Creates an {@link Addon} instance. 
	 * 
	 * @param addon the short name of the wow addon, which is part of the url.
	 */
	private Addon() {
	}
	
	public static List<Addon> newInstance(Collection<String> addonNames) {
		List<Addon> addons = new ArrayList<>();
		for (String addonName : addonNames) {
			addons.add(newInstance(addonName));
		}
		return addons;
	}

	private static Addon newInstance(String shortAddonName) {
		Addon addon = new Addon();
		addon.addonNameId = shortAddonName;
		return addon;
	}
	
	public String getAddonNameId() {
		return addonNameId;
	}

	public void setAddonNameId(String addonNameId) {
		this.addonNameId = addonNameId;
	}

	public String getLastZipFileName() {
		return lastZipFileName;
	}

	public void setLastZipFileName(String lastZipFileName) {
		this.lastZipFileName = lastZipFileName;
	}

	public Set<String> getFolders() {
		return folders;
	}

	public void setFolders(Set<String> folders) {
		this.folders = folders;
	}

	@Override
	public int hashCode() {
		return addonNameId.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return addonNameId.equals(obj);
	}

	@Override
	public int compareTo(Addon addon) {
		return addonNameId.compareTo(addon.addonNameId);
	}
	
	@Override
	public String toString() {
		return "Addon '" + addonNameId + "', version '" + lastZipFileName + "'";
	}

	
}