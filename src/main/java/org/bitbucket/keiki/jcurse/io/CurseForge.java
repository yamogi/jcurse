package org.bitbucket.keiki.jcurse.io;
import static org.bitbucket.keiki.jcurse.io.Constants.*;

import java.io.BufferedReader;
import java.io.IOException;
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
	
    private static final String FILE_TYPE_ATTRIBUTE = "file-type-";
	private static final Logger LOG = LoggerFactory.getLogger(CurseForge.class);
	
	public String getDownloadUrl(Addon addon) {
		String url = "http://wow.curseforge.com/addons/" + addon.getAddonNameId() + "/files/";
        try {
            LOG.debug("accessing {}", url);
            HttpClient httpClient = new HttpClient();
            GetMethod method = new GetMethod(url);
            method.setRequestHeader("user-agent", USER_AGENT);
            int status = httpClient.executeMethod(method);
            LOG.debug("state {}", status);
            String downloadUrl = "";
            try (BufferedReader reader = new BufferedReader
                    (new InputStreamReader(method.getResponseBodyAsStream(), CHARSET_WEBSITE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                	int indexOf = line.indexOf("col-file\"><a href");
                	if (indexOf >= 0) {
                		String nextLine = reader.readLine();
                		if (nextLine == null) {
                		    break;
                		}
            			int indexOf2 = nextLine.indexOf(FILE_TYPE_ATTRIBUTE);
            			char statusChar = nextLine.charAt(indexOf2 + FILE_TYPE_ATTRIBUTE.length());
            			ReleaseStatus releaseStatus = ReleaseStatus.valueOf(statusChar);
            			if (addon.getReleaseStatus().ordinal() <= releaseStatus.ordinal()) {
            				String parseAttribute = WebsiteHelper.parseAttribute(line, "href");
            				downloadUrl = getRealDownloadUrl(parseAttribute);
            				break;
            			}
                	}
                }
            }
            if (downloadUrl.isEmpty()) {
                throw new NoSuchElementException("Addon couldn't be found");
            }
            LOG.info(downloadUrl);
            return downloadUrl;
        } catch (IOException e) {
            throw new BusinessException("Can't access " + url, e);
        }
	}

	private String getRealDownloadUrl(String parseAttribute) {
		String url = "http://wow.curseforge.com"+ parseAttribute;
        try {
            LOG.debug("accessing {}", url);
            HttpClient httpClient = new HttpClient();
            GetMethod method = new GetMethod(url);
            method.setRequestHeader("user-agent", USER_AGENT);
            int status = httpClient.executeMethod(method);
            LOG.debug("state {}", status);
            String downloadUrl = "";
            try (BufferedReader reader = new BufferedReader
                    (new InputStreamReader(method.getResponseBodyAsStream(), CHARSET_WEBSITE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                	if (line.contains("user-action-download")) {
                		downloadUrl = WebsiteHelper.parseAttribute(line, "href");
                	}
                	
                }
            }
            if (downloadUrl.isEmpty()) {
                throw new NoSuchElementException("Addon couldn't be found");
            }
            return downloadUrl;
        } catch (IOException e) {
            throw new BusinessException("Can't access " + url, e);
        }
	}

}
