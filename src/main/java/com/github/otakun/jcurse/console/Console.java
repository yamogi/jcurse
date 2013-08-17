package com.github.otakun.jcurse.console;

import java.util.Collection;

import com.github.otakun.jcurse.Addon;
import com.github.otakun.jcurse.AddonRepositoryManager;
import com.github.otakun.jcurse.ErrorCode;

public class Console {

	public static void main(String[] args) {
		switch (args[0]) {
			case "add":
				AddonRepositoryManager.getInstance().add(args[1]);
				break;
			case "remove":
				AddonRepositoryManager.getInstance().remove(args[1]);
				break;
			case "update":
				if ("all".equalsIgnoreCase(args[1])) {
					AddonRepositoryManager.getInstance().updateAll();
				} else {
					AddonRepositoryManager.getInstance().update(args[1]);
				}
				break;
			case "list":
				listAddons();
				break;
			default:
				printHelpExit(ErrorCode.CONSOLE_ARGUMENTS_NUMBER);
				break;
		}
	}

	private static void listAddons() {
		Collection<Addon> addons = AddonRepositoryManager.getInstance().getAddons();
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

	private static void printHelpExit(ErrorCode errorCode) {
		System.err.println("Error: " + errorCode.getErrorMessage());
		System.out.println("\r\nUsage:");
		System.out.println("jcurse [add | remove | update] [addon name | all]");
		System.out.println("jcurse list");
		System.exit(errorCode.getErrorCode());
	}
}
