package com.github.otakun.jcurse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;

public final class Configuration {
	
	static final String CONFIG_PATH = System.getProperty("user.home") + File.separator 
															+ ".jcurse" + File.separator;

	public static final Charset CHARSET = Charset.forName("ASCII");
	
	private static final String CONFIG_FILE_LOCATION = CONFIG_PATH + "config";
	
	private static final String WOW_FOLDER_KEY = "wow.folder"; 
	
	private static Configuration INSTANCE = new Configuration();
	
	private String wowFolder;
	
	private static boolean toInitialize = true;
	
	private Configuration() {
	}
	
	public synchronized static Configuration getConfiguration() {
		if (toInitialize) {
			loadProperties();
			toInitialize = false;
		}
		return INSTANCE;
	}
	
	private static void loadProperties() {
		File propertyFile = new File(CONFIG_FILE_LOCATION);
		Properties properties = new Properties();
		if (!propertyFile.exists()) {
			properties.put(WOW_FOLDER_KEY, "");
			try (FileOutputStream fos = new FileOutputStream(propertyFile)) {
				properties.store(fos, null);
			} catch (IOException e) {
				//TODO error handling
				e.printStackTrace();
			}
		} else {
			try (FileInputStream fis = new FileInputStream(propertyFile)) {
				properties.load(fis);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		INSTANCE.wowFolder = properties.getProperty(WOW_FOLDER_KEY);
	}

	public String getWowFolder() {
		if (wowFolder.isEmpty()) {
			throw new RuntimeException("No folder for WoW given in config file (home/.jcurse/config)");
			//XXX better error handling
		}
		return wowFolder;
	}
}
