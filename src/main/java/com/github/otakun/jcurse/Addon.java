package com.github.otakun.jcurse;

public class Addon {
	
	private String idBaseUrl;
	
	/**
	 * Creates an {@link Addon} instance. 
	 * 
	 * @param addon the short name of the wow addon, which is part of the url.
	 * @return a new instance of {@link Addon}.
	 */
	public static Addon createAddon(String addonName) {
		Addon addon = new Addon();
		addon.idBaseUrl = "wow/" + addonName;
		return addon;
	}

	public String getIdBaseUrl() {
		return idBaseUrl;
	}
}
