package com.github.otakun.jcurse;

import java.util.Collection;
import java.util.List;

public interface AddonFileHandler {

	public abstract void downloadToWow(Addon newAddon);

	public abstract void removeAddons(Collection<Addon> toDelete);

	public abstract void removeAddonFolders(Collection<String> toDelete);

	public abstract String getCompressedFileName(String gameAddonNameId);

	public abstract void downloadToWow(List<Addon> toDownload);

}