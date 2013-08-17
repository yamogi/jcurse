package com.github.otakun.jcurse;

import java.util.HashSet;
import java.util.Set;

public final class Addon implements Comparable<Addon> {
	
	private String baseUrlId;
	
	private String lastZipFileName;
	
	private Set<String> folders;
	
	/**
	 * Creates an {@link Addon} instance. 
	 * 
	 * @param addon the short name of the wow addon, which is part of the url.
	 */
	private Addon() {
	}
	
	public static Addon newWowInstance(String shortAddonName) {
		Addon addon = new Addon();
		addon.baseUrlId = "wow/" + shortAddonName;
		return addon;
	}
	
	public static Addon newInstance(String gameAddonName) {
		Addon addon = new Addon();
		addon.baseUrlId = gameAddonName;
		return addon;
	}

	public String getIdBaseUrl() {
		return baseUrlId;
	}

	public String getLastZipFileName() {
		return lastZipFileName;
	}

	public void setLastZipFileName(String lastZipFileName) {
		this.lastZipFileName = lastZipFileName;
	}
	
	public Set<String> getFolders() {
		if (folders == null) {
			folders = new HashSet<>();
		}
		return folders;
	}

	@Override
	public int hashCode() {
		return baseUrlId.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return baseUrlId.equals(obj);
	}

	@Override
	public int compareTo(Addon addon) {
		return baseUrlId.compareTo(addon.baseUrlId);
	}
	
	@Override
	public String toString() {
		return "Addon '" + baseUrlId + "', version '" + lastZipFileName + "'";
	}
	
}
