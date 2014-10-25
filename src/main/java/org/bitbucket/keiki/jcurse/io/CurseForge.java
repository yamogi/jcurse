package org.bitbucket.keiki.jcurse.io;
import static org.bitbucket.keiki.jcurse.io.Constants.CHARSET_WEBSITE;
import static org.bitbucket.keiki.jcurse.io.Constants.USER_AGENT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.bitbucket.keiki.jcurse.data.Addon;
import org.bitbucket.keiki.jcurse.data.BusinessException;
import org.bitbucket.keiki.jcurse.data.ReleaseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurseForge {
    
    private static final String LOG_CAN_T_ACCESS = "Can't access ";
    private static final String LOG_HTTP_STATE_CODE = "state {}";
    private static final String HTTP_HEADER_KEY_USER_AGENT = "user-agent";
    private static final String LOG_ACCESSING_MSG = "accessing {}";
    private static final String FILE_TYPE_ATTRIBUTE = "file-type-";
    private static final Logger LOG = LoggerFactory.getLogger(CurseForge.class);
    
    public String getDownloadUrl(Addon addon) {
        String url = "http://wow.curseforge.com/addons/" + addon.getAddonNameId() + "/files/";
        try {
            LOG.debug(LOG_ACCESSING_MSG, url);
            HttpClient httpClient = new HttpClient();
            GetMethod method = new GetMethod(url);
            method.setRequestHeader(HTTP_HEADER_KEY_USER_AGENT, USER_AGENT);
            int status = executeHttp(httpClient, method);
            LOG.debug(LOG_HTTP_STATE_CODE, status);
            String detailSiteUrl = "";
            try (BufferedReader reader = new BufferedReader
                    (new InputStreamReader(getStreamOverviewSite(method), CHARSET_WEBSITE))) {
                detailSiteUrl = readLines(addon, reader);
            }
            if (detailSiteUrl.isEmpty()) {
                throw new NoSuchElementException("detail site couldn't be found");
            }
            LOG.debug(detailSiteUrl);
            return detailSiteUrl;
        } catch (IOException e) {
            throw new BusinessException(LOG_CAN_T_ACCESS + url, e);
        }
    }

    protected String readLines(Addon addon, BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            int indexOf = line.indexOf("col-file\"><a href");
            if (indexOf >= 0) {
                String nextLine = reader.readLine();
                if (nextLine == null) {
                    return "";
                }
                int indexOf2 = nextLine.indexOf(FILE_TYPE_ATTRIBUTE);
                char statusChar = nextLine.charAt(indexOf2 + FILE_TYPE_ATTRIBUTE.length());
                ReleaseStatus releaseStatus = ReleaseStatus.valueOf(statusChar);
                if (addon.getReleaseStatus().ordinal() <= releaseStatus.ordinal()) {
                    String parseAttribute = WebsiteHelper.parseAttribute(line, "href");
                    return getDownloadUrlOfDetailSite(parseAttribute);
                }
            }
        }
        return "";
    }

    protected int executeHttp(HttpClient httpClient, GetMethod method) throws IOException {
        return httpClient.executeMethod(method);
    }

    protected InputStream getStreamOverviewSite(GetMethod method) throws IOException {
        return method.getResponseBodyAsStream();
    }
    
    protected InputStream getStreamDetailSite(GetMethod method) throws IOException {
        return method.getResponseBodyAsStream();
    }

    private String getDownloadUrlOfDetailSite(String detailSite) {
        String url = "http://wow.curseforge.com"+ detailSite;
        try {
            LOG.debug(LOG_ACCESSING_MSG, url);
            HttpClient httpClient = new HttpClient();
            GetMethod method = new GetMethod(url);
            method.setRequestHeader(HTTP_HEADER_KEY_USER_AGENT, USER_AGENT);
            int status = executeHttp(httpClient, method);
            LOG.debug(LOG_HTTP_STATE_CODE, status);
            String downloadUrl = "";
            downloadUrl = extractDownloadUrlFromStream(downloadUrl, method);
            if (downloadUrl.isEmpty()) {
                throw new NoSuchElementException("Addon download url couldn't be found");
            }
            return downloadUrl;
        } catch (IOException e) {
            throw new BusinessException(LOG_CAN_T_ACCESS + url, e);
        }
    }

    protected String extractDownloadUrlFromStream(String downloadUrl, GetMethod method) throws IOException {
        try (BufferedReader reader = new BufferedReader
                (new InputStreamReader(getStreamDetailSite(method), CHARSET_WEBSITE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("user-action-download")) {
                    downloadUrl = WebsiteHelper.parseAttribute(line, "href");
                }
            }
        }
        return downloadUrl;
    }

}
