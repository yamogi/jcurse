package org.bitbucket.keiki.jcurse.io;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class AddonFileHandlerTest {

    private static final String EXPECTED_DIRECTORY_NAMES = "[DuneSimpleBuffs]";
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private InputStream testAddonFileStream;
    
    @Before
    public void beforeClass() {
        testAddonFileStream = this.getClass().getResourceAsStream("DuneSimpleBuffs.zip");
    }
    
    
    @Test
    public void successfulDownloadCompress() throws IOException {
        AddonFileHandler addonFileHandler = new AddonFileHandler(folder.getRoot().getAbsolutePath());
        Set<String> download = addonFileHandler.download(testAddonFileStream);
        assertEquals(EXPECTED_DIRECTORY_NAMES, download.toString());
    }
    
    @Test
    public void successfulDownloadCompressAndCreateDirectories() throws IOException {
        AddonFileHandler addonFileHandler = new AddonFileHandler(folder.getRoot().getAbsolutePath() + File.separator + 
                "createThis" + File.separator + "andThat");
        Set<String> download = addonFileHandler.download(testAddonFileStream);
        assertEquals(EXPECTED_DIRECTORY_NAMES, download.toString());
    }
    
    @Test
    public void removeAddonFolders() throws IOException {
        File newFolder = folder.newFolder();
        String pathnameWithSlash = newFolder.getAbsolutePath() + File.separator;
        File file1 = createTestFile(pathnameWithSlash, "test1", "file1");
        File file2 = createTestFile(pathnameWithSlash, "test2", "file1");
        File file3 = createTestFile(pathnameWithSlash, "test2", "file2");
        AddonFileHandler addonFileHandler = new AddonFileHandler(newFolder.getAbsolutePath() + File.separator);
        
        addonFileHandler.removeAddonFolders(Arrays.asList("test1", "test2"));
        
        assertFalse(file1.isFile());
        assertFalse(file2.isFile());
        assertFalse(file3.isFile());
    }


    protected File createTestFile(String pathnameWithSlash, String folderName, String fileName) throws IOException {
        File file1 = new File(pathnameWithSlash + folderName + File.separator + fileName);
        file1.getParentFile().mkdirs();
        file1.createNewFile();
        return file1;
    }
}
