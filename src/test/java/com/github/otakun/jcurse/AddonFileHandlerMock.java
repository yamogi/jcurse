package com.github.otakun.jcurse;

import java.util.Collection;
import java.util.List;

public class AddonFileHandlerMock implements AddonFileHandler {

	@Override
	public void downloadToWow(Addon newAddon) {
		newAddon.setLastZipFileName(newAddon.getAddonNameId() + "-1.0.zip");
	}

	@Override
	public void removeAddons(Collection<Addon> toDelete) {
	}

	@Override
	public void removeAddonFolders(Collection<String> toDpublicelete) {
	}

	@Override
	public String getCompressedFileName(String addonNameId) {
		return addonNameId + "-1.0.zip";
	}

	@Override
	public void downloadToWow(List<Addon> toDownload) {
	}

}
