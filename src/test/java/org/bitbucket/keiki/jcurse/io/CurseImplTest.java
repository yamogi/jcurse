package org.bitbucket.keiki.jcurse.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.bitbucket.keiki.jcurse.data.Addon;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class CurseImplTest {

    @Rule
    public TemporaryFolder addonFolder = new TemporaryFolder();
    
    @Test
    public void downloadToWoWEmptyList() {
        CurseImpl curse = new CurseImpl(addonFolder.getRoot().getAbsolutePath(), "http://localhost");
        curse.downloadToWow(Collections.<Addon>emptyList());
    }
    
    @Test
    public void downloadToWoW() {
        CurseImpl curse = new CurseImplTestable(addonFolder.getRoot().getAbsolutePath(), "http://localhost/");
        curse.downloadToWow(Addon.newInstance(Arrays.asList("bagnon")));
    }
    
    private static class CurseImplTestable extends CurseImpl{

        public CurseImplTestable(String addonFolderName, String curseBaseUrl) {
            super(addonFolderName, curseBaseUrl);
        }
        
        @Override
        protected int executeHttpMethod(HttpClient httpClient, GetMethod method)
                throws IOException, HttpException {
            return 200;
        }
        
        @Override
        protected InputStream getDownloadStream(GetMethod method)
                throws IOException {
            return this.getClass().getResourceAsStream("/websites/123/456/bagnon/download");
        }
        
        @Override
        protected Set<String> downloadAndExtract(String downloadUrl) {
            Set<String> folderNamesMock = new HashSet<String>();
            folderNamesMock.add("bagnon");
            return folderNamesMock;
        }        
    }
    
}
