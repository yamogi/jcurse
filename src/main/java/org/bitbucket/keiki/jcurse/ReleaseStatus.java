package org.bitbucket.keiki.jcurse;

public enum ReleaseStatus {
	ALPHA('a',"alpha"), BETA('b', "beta"), RELEASE('r', "release");
	
	final char statusAbbr;
	final String status;
	
	private ReleaseStatus(char statusAbbreviation, String status) {
		this.statusAbbr = statusAbbreviation;
		this.status = status;
	}
	
	public char getStatusAbbr() {
		return statusAbbr;
	}

	public String getStatus() {
		return status;
	}

	public static ReleaseStatus valueOf(char status) {
		
		switch(status) {
		case 'r':
			return RELEASE;
		case 'b':
			return BETA;
		case 'a':
			return ALPHA;
		}
		throw new IllegalArgumentException(status + " is no valid status");
	}
	
	public static ReleaseStatus valueOfIgnoreCase(String status) {
		if ("alpha".equalsIgnoreCase(status)) {
			return ALPHA;
		}
		if ("beta".equalsIgnoreCase(status)) {
			return BETA;
		}
		if ("release".equalsIgnoreCase(status)) {
			return RELEASE;
		}
		return null;
	}
}
