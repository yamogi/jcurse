package org.bitbucket.keiki.jcurse.console;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.bitbucket.keiki.jcurse.AddonFileHandlerMock;
import org.bitbucket.keiki.jcurse.AddonRepoPersistenceMock;
import org.bitbucket.keiki.jcurse.AddonInstallationManager;
import org.bitbucket.keiki.jcurse.data.BusinessException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;

public class ConsoleTest {

    @Rule
    public final StandardOutputStreamLog log = new StandardOutputStreamLog();
    
    @Test (expected = BusinessException.class)
    public void testFewArguments() {
        Console.executeArguments(Arrays.asList(new String[0]), null);
    }
    
    @Test
    public void testAdd() {
        AddonInstallationManager repositoryManager = new AddonInstallationManager(new AddonRepoPersistenceMock(true),
                new AddonFileHandlerMock());
        Console.executeCommands(Arrays.asList("add","bagnon"), repositoryManager, "add");
        assertEquals("added [bagnon]" + System.lineSeparator(), log.getLog());
    }
    
    @Test
    public void testRemove() {
        AddonInstallationManager repositoryManager = new AddonInstallationManager(new AddonRepoPersistenceMock(true),
                new AddonFileHandlerMock());
        Console.executeCommands(Arrays.asList("remove","test1"), repositoryManager, "remove");
        assertEquals("removed [test1]" + System.lineSeparator(), log.getLog());
    }
    
    @Test
    public void testUpdateAll() {
        AddonInstallationManager repositoryManager = new AddonInstallationManager(new AddonRepoPersistenceMock(true),
                new AddonFileHandlerMock());
        Console.executeCommands(Arrays.asList("update","all"), repositoryManager, "update");
        String result = log.getLog();
        assertTrue(result.startsWith("updating all addons" + System.lineSeparator()));
        //because of parallel procession we don't know what comes first
        assertTrue(result.contains("test1 already up2date" + System.lineSeparator()));
        assertTrue(result.contains("updated test2" + System.lineSeparator()));
        assertTrue(result.contains("updating test2" + System.lineSeparator())); 
        result.endsWith("all addons are now up2date" + System.lineSeparator());
    }
    
    @Test
    public void testUpdateSingle() {
        AddonInstallationManager repositoryManager = new AddonInstallationManager(new AddonRepoPersistenceMock(true),
                new AddonFileHandlerMock());
        Console.executeCommands(Arrays.asList("update","test1"), repositoryManager, "update");
        assertEquals("updating [test1]" + System.lineSeparator() +
                "test1 already up2date" + System.lineSeparator() +
                "all addons are now up2date" + System.lineSeparator()
                , log.getLog());
    }
    
    @Test
    public void testList() {
        AddonInstallationManager repositoryManager = new AddonInstallationManager(new AddonRepoPersistenceMock(true),
                new AddonFileHandlerMock());
        Console.executeCommands(Arrays.asList("list"), repositoryManager, "list");
        assertEquals("Currently installed addons:" + System.lineSeparator() +
                "test1 version test1-1.0.zip" + System.lineSeparator() +
                "test2 version test2-1.054.zip" + System.lineSeparator(), log.getLog());
    }
    
    @Test
    public void testListNoAddon() {
        AddonInstallationManager repositoryManager = new AddonInstallationManager(new AddonRepoPersistenceMock(false),
                new AddonFileHandlerMock());
        Console.executeCommands(Arrays.asList("list"), repositoryManager, "list");
        assertEquals("We don't know of any installed addon." + System.lineSeparator(), log.getLog());
    }
    
    @Test
    public void testExport() {
        AddonInstallationManager repositoryManager = new AddonInstallationManager(new AddonRepoPersistenceMock(true),
                new AddonFileHandlerMock());
        Console.executeCommands(Arrays.asList("export"), repositoryManager, "export");
        assertEquals("jcurse add test1 test2" + System.lineSeparator(), log.getLog());
    }
    
    @Test
    public void testExportNoAddon() {
        AddonInstallationManager repositoryManager = new AddonInstallationManager(new AddonRepoPersistenceMock(false),
                new AddonFileHandlerMock());
        Console.executeCommands(Arrays.asList("export"), repositoryManager, "export");
        assertEquals("No addon(s) are installed" + System.lineSeparator(), log.getLog());
    }
    
    @Test (expected = BusinessException.class)
    public void unknownCommandOneArg() {
        AddonInstallationManager repositoryManager = new AddonInstallationManager(new AddonRepoPersistenceMock(false),
                new AddonFileHandlerMock());
        Console.executeCommands(Arrays.asList("lis"), repositoryManager, "lis");
    }
    
    @Test (expected = BusinessException.class)
    public void unknownCommandTwoArg() {
        AddonInstallationManager repositoryManager = new AddonInstallationManager(new AddonRepoPersistenceMock(false),
                new AddonFileHandlerMock());
        Console.executeCommands(Arrays.asList("ad", "two"), repositoryManager, "ad");
    }
}
