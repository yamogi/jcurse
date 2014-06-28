package org.bitbucket.keiki.jcurse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.bitbucket.keiki.jcurse.curse.CurseAddonFileHandler;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class CurseAddonFileHandlerTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private static final String URL_PLACEHOLDER = "#BASE_URL_TO_REPLACE#";
    private static String baseUrl = null;
    private static String fileUrl = null;
    private static String addonPath = null;

    @BeforeClass
    public static void beforeClass() throws IOException {
        baseUrl = Thread.currentThread().getContextClassLoader().
                getResource("websites/123/456/").toString();

        fileUrl = baseUrl
                + File.separator + "bagnon" + File.separator + "download";
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
        addonPath = root.getAbsolutePath() + File.separator;
    }
    
    @Test (expected = BusinessException.class)
    public void testDownloadToWoWAddonNotFoundUrl() {
        CurseAddonFileHandler.extractZipFileName("file:bagnon-54.zip");
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
        
        CurseAddonFileHandler fileHandler = new CurseAddonFileHandler(addonPath, baseUrl);
        fileHandler.removeAddons(addons);
        
        assertEquals(0, new File(addonPath).listFiles().length);
    }

}
