package org.bitbucket.keiki.jcurse.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public final class Addon implements Comparable<Addon> {
    
    private String addonNameId;

    private int addonId;
    
    private String lastZipFileName;
    
    private int versionId;
    
    private Set<String> folders;
    
    private ReleaseStatus releaseStatus = ReleaseStatus.RELEASE;
    
    /**
     * Creates an {@link Addon} instance. 
     */
    private Addon() {
    }
    
    public static List<Addon> newInstance(Collection<String> addonNames, ReleaseStatus status) {
        List<Addon> addons = new ArrayList<>();
        for (String addonName : addonNames) {
            addons.add(newInstance(addonName, status));
        }
        return addons;
    }
    public static List<Addon> newInstance(Collection<String> addonNames) {
        List<Addon> addons = new ArrayList<>();
        for (String addonName : addonNames) {
            addons.add(newInstance(addonName, ReleaseStatus.RELEASE));
        }
        return addons;
    }

    private static Addon newInstance(String shortAddonName, ReleaseStatus status) {
        Addon addon = new Addon();
        addon.addonNameId = shortAddonName;
        addon.releaseStatus = status;
        return addon;
    }
    
    public int getAddonId() {
        return addonId;
    }

    public void setAddonId(int addonId) {
        this.addonId = addonId;
    }

    public String getAddonNameId() {
        return addonNameId;
    }

    public void setAddonNameId(String addonNameId) {
        this.addonNameId = addonNameId;
    }

    public ReleaseStatus getReleaseStatus() {
        return releaseStatus;
    }

    public void setReleaseStatus(ReleaseStatus releaseStatus) {
        this.releaseStatus = releaseStatus;
    }

    public String getLastZipFileName() {
        return lastZipFileName;
    }

    public void setLastZipFileName(String lastZipFileName) {
        this.lastZipFileName = lastZipFileName;
    }

    public Set<String> getFolders() {
        return folders;
    }

    public void setFolders(Set<String> folders) {
        this.folders = folders;
    }

    @Override
    public int hashCode() {
        return addonNameId.hashCode();
    }
    
    public int getVersionId() {
        return versionId;
    }

    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Addon)) {
            return false;
        }
        Addon otherAddon = (Addon) obj;
        return addonNameId.equals(otherAddon.addonNameId);
    }

    @Override
    public int compareTo(Addon addon) {
        return addonNameId.compareTo(addon.addonNameId);
    }
    
    @Override
    public String toString() {
        return addonNameId + (lastZipFileName != null ? " version " + lastZipFileName : "")
                + (releaseStatus == ReleaseStatus.RELEASE ? "" : " (" + releaseStatus + ")");
    }

    public String toStringVerbose() {
        return addonNameId + (lastZipFileName != null ? ", version "
                + lastZipFileName : "") + " " + versionId;
    }



    
}
