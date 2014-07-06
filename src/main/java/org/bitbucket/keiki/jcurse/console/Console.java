package org.bitbucket.keiki.jcurse.console;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bitbucket.keiki.jcurse.Addon;
import org.bitbucket.keiki.jcurse.AddonInstallationManager;
import org.bitbucket.keiki.jcurse.BusinessException;
import org.bitbucket.keiki.jcurse.Configuration;
import org.bitbucket.keiki.jcurse.ConfigurationImpl;
import org.bitbucket.keiki.jcurse.ReleaseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class Console {

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
            LOG.info("jcurse add (alpha|beta) [addon name1, name2, ...]");
            LOG.info("jcurse remove [addon name1, name2, ...]");
            LOG.info("jcurse update (--force|-f) [addon name1, name2, ... | all]");
            LOG.info("jcurse set [alpha|beta|release] [addon name, ...]");
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
        executeCommands(arguments, new AddonInstallationManager(config), command);
    }

    static void executeCommands(List<String> arguments,
            AddonInstallationManager repositoryManager, String command) {
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
            AddonInstallationManager repositoryManager, String command) {
        if (arguments.size() >= 2) {
            List<String> unprocessedArgs = arguments.subList(1, arguments.size());
            
            switch (command) {
                case "add":
                	add(repositoryManager, unprocessedArgs);
                    break;
                case "remove":
                    repositoryManager.remove(unprocessedArgs);
                    LOG.info("removed " + unprocessedArgs);
                    break;
                case "update":
                    update(repositoryManager, unprocessedArgs);
                    break;
                case "set":
                	setReleaseStatus(repositoryManager, unprocessedArgs);
                	break;
                default:
                    throw new BusinessException("Unrecognized command " + command);
            }
        }
    }

	private static void setReleaseStatus(AddonInstallationManager repositoryManager, List<String> unprocessedArgs) {
	    ReleaseStatus status = ReleaseStatus.valueOfIgnoreCase(unprocessedArgs.get(0));
	    if (status == null) {
	        throw new BusinessException("status '" + unprocessedArgs.get(0) + "' is unknown");
	    }
	    repositoryManager.setReleaseStatus(status, unprocessedArgs.subList(1, unprocessedArgs.size()));
    }

    private static void add(
			AddonInstallationManager repositoryManager,
			List<String> unprocessedArgs) {
		ReleaseStatus status = ReleaseStatus.valueOfIgnoreCase(unprocessedArgs.get(0));
		List<String> subList;
		if (status == null) {
			status = ReleaseStatus.RELEASE;
			subList = unprocessedArgs;
		} else {
		    subList = unprocessedArgs.subList(1, unprocessedArgs.size());
		}
		
		List<Addon> added = repositoryManager.add(subList, status);
		LOG.info("added " + added);
	}

    private static void update(AddonInstallationManager repositoryManager, List<String> unprocessedArgsPara) {
        List<String> unprocessedArgs = unprocessedArgsPara;
        String secondParameter = unprocessedArgs.get(0);
        boolean forceUpdate = false;
        if ("-f".equalsIgnoreCase(secondParameter) || "--force".equalsIgnoreCase(secondParameter)) {
            forceUpdate = true;
            LOG.info("Force update!");
            unprocessedArgs = unprocessedArgs.subList(1, unprocessedArgs.size());
        }
        if ("all".equalsIgnoreCase(unprocessedArgs.get(0))) {
            LOG.info("updating all addons");
            repositoryManager.updateAll(forceUpdate);
        } else {
            LOG.info("updating " + unprocessedArgs);
            repositoryManager.update(unprocessedArgs, forceUpdate);
        }
        LOG.info("all addons are now up2date");
    }

    private static void executeOneArgumentCommand(List<String> arguments,
            AddonInstallationManager repositoryManager, String command) {
        if (arguments.size() == 1) {
            switch (command) {
                case "list":
                    listAddons(repositoryManager.getAddons(), false);
                    break;
                case "listv":
                    listAddons(repositoryManager.getAddons(), true);
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

    private static void listAddons(Collection<Addon> addons, boolean verbose) {
        if (addons.isEmpty()) {
            LOG.info("We don't know of any installed addon.");
            return;
        }
        LOG.info("Currently installed addons:");
        
        for (Addon addon : addons) {
            if (verbose) {
                LOG.info(addon.toStringVerbose());
            } else {
                LOG.info(addon.toString());
            }
        }
    }
}
