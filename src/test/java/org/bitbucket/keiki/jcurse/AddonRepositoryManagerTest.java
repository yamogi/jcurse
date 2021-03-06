package org.bitbucket.keiki.jcurse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.bitbucket.keiki.jcurse.data.Addon;
import org.bitbucket.keiki.jcurse.data.ReleaseStatus;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;

public class AddonRepositoryManagerTest {
    
    public final StandardOutputStreamLog log = new StandardOutputStreamLog();
    
    @Test
    public void testAddOne() {
        AddonInstallationManager manager = new AddonInstallationManager(new AddonRepoPersistenceMock(true),
                new AddonFileHandlerMock());
        manager.add(Arrays.asList("buxtehude"), ReleaseStatus.RELEASE);
        Collection<Addon> addons = manager.getAddons();
        assertEquals(3, addons.size());
        Iterator<Addon> iterator = addons.iterator();
        Addon addon1 = iterator.next();
        assertEquals("buxtehude", addon1.getAddonNameId());
    }
    
    @Test
    public void testAddUnknown() {
        AddonInstallationManager manager = new AddonInstallationManager(new AddonRepoPersistenceMock(true),
                new AddonFileHandlerMock());
        manager.add(Arrays.asList("unknownAddon", "buxtehude"), ReleaseStatus.RELEASE);
        Collection<Addon> addons = manager.getAddons();
        assertEquals(3, addons.size());
        Iterator<Addon> iterator = addons.iterator();
        Addon addon1 = iterator.next();
        assertEquals("buxtehude", addon1.getAddonNameId());
    }
    
    @Test
    public void testAddUnknownAddon() {
        AddonInstallationManager manager = new AddonInstallationManager(new AddonRepoPersistenceMock(true),
                new AddonFileHandlerMock());
        manager.add(Arrays.asList("buxtehude"), ReleaseStatus.RELEASE);
        Collection<Addon> addons = manager.getAddons();
        assertEquals(3, addons.size());
        Iterator<Addon> iterator = addons.iterator();
        Addon addon1 = iterator.next();
        assertEquals("buxtehude", addon1.getAddonNameId());
    }

    
    @Test
    public void testAddTwo() {
        AddonInstallationManager manager = new AddonInstallationManager(new AddonRepoPersistenceMock(true),
                new AddonFileHandlerMock());
        String expected1 = "bus1";
        String expected2 = "bus2";
        manager.add(Arrays.asList(expected1, expected2), ReleaseStatus.RELEASE);
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
        AddonInstallationManager manager = new AddonInstallationManager(new AddonRepoPersistenceMock(true),
                new AddonFileHandlerMock());
        String string = new String("test1");
        manager.add(Arrays.asList(string), ReleaseStatus.RELEASE);
        Iterator<Addon> iterator = manager.getAddons().iterator();
        Addon next = iterator.next();
        assertNotSame(string, next.getAddonNameId());
    }
    
    @Test
    public void testRemove() {
        AddonInstallationManager manager = new AddonInstallationManager(new AddonRepoPersistenceMock(true),
                new AddonFileHandlerMock());
        manager.remove(Arrays.asList("test2"));
        Collection<Addon> addons = manager.getAddons();
        assertEquals(1, addons.size());
        Iterator<Addon> iterator = addons.iterator();
        Addon next = iterator.next();
        assertEquals("test1",next.getAddonNameId());
    }
    
    @Test
    public void testRemoveNotExisting() {
        AddonInstallationManager manager = new AddonInstallationManager(new AddonRepoPersistenceMock(true),
                new AddonFileHandlerMock());
        manager.remove(Arrays.asList("test3"));
        assertEquals(2, manager.getAddons().size());
    }
    
    @Test
    public void testUpdateAll() {
        
        AddonInstallationManager manager = new AddonInstallationManager(new AddonRepoPersistenceMock(true),
                new AddonFileHandlerMock());
        manager.updateAll(false);
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
        AddonInstallationManager manager = new AddonInstallationManager(new AddonRepoPersistenceMock(true),
                new AddonFileHandlerMock());
        manager.update(Arrays.asList("test1"), false);
        Iterator<Addon> iterator = manager.getAddons().iterator();
        checkAddonZipFile(iterator, "test1-1.0.zip");
        checkAddonZipFile(iterator, "test2-1.054.zip");
        
        manager.update(Arrays.asList("test2"), false);
        
        iterator = manager.getAddons().iterator();
        checkAddonZipFile(iterator, "test1-1.0.zip");
        checkAddonZipFile(iterator, "test2-1.0.zip");
        
    }

}
