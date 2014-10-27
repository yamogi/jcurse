package org.bitbucket.keiki.jcurse;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bitbucket.keiki.jcurse.data.Addon;
import org.bitbucket.keiki.jcurse.data.BusinessException;
import org.bitbucket.keiki.jcurse.io.CurseImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class CurseAddonFileHandlerTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private static String baseUrl = null;
    private static String addonPath = null;


    @Before
    public void before() {
        File root = folder.getRoot();
        addonPath = root.getAbsolutePath() + File.separator;
    }
    
    @Test (expected = BusinessException.class)
    public void testDownloadToWoWAddonNotFoundUrl() {
        CurseImpl.extractZipFileName("file:bagnon-54.zip");
    }
    
    @Test
    public void testRemoveFolders() throws IOException {
        new File(addonPath + File.separator + "test1").mkdirs();
        new File(addonPath + File.separator + "test2").mkdirs();
        new File(addonPath + File.separator + "test3").mkdirs();

        
        List<Addon> addons = Addon.newInstance(Arrays.asList("test"));
        Addon addon = addons.get(0);
        Set<String> folders = new HashSet<>();
        folders.add("test1");
        folders.add("test2");
        folders.add("test3");
        addon.setFolders(folders);
        
        CurseImpl fileHandler = new CurseImpl(addonPath, baseUrl);
        fileHandler.removeAddons(addons);
        
        assertEquals(0, new File(addonPath).listFiles().length);
    }

}
