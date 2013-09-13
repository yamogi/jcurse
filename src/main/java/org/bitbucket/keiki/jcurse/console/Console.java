package org.bitbucket.keiki.jcurse.console;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bitbucket.keiki.jcurse.Addon;
import org.bitbucket.keiki.jcurse.AddonRepositoryManager;
import org.bitbucket.keiki.jcurse.ErrorCode;

public class Console {

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
					List<Addon> added = repositoryManager.add(addons);
					System.out.println("added " + added);
					break;
				case "remove":
					repositoryManager.remove(addons);
					System.out.println("removed " + addons);
					break;
				case "update":
					if ("all".equalsIgnoreCase(arguments.get(1))) {
						repositoryManager.updateAll();
						System.out.println("updated all addons");
					} else {
						repositoryManager.update(addons);
						System.out.println("updated " + addons);
					}
					break;
				default:
					printHelpExit(ErrorCode.CONSOLE_UNKNOWN_OPTION, command);
					break;
			}
		}
	}

	private static void listAddons(Collection<Addon> addons) {
		System.out.println();
		if (addons.isEmpty()) {
			System.out.println("We don't know of any installed addon.");
			return;
		}
		
		System.out.println("Currently installed addons:");
		
		for (Addon addon : addons) {
			System.out.println(addon);
		}
	}

	private static void printHelpExit(ErrorCode errorCode, String... messageParameter) {
		if (messageParameter.length > 0) {
			System.err.println("Error: " + MessageFormat.format(errorCode.getErrorMessage(), (Object[]) messageParameter));
		} else {
			System.err.println("Error: " + errorCode.getErrorMessage());
		}
		
		System.out.println("\r\nUsage:");
		System.out.println("jcurse [add | remove | update] [addon name | all]");
		System.out.println("jcurse list");
		System.exit(errorCode.getErrorCode());
	}
}
