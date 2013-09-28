package org.bitbucket.keiki.jcurse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class CurseAddonFileHandlerTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private static final String URL_PLACEHOLDER = "#BASE_URL_TO_REPLACE#";
    private static String baseUrl = null;
    private static String fileUrl = null;

    @BeforeClass
    public static void beforeClass() throws IOException {
        baseUrl = Thread.currentThread().getContextClassLoader().
                getResource("websites/").toString();
        Configuration.getConfiguration().setCurseBaseUrl(baseUrl);

        fileUrl = baseUrl + File.separator + "bagnon" + File.separator + "download";
        String substring = fileUrl.substring(5);

        File file = new File(substring);
        String content = new String(FileUtils.readFileToString(file));
        String replaceOnce = StringUtils.replaceOnce(content, URL_PLACEHOLDER, baseUrl);
        FileUtils.write(file, replaceOnce);
    }

    
    @AfterClass
    public static void afterClass() throws IOException {
        String substring = fileUrl.substring(5);

        File file = new File(substring);
        String content = new String(FileUtils.readFileToString(file));
        String replaceOnce = StringUtils.replaceOnce(content, baseUrl, URL_PLACEHOLDER);
        FileUtils.write(file, replaceOnce);
    }

    @Before
    public void before() {
        File root = folder.getRoot();
        Configuration.getConfiguration().setWowFolder(root.getAbsolutePath());
    }
    
    @Test
    public void testDownloadToWoW() {
        CurseAddonFileHandler fileHandler = new CurseAddonFileHandler();
        List<Addon> addons = Addon.newInstance(Arrays.asList("bagnon"));
        
        fileHandler.downloadToWow(addons);

        String rootPath = folder.getRoot().getAbsolutePath();
        String addonPath = rootPath + File.separator + "Interface" + File.separator + "AddOns";
        File addonRoot = new File(addonPath);
        File[] listFiles = addonRoot.listFiles();
        assertEquals(5, listFiles.length);
    }
    
    @Test
    public void testDownloadToWoWAddonNotFound() {
        CurseAddonFileHandler fileHandler = new CurseAddonFileHandler();
        List<Addon> addons = Addon.newInstance(Arrays.asList("bagno"));
        
        fileHandler.downloadToWow(addons);

        String rootPath = folder.getRoot().getAbsolutePath();
        String addonPath = rootPath + File.separator + "Interface" + File.separator + "AddOns";
        File addonRoot = new File(addonPath);
        assertFalse(addonRoot.exists());
    }
    
//    @Test
//    public void testDownloadToWoWAddonNotFoundUrl() {
//        CurseAddonFileHandler fileHandler = new CurseAddonFileHandler();
//        List<Addon> addons = Addon.newInstance(Arrays.asList("bagno"));
//        
//        fileHandler.downloadToWow(addons.get(0), "");
//
//        String rootPath = folder.getRoot().getAbsolutePath();
//        String addonPath = rootPath + File.separator + "Interface" + File.separator + "AddOns";
//        File addonRoot = new File(addonPath);
//        assertFalse(addonRoot.exists());
//    }
    
    @Test
    public void testGetCompressedFileName() {
        CurseAddonFileHandler fileHandler = new CurseAddonFileHandler();
        String fileName = fileHandler.getCompressedFileName("bagnon");
        assertEquals("Bagnon_5.3.6.zip", fileName);
    }
    
    @Test
    public void testRemoveFolders() throws IOException {
        File root = folder.getRoot();
        String addonPath = root.getAbsolutePath() + File.separator + "Interface" + File.separator + "AddOns";
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
        
        CurseAddonFileHandler fileHandler = new CurseAddonFileHandler();
        fileHandler.removeAddons(addons);
        
        assertEquals(0, new File(addonPath).listFiles().length);
    }

}
