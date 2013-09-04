package com.github.otakun.jcurse;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.github.otakun.jcurse.api.AddonRepoPersistence;


public final class AddonRepoPersistenceImpl implements AddonRepoPersistence {

	private final File repoFile;
	
	public AddonRepoPersistenceImpl(String pathToRepoFile) {
		this.repoFile = new File(pathToRepoFile);
	}
	
	@Override
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
			//TODO better error handling
			return Collections.emptyList();
		}
	}
	
	@Override
	public synchronized void saveInstalledAddons(Collection<Addon> addons) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(repoFile, addons);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return repoFile.toString();
	}
}
