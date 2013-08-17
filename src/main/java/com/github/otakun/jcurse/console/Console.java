package com.github.otakun.jcurse.console;

import com.github.otakun.jcurse.AddonRepository;
import com.github.otakun.jcurse.ErrorCode;

public class Console {

	public static void main(String[] args) {
		if (args.length != 2) {
			printHelpExit(ErrorCode.CONSOLE_ARGUMENTS_NUMBER);
		}
		if ("add".equals(args[0])) {
			AddonRepository.add(args[1]);
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
