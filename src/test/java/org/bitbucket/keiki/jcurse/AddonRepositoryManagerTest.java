package org.bitbucket.keiki.jcurse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

public class AddonRepositoryManagerTest {
	
	@Test
	public void testAddOne() {
		AddonRepositoryManager manager = new AddonRepositoryManager(new AddonRepoPersistenceMock(),
				new AddonFileHandlerMock());
		manager.add(Arrays.asList("buxtehude"));
		Collection<Addon> addons = manager.getAddons();
		assertEquals(3, addons.size());
		Iterator<Addon> iterator = addons.iterator();
		Addon addon1 = iterator.next();
		assertEquals("buxtehude", addon1.getAddonNameId());
	}
	
	@Test
	public void testAddTwo() {
		AddonRepositoryManager manager = new AddonRepositoryManager(new AddonRepoPersistenceMock(),
				new AddonFileHandlerMock());
		String expected1 = "bus1";
		String expected2 = "bus2";
		manager.add(Arrays.asList(expected1, expected2));
		Collection<Addon> addons = manager.getAddons();
		assertEquals(4, addons.size());
		Iterator<Addon> iterator = addons.iterator();
		Addon addon1 = iterator.next();
		assertSame(expected1, addon1.getAddonNameId());
		Addon addon2 = iterator.next();
		assertSame(expected2, addon2.getAddonNameId());
	}
	
	@Test
	public void testAlreadyExisting() {
		AddonRepositoryManager manager = new AddonRepositoryManager(new AddonRepoPersistenceMock(),
				new AddonFileHandlerMock());
		String string = new String("test1");
		manager.add(Arrays.asList(string));
		Iterator<Addon> iterator = manager.getAddons().iterator();
		Addon next = iterator.next();
		assertNotSame(string, next.getAddonNameId());
	}
	
	@Test
	public void testRemove() {
		AddonRepositoryManager manager = new AddonRepositoryManager(new AddonRepoPersistenceMock(),
				new AddonFileHandlerMock());
		manager.remove(Arrays.asList("test2"));
		Collection<Addon> addons = manager.getAddons();
		assertEquals(1, addons.size());
		Iterator<Addon> iterator = addons.iterator();
		Addon next = iterator.next();
		assertEquals("test1",next.getAddonNameId());
	}
	
	@Test (expected = RuntimeException.class)
	public void testRemoveNotExisting() {
		AddonRepositoryManager manager = new AddonRepositoryManager(new AddonRepoPersistenceMock(),
				new AddonFileHandlerMock());
		manager.remove(Arrays.asList("test3"));
	}
	
	@Test
	public void testUpdateAll() {
		AddonRepositoryManager manager = new AddonRepositoryManager(new AddonRepoPersistenceMock(),
				new AddonFileHandlerMock());
		manager.updateAll();
		Collection<Addon> addons = manager.getAddons();
		assertEquals(2, addons.size());
		
		Iterator<Addon> iterator = addons.iterator();
		checkAddonZipFile(iterator, "test1-1.0.zip");
		checkAddonZipFile(iterator, "test2-1.0.zip");
	}

	private void checkAddonZipFile(Iterator<Addon> iterator, String toCheck) {
		Addon addon1 = iterator.next();
		assertEquals(toCheck, addon1.getLastZipFileName());
	}
	
	@Test
	public void testUpdateSingle() {
		AddonRepositoryManager manager = new AddonRepositoryManager(new AddonRepoPersistenceMock(),
				new AddonFileHandlerMock());
		manager.update(Arrays.asList("test1"));
		Iterator<Addon> iterator = manager.getAddons().iterator();
		checkAddonZipFile(iterator, "test1-1.0.zip");
		checkAddonZipFile(iterator, "test2-1.054.zip");
		
		manager.update(Arrays.asList("test2"));
		
		iterator = manager.getAddons().iterator();
		checkAddonZipFile(iterator, "test1-1.0.zip");
		checkAddonZipFile(iterator, "test2-1.0.zip");
		
	}

}
