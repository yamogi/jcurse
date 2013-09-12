package org.bitbucket.keiki.jcurse.console;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bitbucket.keiki.jcurse.Addon;
import org.bitbucket.keiki.jcurse.AddonRepositoryManager;
import org.bitbucket.keiki.jcurse.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Console {

	private static final Logger LOG = LoggerFactory.getLogger(Console.class);
	
	public static void main(String... args) {
		List<String> arguments = Arrays.asList(args);
		if (arguments.size() < 1) {
			printHelpExit(ErrorCode.CONSOLE_ARGUMENTS_NUMBER);
		}
		executeArguments(arguments, new AddonRepositoryManager());
	}

	static void executeArguments(List<String> arguments, AddonRepositoryManager repositoryManager) {
		String command = arguments.get(0);
		if (arguments.size() == 1) {
			switch (command) {
				case "list":
					listAddons(repositoryManager.getAddons());
					break;
				default:
					printHelpExit(ErrorCode.CONSOLE_ARGUMENTS_NUMBER);		
			}
		}
		if (arguments.size() >= 2) {
			List<String> addons = arguments.subList(1, arguments.size());
			
			switch (command) {
				case "add":
					repositoryManager.add(addons);
					LOG.info("added " + addons);
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
					printHelpExit(ErrorCode.CONSOLE_UNKNOWN_OPTION, command);
					break;
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

	private static void printHelpExit(ErrorCode errorCode, String... messageParameter) {
		if (messageParameter.length > 0) {
			LOG.error("Error: " + MessageFormat.format(errorCode.getErrorMessage(), (Object[]) messageParameter));
		} else {
			LOG.error("Error: " + errorCode.getErrorMessage());
		}
		
		LOG.info("\r\nUsage:");
		LOG.info("jcurse [add | remove | update] [addon name | all]");
		LOG.info("jcurse list");
		System.exit(errorCode.getErrorCode());
	}
}
