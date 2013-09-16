package org.bitbucket.keiki.jcurse.console;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.bitbucket.keiki.jcurse.AddonFileHandlerMock;
import org.bitbucket.keiki.jcurse.AddonRepoPersistenceMock;
import org.bitbucket.keiki.jcurse.AddonRepositoryManager;
import org.bitbucket.keiki.jcurse.ErrorCode;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.StandardErrorStreamLog;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;

public class ConsoleTest {

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();
    
    @Rule
    public final StandardErrorStreamLog err = new StandardErrorStreamLog();
    
    @Rule
    public final StandardOutputStreamLog log = new StandardOutputStreamLog();
    
    @Test
    public void testFewArguments() {
        exit.expectSystemExitWithStatus(ErrorCode.CONSOLE_ARGUMENTS_NUMBER.getErrorCode());
        Console.main();
        assertEquals("Error: Number of arguments are wrong.", err.getLog());
    }

    @Test
    public void testAdd() {
        AddonRepositoryManager repositoryManager = new AddonRepositoryManager(new AddonRepoPersistenceMock(),
                new AddonFileHandlerMock());
        Console.executeArguments(Arrays.asList("add","bagnon"), repositoryManager);
        assertEquals("added [bagnon]" + System.lineSeparator(), log.getLog());
    }
    
    @Test
    public void testRemove() {
        AddonRepositoryManager repositoryManager = new AddonRepositoryManager(new AddonRepoPersistenceMock(),
                new AddonFileHandlerMock());
        Console.executeArguments(Arrays.asList("remove","test1"), repositoryManager);
        assertEquals("removed [test1]" + System.lineSeparator(), log.getLog());
    }
    
    @Test
    public void testUpdateAll() {
        AddonRepositoryManager repositoryManager = new AddonRepositoryManager(new AddonRepoPersistenceMock(),
                new AddonFileHandlerMock());
        Console.executeArguments(Arrays.asList("update","all"), repositoryManager);
        assertEquals("updating all addons" + System.lineSeparator() +
                "test1 already up2date" + System.lineSeparator() + 
                "updating test2" + System.lineSeparator() + 
                "updated test2" + System.lineSeparator() + 
                "done updating all addons" + System.lineSeparator() + 
                "updated all addons" + System.lineSeparator() 
                , log.getLog());
    }
    
    @Test
    public void testUpdateSingle() {
        AddonRepositoryManager repositoryManager = new AddonRepositoryManager(new AddonRepoPersistenceMock(),
                new AddonFileHandlerMock());
        Console.executeArguments(Arrays.asList("update","test1"), repositoryManager);
        assertEquals("updating [test1]" + System.lineSeparator() +
                "test1 already up2date" + System.lineSeparator() +
                "done updating [test1]" + System.lineSeparator() +
                "updated [test1]" + System.lineSeparator()
                , log.getLog());
    }
    
    @Test
    public void testList() {
        AddonRepositoryManager repositoryManager = new AddonRepositoryManager(new AddonRepoPersistenceMock(),
                new AddonFileHandlerMock());
        Console.executeArguments(Arrays.asList("list"), repositoryManager);
        assertEquals("Currently installed addons:" + System.lineSeparator() +
                "Addon 'test1', version 'test1-1.0.zip'" + System.lineSeparator() +
                "Addon 'test2', version 'test2-1.054.zip'" + System.lineSeparator(), log.getLog());
    }
}
