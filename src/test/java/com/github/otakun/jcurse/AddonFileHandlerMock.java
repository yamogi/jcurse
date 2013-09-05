package com.github.otakun.jcurse;

import java.util.Collection;
import java.util.List;

import com.github.otakun.jcurse.api.AddonFileHandler;

public class AddonFileHandlerMock implements AddonFileHandler {

	@Override
	public void downloadToWow(Addon newAddon) {
	}

	@Override
	public void downloadToWow(String downloadUrl, Addon addon) {
	}

	@Override
	public void removeAddons(Collection<Addon> toDelete) {
	}

	@Override
	public void removeAddonFolders(Collection<String> toDpublicelete) {
	}

	@Override
	public String getCompressedFileName(String gameAddonNameId) {
		return gameAddonNameId + "-1.0.zip";
	}

	@Override
	public void downloadToWow(List<Addon> toDownload) {
	}

}
