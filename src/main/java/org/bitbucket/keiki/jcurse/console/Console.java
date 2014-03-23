package org.bitbucket.keiki.jcurse.console;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bitbucket.keiki.jcurse.Addon;
import org.bitbucket.keiki.jcurse.AddonRepositoryManager;
import org.bitbucket.keiki.jcurse.BusinessException;
import org.bitbucket.keiki.jcurse.Configuration;
import org.bitbucket.keiki.jcurse.ConfigurationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Console {

    private static final String SET_WOW_ARGUMENT = "--set-wow";

    private static final Logger LOG = LoggerFactory.getLogger(Console.class);
    
    private Console() {
        // main class
    }
    
    public static void main(String... args) {
        try {
            Configuration config = new ConfigurationImpl();
            executeArguments(Arrays.asList(args), config);
        } catch (BusinessException e) {
            LOG.error(e.getMessage());
            LOG.debug(e.getMessage(), e);
            LOG.info("\r\nUsage:");
            LOG.info("jcurse [add | remove | update] [addon name1, name2, ... | all]");
            LOG.info("jcurse list");
            LOG.info("jcurse export");
            LOG.info("jcurse " + SET_WOW_ARGUMENT + " <full path to wow folder>");

        }
    }

    static void executeArguments(List<String> arguments, Configuration config) {
        if (arguments.size() < 1) {
            throw new BusinessException("Number of arguments are wrong");
        }
        String command = arguments.get(0);
        if (executeConfigChanges(command, arguments, config)) {
            return;
        }
        config.load();
        executeCommands(arguments, new AddonRepositoryManager(config), command);
    }

    static void executeCommands(List<String> arguments,
            AddonRepositoryManager repositoryManager, String command) {
        executeOneArgumentCommand(arguments, repositoryManager, command);
        executeTwoArgumentsCommand(arguments, repositoryManager, command);
    }

    private static boolean executeConfigChanges(String command, List<String> args, Configuration config) {
        if (command.equals(SET_WOW_ARGUMENT)) { 
            if (args.size() >= 2) {
                config.setWowFolder(args.get(1));
                config.save();
                LOG.info("Changed wow directory to " + args.get(1));
            } else {
                throw new BusinessException("No wow directory given. Aborting.");
            }
            return true;
        }
        return false;
    }

    private static void executeTwoArgumentsCommand(List<String> arguments,
            AddonRepositoryManager repositoryManager, String command) {
        if (arguments.size() >= 2) {
            List<String> argsRemainder = arguments.subList(1, arguments.size());
            
            switch (command) {
                case "add":
                    List<Addon> added = repositoryManager.add(argsRemainder);
                    LOG.info("added " + added);
                    break;
                case "remove":
                    repositoryManager.remove(argsRemainder);
                    LOG.info("removed " + argsRemainder);
                    break;
                case "update":
                    if ("all".equalsIgnoreCase(arguments.get(1))) {
                        LOG.info("updating all addons");
                        repositoryManager.updateAll();
                    } else {
                        LOG.info("updating " + argsRemainder);
                        repositoryManager.update(argsRemainder);
                    }
                    LOG.info("all addons are now up2date");
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
                case "export":
                    exportAddons(repositoryManager.getAddons());
                    break;
                default:
                    throw new BusinessException("Unregonized command " + command);
            }
        }
    }

    private static void exportAddons(Collection<Addon> addons) {
        if (addons.isEmpty()) {
            LOG.info("No addon(s) are installed");
            return;
        }
        StringBuilder build = new StringBuilder("jcurse add");
        for (Addon addon : addons) {
            build.append(' ').append(addon.getAddonNameId());
        }
        LOG.info(build.toString());
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
