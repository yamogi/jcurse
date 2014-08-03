package org.bitbucket.keiki.jcurse.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.bitbucket.keiki.jcurse.data.Addon;
import org.bitbucket.keiki.jcurse.data.ReleaseStatus;
import org.junit.Test;

public class CurseForgeTest {
    
    @Test
    public void getDownloadUrlBeta() {
        CurseForgeTestable curseForgeTestable = new CurseForgeTestable();
        List<Addon> addon = Addon.newInstance(Arrays.asList("dunesimplebuffs"), ReleaseStatus.BETA);
        
        String downloadUrl = curseForgeTestable.getDownloadUrl(addon.get(0));
        
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
}
