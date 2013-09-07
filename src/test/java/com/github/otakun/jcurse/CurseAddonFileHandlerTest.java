package com.github.otakun.jcurse;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class CurseAddonFileHandlerTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private static final String URL_PLACEHOLDER = "#BASE_URL_TO_REPLACE#";
	private static String baseUrl = null;

	private File properties;
	
	@BeforeClass
	public static void beforeClass() throws IOException {
		baseUrl = Thread.currentThread().getContextClassLoader().
				getResource("websites/").toString();
		Configuration.getConfiguration().setCurseBaseUrl(baseUrl);

		String fileUrl = baseUrl + File.separator + "5.3.6Bagnon.html";
		String substring = fileUrl.substring(5);

		File file = new File(substring);
		String content = new String(FileUtils.readFileToString(file));
		String replaceOnce = StringUtils.replaceOnce(content, URL_PLACEHOLDER, baseUrl);
		FileUtils.write(file, replaceOnce);
	}

	
	@AfterClass
	public static void afterClass() throws IOException {
		String fileUrl = baseUrl + File.separator + "5.3.6Bagnon.html";
		String substring = fileUrl.substring(5);

		File file = new File(substring);
		String content = new String(FileUtils.readFileToString(file));
		String replaceOnce = StringUtils.replaceOnce(content, baseUrl, URL_PLACEHOLDER);
		FileUtils.write(file, replaceOnce);
	}

	@Test
	public void testDownloadToWoW() {
		File root = folder.getRoot();
		Configuration.getConfiguration().setWowFolder(root.getAbsolutePath());
		CurseAddonFileHandler fileHandler = new CurseAddonFileHandler();
		List<Addon> addons = Addon.newInstance(Arrays.asList("bagnon"));
		fileHandler.downloadToWow(addons);

		String rootPath = root.getAbsolutePath();
		String addonPath = rootPath + File.separator + "Interface" + File.separator + "AddOns";
		File addonRoot = new File(addonPath);
		File[] listFiles = addonRoot.listFiles();
		assertEquals(5, listFiles.length);
	}

}
