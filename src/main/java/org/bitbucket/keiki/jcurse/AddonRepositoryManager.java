package org.bitbucket.keiki.jcurse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class AddonRepositoryManager {
    
    private static final Logger LOG = LoggerFactory.getLogger(AddonRepositoryManager.class);

    private final AddonRepoPersistence persistence;
    
    private final AddonFileHandler curse;

    private final Map<Addon, Addon> repository;
    
    public AddonRepositoryManager(Configuration config) {
        this(new AddonRepoPersistenceImpl(ConfigurationImpl.CONFIG_PATH + "repository"),
                new CurseAddonFileHandler(config.getWowAddonFolder(), config.getCurseBaseUrl()));
    }
    
    public AddonRepositoryManager(AddonRepoPersistence persistence,
            AddonFileHandler addonFileHandler) {
        this.persistence = persistence;
        this.curse = addonFileHandler;
        Collection<Addon> addons = persistence.loadInstalledAddons();
        Map<Addon, Addon> tmpTree = new TreeMap<>();
        for (Addon addon : addons) {
            tmpTree.put(addon, addon);
        }
        repository = tmpTree;
    }
    
        public List<Addon> add(Collection<String> addonName) {
        List<Addon> newAddons = Addon.newInstance(addonName);

        List<Addon> toDownload = checkAddonAlreadyExists(newAddons, false);
        
                List<Addon> downloadToWow = curse.downloadToWow(toDownload);
        
                updateRepository(downloadToWow);
                return downloadToWow;
    }

    private void updateRepository(List<Addon> toDownload) {
        for (Addon addon : toDownload) {
            repository.put(addon, addon);
        }
        persistence.saveInstalledAddons(repository.values());
    }

    private List<Addon> checkAddonAlreadyExists(List<Addon> newAddons, boolean getExistingAddons) {
        List<Addon> toDownload = new ArrayList<>();
        List<Addon> toUpdate = new ArrayList<>();
        for (Addon addon : newAddons) {
            Addon repoAddon = repository.get(addon);
            if (repoAddon != null) {
                toUpdate.add(repoAddon);
            } else {
                toDownload.add(addon);
            }
        }
        if (getExistingAddons) {
            if (!toDownload.isEmpty()) {
                LOG.info("The Addon(s) " + toDownload + " are not installed.");
            }
            return toUpdate;
        } else {
            if (!toUpdate.isEmpty()) {
                LOG.info("The Addon(s) " + toUpdate + " are already installed.");
            }
            return toDownload;
        }
    }



    public void remove(List<String> addons) {
        List<Addon> newAddons = Addon.newInstance(addons);
        List<Addon> repoAddons = checkAddonAlreadyExists(newAddons, true);
        curse.removeAddons(repoAddons);
        removeAddonsFromRepo(repoAddons);
    }

    private void removeAddonsFromRepo(List<Addon> repoAddons) {
        for (Addon addon : repoAddons) {
            repository.remove(addon);
        }    
        persistence.saveInstalledAddons(repository.values());
    }

    public void updateAll() {
        updateInternal(repository.values());
        persistence.saveInstalledAddons(repository.values());
    }


    private void updateInternal(Collection<Addon> repoAddons) {
        for (Addon addon : repoAddons) {
            updateInternal(addon);    
        }
    }

    private void updateInternal(Addon addon) {
        String downloadUrl = curse.getDownloadUrl(addon.getAddonNameId());
        String fileName = CurseAddonFileHandler.extractZipFileName(downloadUrl);
        if (addon.getLastZipFileName().equals(fileName)) {
            LOG.info(addon.getAddonNameId() + " already up2date");
            return;
        }
        LOG.info("updating " + addon.getAddonNameId());
        curse.removeAddonFolders(repository.get(addon).getFolders());
        curse.downloadToWow(addon, downloadUrl);
        
        repository.put(addon, addon);
        LOG.info("updated " + addon.getAddonNameId());
    }

    public void update(List<String> addons) {
        List<Addon> newAddon = Addon.newInstance(addons);
        List<Addon> repoAddons = checkAddonAlreadyExists(newAddon, true);
        updateInternal(repoAddons);
        persistence.saveInstalledAddons(repository.values());
    }

    public Collection<Addon> getAddons() {
        return repository.values();
    }
}
