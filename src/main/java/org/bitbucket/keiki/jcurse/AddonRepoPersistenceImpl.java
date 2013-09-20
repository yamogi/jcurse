package org.bitbucket.keiki.jcurse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;


public final class AddonRepoPersistenceImpl implements AddonRepoPersistence {

    private final File repoFile;
    
    public AddonRepoPersistenceImpl(String pathToRepoFile) {
        this.repoFile = new File(pathToRepoFile);
    }
    
    @Override
    public synchronized Collection<Addon> loadInstalledAddons() {
        try {
            if (!repoFile.exists() && Files.isWritable(Paths.get(repoFile.getAbsolutePath()))) {
                throw new BusinessException("Can not find or write to repository file at '" + repoFile.getAbsolutePath() + "'.");
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(repoFile, new TypeReference<Collection<Addon>>() {});
        } catch (IOException e) {
            throw new BusinessException("Could not read repository file from " + repoFile.getAbsolutePath(), e);
        }
    }
    
    @Override
    public synchronized void saveInstalledAddons(Collection<Addon> addons) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(repoFile, addons);
        } catch (IOException e) {
            throw new BusinessException("Could not write repository file.", e);
        }
    }
    
    @Override
    public String toString() {
        return repoFile.toString();
    }
}
