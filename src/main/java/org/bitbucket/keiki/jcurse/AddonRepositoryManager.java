package org.bitbucket.keiki.jcurse;

import org.apache.commons.lang3.StringUtils;
import org.bitbucket.keiki.jcurse.curse.CurseHandler;
import org.bitbucket.keiki.jcurse.curse.CurseAddonFileHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


public final class AddonRepositoryManager {

    private static final Logger LOG = LoggerFactory.getLogger(AddonRepositoryManager.class);

    private static final int NUMBER_OF_THREADS = 5;

    private final AddonRepoPersistence persistence;

    private final CurseHandler curse;

    private final Map<Addon, Addon> repository;

    private static final ExecutorService EXECUTOR_UPDATE = Executors.newFixedThreadPool(NUMBER_OF_THREADS, new ThreadFactory() {
        
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        }
    }); 

    public AddonRepositoryManager(Configuration config) {
        this(new AddonRepoPersistenceImpl(ConfigurationImpl.CONFIG_PATH + "repository"),
                new CurseAddonFileHandler(config.getWowAddonFolder(), config.getCurseBaseUrl()));
    }

    public AddonRepositoryManager(AddonRepoPersistence persistence,
            CurseHandler addonFileHandler) {
        this.persistence = persistence;
        this.curse = addonFileHandler;
        Collection<Addon> addons = persistence.loadInstalledAddons();
        Map<Addon, Addon> tmpTree = new ConcurrentSkipListMap<>();
        for (Addon addon : addons) {
            tmpTree.put(addon, addon);
        }
        repository = tmpTree;
    }

    public List<Addon> add(Collection<String> addonName, ReleaseStatus status) {
        List<Addon> newAddons = Addon.newInstance(addonName, status);

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

    public void updateAll(boolean forceUpdate) {
        updateInternal(repository.values(), forceUpdate);
    }


    private void updateInternal(Collection<Addon> addons, final boolean forceUpdate) {
        List<Callable<Void>> futures = new ArrayList<>(addons.size());
        for (final Addon addon : addons) {
            futures.add(new Callable<Void>() {

                @Override
                public Void call() {
                    updateInternal(addon, forceUpdate); 
                    return null;
                }
            });
        }
        try {
            EXECUTOR_UPDATE.invokeAll(futures);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        persistence.saveInstalledAddons(repository.values());
    }

    private void updateInternal(Addon addon, boolean forceUpdate) {
        String downloadUrl = curse.getDownloadUrl(addon);
        int fileName = CurseAddonFileHandler.extractFileId(StringUtils.split(downloadUrl, '/'));
        if (!forceUpdate && addon.getVersionId() == fileName) {
            LOG.info(addon.getAddonNameId() + " already up2date");
            return;
        }
        LOG.info("updating " + addon.getAddonNameId());
        curse.removeAddon(repository.get(addon));
        curse.downloadToWow(addon, downloadUrl);

        repository.put(addon, addon);
        LOG.info("updated " + addon.getAddonNameId());
    }

    public void update(List<String> addons, boolean forceUpdate) {
        List<Addon> repoAddons = checkAddonAlreadyExists(Addon.newInstance(addons), true);
        updateInternal(repoAddons, forceUpdate);
    }

    public Collection<Addon> getAddons() {
        return repository.values();
    }
    
    public void setReleaseStatus(ReleaseStatus releaseStatus, List<String> addons) {
        List<Addon> repoAddons = checkAddonAlreadyExists(Addon.newInstance(addons), true);
        for (Addon addon : repoAddons) {
            addon.setReleaseStatus(releaseStatus);
            repository.put(addon, addon);
        }
        persistence.saveInstalledAddons(repository.values());
        LOG.info("Prefering " + releaseStatus.getStatus() + " from now on for " + repoAddons);
    }
}
