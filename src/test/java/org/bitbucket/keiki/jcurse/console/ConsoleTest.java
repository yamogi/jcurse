package org.bitbucket.keiki.jcurse.console;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.bitbucket.keiki.jcurse.AddonFileHandlerMock;
import org.bitbucket.keiki.jcurse.AddonRepoPersistenceMock;
import org.bitbucket.keiki.jcurse.AddonRepositoryManager;
import org.bitbucket.keiki.jcurse.BusinessException;
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
    public void testFewArgumentsExceptionCatched() {
        Console.main(new String[0]);
    }

    @Test
    public void testAdd() {
        AddonRepositoryManager repositoryManager = new AddonRepositoryManager(new AddonRepoPersistenceMock(true),
                new AddonFileHandlerMock());
        Console.executeArguments(Arrays.asList("add","bagnon"), repositoryManager);
        assertEquals("added [bagnon]" + System.lineSeparator(), log.getLog());
    }
    
    @Test
    public void testRemove() {
        AddonRepositoryManager repositoryManager = new AddonRepositoryManager(new AddonRepoPersistenceMock(true),
                new AddonFileHandlerMock());
        Console.executeArguments(Arrays.asList("remove","test1"), repositoryManager);
        assertEquals("removed [test1]" + System.lineSeparator(), log.getLog());
    }
    
    @Test
    public void testUpdateAll() {
        AddonRepositoryManager repositoryManager = new AddonRepositoryManager(new AddonRepoPersistenceMock(true),
                new AddonFileHandlerMock());
        Console.executeArguments(Arrays.asList("update","all"), repositoryManager);
        assertEquals("updating all addons" + System.lineSeparator() +
                "test1 already up2date" + System.lineSeparator() + 
                "updating test2" + System.lineSeparator() + 
                "updated test2" + System.lineSeparator() + 
                "done updating" + System.lineSeparator()
                , log.getLog());
    }
    
    @Test
    public void testUpdateSingle() {
        AddonRepositoryManager repositoryManager = new AddonRepositoryManager(new AddonRepoPersistenceMock(true),
                new AddonFileHandlerMock());
        Console.executeArguments(Arrays.asList("update","test1"), repositoryManager);
        assertEquals("updating [test1]" + System.lineSeparator() +
                "test1 already up2date" + System.lineSeparator() +
                "done updating" + System.lineSeparator()
                , log.getLog());
    }
    
    @Test
    public void testList() {
        AddonRepositoryManager repositoryManager = new AddonRepositoryManager(new AddonRepoPersistenceMock(true),
                new AddonFileHandlerMock());
        Console.executeArguments(Arrays.asList("list"), repositoryManager);
        assertEquals("Currently installed addons:" + System.lineSeparator() +
                "test1, version test1-1.0.zip" + System.lineSeparator() +
                "test2, version test2-1.054.zip" + System.lineSeparator(), log.getLog());
    }
    
    @Test
    public void testListNoAddon() {
        AddonRepositoryManager repositoryManager = new AddonRepositoryManager(new AddonRepoPersistenceMock(false),
                new AddonFileHandlerMock());
        Console.executeArguments(Arrays.asList("list"), repositoryManager);
        assertEquals("We don't know of any installed addon." + System.lineSeparator(), log.getLog());
    }
    
    @Test
    public void testExport() {
        AddonRepositoryManager repositoryManager = new AddonRepositoryManager(new AddonRepoPersistenceMock(true),
                new AddonFileHandlerMock());
        Console.executeArguments(Arrays.asList("export"), repositoryManager);
        assertEquals("jcurse add test1 test2" + System.lineSeparator(), log.getLog());
    }
    
    @Test
    public void testExportNoAddon() {
        AddonRepositoryManager repositoryManager = new AddonRepositoryManager(new AddonRepoPersistenceMock(false),
                new AddonFileHandlerMock());
        Console.executeArguments(Arrays.asList("export"), repositoryManager);
        assertEquals("No addon(s) are installed" + System.lineSeparator(), log.getLog());
    }
    
    @Test (expected = BusinessException.class)
    public void unknownCommandOneArg() {
        AddonRepositoryManager repositoryManager = new AddonRepositoryManager(new AddonRepoPersistenceMock(false),
                new AddonFileHandlerMock());
        Console.executeArguments(Arrays.asList("lis"), repositoryManager);
    }
    
    @Test (expected = BusinessException.class)
    public void unknownCommandTwoArg() {
        AddonRepositoryManager repositoryManager = new AddonRepositoryManager(new AddonRepoPersistenceMock(false),
                new AddonFileHandlerMock());
        Console.executeArguments(Arrays.asList("ad", "two"), repositoryManager);
    }
}
