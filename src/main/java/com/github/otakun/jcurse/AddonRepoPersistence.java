package com.github.otakun.jcurse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;


public final class AddonRepoPersistence {

	private static final Path PATH_REPO_FILE = Paths.get(Configuration.CONFIG_PATH + "repository");
	
	private static final String headerLine = "# DO NOT CHANGE THIS FILE! I have warned you!";
	
	public synchronized static TreeSet<Addon> loadInstalledAddons() {
	    if (!Files.exists(PATH_REPO_FILE)) {
	    	try {
	    		Files.createDirectories(PATH_REPO_FILE.getParent());
				Files.write(PATH_REPO_FILE, Arrays.asList(headerLine), Configuration.CHARSET, StandardOpenOption.CREATE_NEW);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    TreeSet<Addon> addons = new TreeSet<>();
	    try {
	    	List<String> lines = Files.readAllLines(PATH_REPO_FILE, Configuration.CHARSET);
	    	for (String addonLine : lines) {
	    		if (addonLine.startsWith("#")) {
	    			continue;
	    		}
	    		String[] addonAttributes = addonLine.split(";");
	    		Addon addon = Addon.newInstance(addonAttributes[0]);
	    		addon.setLastZipFileName(addonAttributes[1]);
	    		addons.add(addon);
	    	}
	    	return addons;
	    } catch (IOException e) {
	    	// TODO Auto-generated catch block
	    	e.printStackTrace();
	    	throw new RuntimeException(e);
	    }
	}
	
	public synchronized static void saveInstalledAddons(TreeSet<Addon> addons) {
		List<String> lines = new ArrayList<>();
		for (Addon addon : addons) {
			lines.add(addon.getIdBaseUrl() + ";" + addon.getLastZipFileName());
		}
		
		try {
			Files.write(PATH_REPO_FILE, lines, Configuration.CHARSET, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
