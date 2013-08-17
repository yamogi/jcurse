package com.github.otakun.jcurse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class CurseAddonFileHandler {
	
	public static void downloadToWow(Addon newAddon) {
		WebDriver driver = new HtmlUnitDriver();
		String url = "http://www.curse.com/addons/" + newAddon.getGameAddonNameId();
		driver.get(url);
		System.out.println("website of addon " + url);
		WebElement downloadButton = driver.findElement(By.xpath("//*[@id=\"project-overview\"]/div/div[2]/div/div/div[2]/ul/li[1]/em/a"));
		downloadButton.click();
		WebElement directDownloadElement = driver.findElement(By.xpath("//*[@id=\"file-download\"]/div/div[2]/div/div/div[1]/p/a"));
		String downloadUrl = directDownloadElement.getAttribute("data-href");
		System.out.println("Downloading " + downloadUrl);
		
		int lastIndexOf = downloadUrl.lastIndexOf("/");
		if (lastIndexOf == -1) {
			throw new RuntimeException("Download url wrong"); //FIXME correct error handling
		}
		String zipFilename = downloadUrl.substring(lastIndexOf + 1);
		newAddon.setLastZipFileName(zipFilename);
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
			
			Set<String> addonFolders = new HashSet<>();

			while(ze!=null){

				String fileName = ze.getName();
				
				//only one should be possible. The other one has -1
				int index = Math.max(fileName.indexOf('/'), fileName.indexOf('\\'));
				addonFolders.add(fileName.substring(0, index));
				
				
				File newFile = new File(outputFolder + File.separator + fileName);

				//create all non exists folders
				//else you will hit FileNotFoundException for compressed folder
				new File(newFile.getParent()).mkdirs();
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

			newAddon.setFolders(addonFolders);
			
			System.out.println("Done unzipping");
		} catch(IOException ex){
			//TODO error handling
			ex.printStackTrace(); 
		}
	}
	
	
	public static void removeAddonFolders(Collection<String> toDelete) {
		try {
			for (String folderName : toDelete) {
					FileUtils.deleteDirectory(new File(Configuration.getConfiguration().getWowAddonFolder() + folderName));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
