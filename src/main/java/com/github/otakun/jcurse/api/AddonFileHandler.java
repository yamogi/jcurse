package com.github.otakun.jcurse.api;

import java.util.Collection;
import java.util.List;

import com.github.otakun.jcurse.Addon;

public interface AddonFileHandler {

	public abstract void downloadToWow(Addon newAddon);

	public abstract void downloadToWow(String downloadUrl, Addon addon);

	public abstract void removeAddons(Collection<Addon> toDelete);

	public abstract void removeAddonFolders(Collection<String> toDelete);

	public abstract String getCompressedFileName(String gameAddonNameId);

	public abstract void downloadToWow(List<Addon> toDownload);

}