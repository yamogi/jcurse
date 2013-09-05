package com.github.otakun.jcurse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class CurseAddonFileHandler implements AddonFileHandler {
	
	@Override
	public void downloadToWow(Addon newAddon) {
		String downloadUrl = getDownloadUrl(newAddon.getAddonNameId());
		String zipFilename = getZipFileName(downloadUrl);
		
		Set<String> addonFolders = downloadAndExtract(downloadUrl);
		
		newAddon.setLastZipFileName(zipFilename);
		newAddon.setFolders(addonFolders);
		
		System.out.println("Done unzipping");
	}

	@Override
	public void downloadToWow(String downloadUrl, Addon addon) {
		String zipFilename = getZipFileName(downloadUrl);
		
		Set<String> addonFolders = downloadAndExtract(downloadUrl);
		
		addon.setLastZipFileName(zipFilename);
		addon.setFolders(addonFolders);
		
		System.out.println("Done unzipping");
	}

	private Set<String> downloadAndExtract(String downloadUrl) {
		Set<String> addonFolders = new HashSet<>();
		try{
			URL website = new URL(downloadUrl);

			byte[] buffer = new byte[1024];

			//create output directory is not exists
			String outputFolder = Configuration.getConfiguration().getWowAddonFolder();
			File folder = new File(outputFolder);
			if(!folder.exists()){
				folder.mkdirs();
			}

			//get the zip file content
			ZipInputStream zis = new ZipInputStream(website.openStream());
			//get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();
			

			while(ze!=null){
				String fileName = ze.getName();
				if (fileName.endsWith("/") || fileName.endsWith("\\")) {
					//directory
					ze = zis.getNextEntry();
					continue;
				}
				//only one should be possible. The other one has -1
				int index = Math.max(fileName.indexOf('/'), fileName.indexOf('\\'));
				addonFolders.add(fileName.substring(0, index));
				
				
				File newFile = new File(outputFolder + File.separator + fileName);

				//create all non exists folders
				//else you will hit FileNotFoundException for compressed folder
				newFile.getParentFile().mkdirs();
				//overwrites all files
				FileOutputStream fos = new FileOutputStream(newFile);             

				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}

				fos.close();   
				ze = zis.getNextEntry();
			}
			//XXX close it the correct way
			zis.closeEntry();
			zis.close();
			return addonFolders;
		} catch(IOException e){
			//TODO error handling
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


	private static String getZipFileName(String downloadUrl) {
		int lastIndexOf = downloadUrl.lastIndexOf("/");
		if (lastIndexOf == -1) {
			throw new RuntimeException("Download url wrong"); //FIXME correct error handling
		}
		return downloadUrl.substring(lastIndexOf + 1);
	}


	private static String getDownloadUrl(String gameAddonNameId) {
		WebDriver driver = new HtmlUnitDriver();
		String url = "http://www.curse.com/addons/wow/" + gameAddonNameId;
		driver.get(url);
		System.out.println("website of addon " + url);
		WebElement downloadButton = driver.findElement(By.xpath("//*[@id=\"project-overview\"]/div/div[2]/div/div/div[2]/ul/li[1]/em/a"));
		downloadButton.click();
		WebElement directDownloadElement = driver.findElement(By.xpath("//*[@id=\"file-download\"]/div/div[2]/div/div/div[1]/p/a"));
		String downloadUrl = directDownloadElement.getAttribute("data-href");
		return downloadUrl;
	}
	
	@Override
	public void removeAddons(Collection<Addon> toDelete) {
		for (Addon addon : toDelete) {
			removeAddonFolders(addon.getFolders());
		}
	}

	
	
	@Override
	public void removeAddonFolders(Collection<String> toDelete) {
		try {
			for (String folderName : toDelete) {
					FileUtils.deleteDirectory(new File(Configuration.getConfiguration().getWowAddonFolder() + folderName));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public String getCompressedFileName(String gameAddonNameId) {
		String downloadUrl = getDownloadUrl(gameAddonNameId);
		return getZipFileName(downloadUrl);
	}

	@Override
	public void downloadToWow(List<Addon> toDownload) {
		for (Addon addon : toDownload) {
			downloadToWow(addon);
		}
	}
}
