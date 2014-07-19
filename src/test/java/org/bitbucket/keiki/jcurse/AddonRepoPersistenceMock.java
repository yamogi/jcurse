package org.bitbucket.keiki.jcurse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bitbucket.keiki.jcurse.data.Addon;

public class AddonRepoPersistenceMock implements AddonRepoPersistence {

    private final List<Addon> list = new ArrayList<>();
    
    private void addTestData() {
        List<Addon> addons = Addon.newInstance(Arrays.asList("test1", "test2"));
        addons.get(0).setLastZipFileName("test1-1.0.zip");
        addons.get(0).setVersionId(123456);
        addons.get(1).setLastZipFileName("test2-1.054.zip");
        addons.get(1).setVersionId(1054);
        
        list.addAll(addons);
    }
    
    public AddonRepoPersistenceMock(boolean b) {
        if (b) {
            addTestData();
        }
    }

    @Override
    public Collection<Addon> loadInstalledAddons() {
        return list;
    }

    @Override
    public void saveInstalledAddons(Collection<Addon> addons) {
        list.clear();
        list.addAll(addons);
    }

}
