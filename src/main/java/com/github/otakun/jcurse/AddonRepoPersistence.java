package com.github.otakun.jcurse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;


public final class AddonRepoPersistence {

	private static final Path PATH_REPO_FILE = Paths.get(Configuration.CONFIG_PATH + "repository");
	
	public synchronized static Collection<Addon> loadInstalledAddons() {
		try {
			if (!Files.exists(PATH_REPO_FILE)) {
				return Collections.emptyList();
			}
			ObjectMapper mapper = new ObjectMapper();
			Object readValue = mapper.readValue(PATH_REPO_FILE.toFile(), new TypeReference<Collection<Addon>>() {});
			@SuppressWarnings("unchecked")
			Collection<Addon> result = (Collection<Addon>) readValue; 
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public synchronized static void saveInstalledAddons(Collection<Addon> addons) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(PATH_REPO_FILE.toFile(), addons);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
