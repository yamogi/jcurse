package com.github.otakun.jcurse;

public enum ErrorCode {
	//application errors with failure (negative values)
	CONSOLE_ARGUMENTS_NUMBER(-1, "Number of arguments are wrong");
	
	//application errors but completed
	
	private final int errorCode;
	private final String errorMessage;
	
	private ErrorCode(int errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
