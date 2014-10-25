package org.bitbucket.keiki.jcurse.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.NoSuchElementException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.bitbucket.keiki.jcurse.data.Addon;
import org.bitbucket.keiki.jcurse.data.ReleaseStatus;
import org.junit.Test;

public class CurseForgeTest {
    
    private Addon addon = Addon.newInstance(Arrays.asList("dunesimplebuffs"), ReleaseStatus.BETA).get(0);

    @Test
    public void successfulGetDownloadUrl() {
        CurseForge curseForge = new CurseForgeTestable();
        
        String downloadUrl = curseForge.getDownloadUrl(addon);
        
        assertEquals("http://www.curseforge.com/media/files/797/148/DuneSimpleBuffs.zip", downloadUrl);
    }
    
    @Test (expected = NoSuchElementException.class)
    public void getDownloadUrlBeta() {
        CurseForge curseForge = new CurseForgeTestableMissingLine();
        curseForge.getDownloadUrl(addon);
    }
    
    private class CurseForgeTestable extends CurseForge {
        @Override
        protected InputStream getStreamOverviewSite(GetMethod method) throws IOException {
            return this.getClass().getResourceAsStream("curseForgeOverview.html");
        }

        @Override
        protected InputStream getStreamDetailSite(GetMethod method) throws IOException {
            return this.getClass().getResourceAsStream("curseForgeDetail.html");
        }
        
        @Override
        protected int executeHttp(HttpClient httpClient, GetMethod method) throws IOException, HttpException {
            return 200;
        }
    }
    
    private class CurseForgeTestableMissingLine extends CurseForgeTestable {
        @Override
        protected InputStream getStreamOverviewSite(GetMethod method) throws IOException {
            return this.getClass().getResourceAsStream("curseForgeOverview2ndLineMissing.html");
        }
    }
}
