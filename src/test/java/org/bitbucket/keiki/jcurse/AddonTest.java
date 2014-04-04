package org.bitbucket.keiki.jcurse;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AddonTest {
    
    @Test
    public void testCreateNewInstance() {
        List<Addon> addons = Addon.newInstance(Arrays.asList("myAddonOne", "thisIsTwo"));
        assertEquals(2, addons.size());
        Addon addon1 = addons.get(0);
        checkNewAddon(addon1, "myAddonOne");
        Addon addon2 = addons.get(1);
        checkNewAddon(addon2, "thisIsTwo");
    }

    private void checkNewAddon(Addon addon, String idName) {
        assertEquals(idName, addon.getAddonNameId());
        assertNull(addon.getFolders());
    }
    
    @Test
    public void testEmptyStringNewInstance() {
        assertEquals("", Addon.newInstance(Arrays.asList("")).get(0).getAddonNameId());
    }
    
    @Test (expected = NullPointerException.class)
    public void testNullNewInstance() {
        Addon.newInstance(null);
    }

    @Test
    public void testNullListNewInstance() {
        List<String> listToTest = new ArrayList<>();
        listToTest.add(null);
        List<Addon> newInstance = Addon.newInstance(listToTest);
        assertEquals(1, newInstance.size());
    }
        
    
    
}
