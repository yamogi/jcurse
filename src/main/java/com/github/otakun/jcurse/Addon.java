package com.github.otakun.jcurse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public final class Addon implements Comparable<Addon> {
	
	private String gameAddonNameId;
	
	private String lastZipFileName;
	
	private Set<String> folders;
	
	/**
	 * Creates an {@link Addon} instance. 
	 * 
	 * @param addon the short name of the wow addon, which is part of the url.
	 */
	private Addon() {
	}
	
	public static List<Addon> newWowInstance(Collection<String> addonNames) {
		List<Addon> addons = new ArrayList<>();
		for (String addonName : addonNames) {
			addons.add(newWowInstance(addonName));
		}
		return addons;
	}

	public static Addon newWowInstance(String shortAddonName) {
		Addon addon = new Addon();
		addon.gameAddonNameId = "wow/" + shortAddonName;
		return addon;
	}
	
	public static Addon newInstance(String gameAddonName) {
		Addon addon = new Addon();
		addon.gameAddonNameId = gameAddonName;
		return addon;
	}

	public String getGameAddonNameId() {
		return gameAddonNameId;
	}

	public void setGameAddonNameId(String gameAddonNameId) {
		this.gameAddonNameId = gameAddonNameId;
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
		return gameAddonNameId.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return gameAddonNameId.equals(obj);
	}

	@Override
	public int compareTo(Addon addon) {
		return gameAddonNameId.compareTo(addon.gameAddonNameId);
	}
	
	@Override
	public String toString() {
		return "Addon '" + gameAddonNameId + "', version '" + lastZipFileName + "'";
	}

	
}
