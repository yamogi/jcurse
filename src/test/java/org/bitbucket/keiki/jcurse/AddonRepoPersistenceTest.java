package org.bitbucket.keiki.jcurse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class AddonRepoPersistenceTest {
	
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
	@Test
	public void testReadWriteJsonFile() throws IOException {
		AddonRepoPersistence persistence = new AddonRepoPersistenceImpl(folder.newFile().getAbsolutePath());
		List<Addon> addons = Addon.newInstance(Arrays.asList("test1","test2","test3"));
		persistence.saveInstalledAddons(addons);
		
		Collection<Addon> loadInstalledAddons = persistence.loadInstalledAddons();
		Iterator<Addon> iterator = loadInstalledAddons.iterator();
		assertEquals("test1", iterator.next().getAddonNameId());
		assertEquals("test2", iterator.next().getAddonNameId());
		assertEquals("test3", iterator.next().getAddonNameId());
	}
	
	@Test
	public void testReadWriteJsonFileEmpty() throws IOException {
		AddonRepoPersistence persistence = new AddonRepoPersistenceImpl(folder.newFile().getAbsolutePath());
		persistence.saveInstalledAddons(new ArrayList<Addon>());
		
		Collection<Addon> loadInstalledAddons = persistence.loadInstalledAddons();
		assertTrue(loadInstalledAddons.isEmpty());
	}

	
	@Test
	public void testWriteJsonFileNull() throws IOException {
		AddonRepoPersistence persistence = new AddonRepoPersistenceImpl(folder.newFile().getAbsolutePath());
		persistence.saveInstalledAddons(null);
	}
	
	@Test
	public void testReadJsonFileNull() throws IOException {
		AddonRepoPersistence persistence = new AddonRepoPersistenceImpl(folder.newFile().getAbsolutePath());
		persistence.saveInstalledAddons(null);
		Collection<Addon> loadInstalledAddons = persistence.loadInstalledAddons();
		assertNull(loadInstalledAddons);
	}
	
	@Test 
	public void testReadJsonFileNotExisting() throws IOException    {
		AddonRepoPersistence persistence = new AddonRepoPersistenceImpl(folder.newFile().getAbsolutePath());
		Collection<Addon> loadInstalledAddons = persistence.loadInstalledAddons();
		
		assertTrue(loadInstalledAddons.isEmpty());
	}
}
