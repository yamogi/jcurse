package com.github.otakun.jcurse;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class AddonRepoPersistenceTest {
	
	@Test
	public void testWriteJsonFileNull() throws IOException {
		File file = File.createTempFile("jcurse", "jcurse");
		AddonRepoPersistence persistence = new AddonRepoPersistence(file.getAbsolutePath());
//		persistence.saveInstalledAddons(addons);
//		AddonRepoPersistence.saveInstalledAddons(null);
	}
	
	@Test
	public void testWriteJsonFileEmpty(){
//		AddonRepoPersistence.saveInstalledAddons(new ArrayList<Addon>());
	}

}
