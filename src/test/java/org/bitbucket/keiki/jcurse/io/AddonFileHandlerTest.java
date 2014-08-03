package org.bitbucket.keiki.jcurse.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class AddonFileHandlerTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Test
    public void succesfullDownloadCompress() throws IOException {
        AddonFileHandler addonFileHandler = new AddonFileHandler(folder.getRoot().getAbsolutePath());
        Set<String> download = addonFileHandler.download(this.getClass().getResourceAsStream("Bagnon_5.3.6.zip"));
        assertEquals("[BagBrother, Bagnon_Config, Bagnon_GuildBank, Bagnon, Bagnon_VoidStorage]", download.toString());
    }
    
    
}
