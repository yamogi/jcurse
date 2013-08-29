package com.github.otakun.jcurse;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;


public final class AddonRepoPersistence {

	private final File repoFile;
	
	public AddonRepoPersistence(String pathToRepoFile) {
		this.repoFile = new File(pathToRepoFile);
	}
	
	public synchronized Collection<Addon> loadInstalledAddons() {
		try {
			if (!repoFile.exists()) {//FIXME: check for file permission
				return Collections.emptyList();
			}
			ObjectMapper mapper = new ObjectMapper();
			Object readValue = mapper.readValue(repoFile, new TypeReference<Collection<Addon>>() {});
			@SuppressWarnings("unchecked")
			Collection<Addon> result = (Collection<Addon>) readValue; 
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public synchronized void saveInstalledAddons(Collection<Addon> addons) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(repoFile, addons);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
