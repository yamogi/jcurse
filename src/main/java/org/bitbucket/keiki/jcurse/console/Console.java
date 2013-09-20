package org.bitbucket.keiki.jcurse.console;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bitbucket.keiki.jcurse.Addon;
import org.bitbucket.keiki.jcurse.AddonRepositoryManager;
import org.bitbucket.keiki.jcurse.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Console {

    private static final Logger LOG = LoggerFactory.getLogger(Console.class);
    
    private Console() {
        // main class
    }
    
    public static void main(String... args) {
        try {
            executeArguments(Arrays.asList(args), new AddonRepositoryManager());
        } catch (BusinessException e) {
            LOG.error(e.getMessage());
            LOG.debug(e.getMessage(), e);
            LOG.info("\r\nUsage:");
            LOG.info("jcurse [add | remove | update] [addon name | all]");
            LOG.info("jcurse list");

        }
    }

    static void executeArguments(List<String> arguments, AddonRepositoryManager repositoryManager) {
        if (arguments.size() < 1) {
            throw new BusinessException("Number of arguments are wrong");
        }
        
        String command = arguments.get(0);
        executeOneArgumentCommand(arguments, repositoryManager, command);
        executeTwoArgumentsCommand(arguments, repositoryManager, command);
    }

    private static void executeTwoArgumentsCommand(List<String> arguments,
            AddonRepositoryManager repositoryManager, String command) {
        if (arguments.size() >= 2) {
            List<String> addons = arguments.subList(1, arguments.size());
            
            switch (command) {
                case "add":
                    List<Addon> added = repositoryManager.add(addons);
                    LOG.info("added " + added);
                    break;
                case "remove":
                    repositoryManager.remove(addons);
                    LOG.info("removed " + addons);
                    break;
                case "update":
                    if ("all".equalsIgnoreCase(arguments.get(1))) {
                        repositoryManager.updateAll();
                        LOG.info("updated all addons");
                    } else {
                        repositoryManager.update(addons);
                        LOG.info("updated " + addons);
                    }
                    break;
                default:
                    throw new BusinessException("Unrecognized command " + command);
            }
        }
    }

    private static void executeOneArgumentCommand(List<String> arguments,
            AddonRepositoryManager repositoryManager, String command) {
        if (arguments.size() == 1) {
            switch (command) {
                case "list":
                    listAddons(repositoryManager.getAddons());
                    break;
                default:
                    throw new BusinessException("Unregonized command " + command);
            }
        }
    }

    private static void listAddons(Collection<Addon> addons) {
        if (addons.isEmpty()) {
            LOG.info("We don't know of any installed addon.");
            return;
        }
        
        LOG.info("Currently installed addons:");
        
        for (Addon addon : addons) {
            LOG.info(addon.toString());
        }
    }
}
