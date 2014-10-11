package org.bitbucket.keiki.jcurse.data;

public enum ReleaseStatus {
	ALPHA('a'), BETA('b'), RELEASE('r');

	private final char statusAbbr;

	private ReleaseStatus(char statusAbbreviation) {
		this.statusAbbr = statusAbbreviation;
	}

	public char getStatusAbbr() {
		return statusAbbr;
	}

	public static ReleaseStatus valueOf(char status) {

		switch (status) {
		case 'r':
			return RELEASE;
		case 'b':
			return BETA;
		case 'a':
			return ALPHA;
		default:
			throw new IllegalArgumentException(status + " is no valid status");
		}
	}

	public static ReleaseStatus valueOfIgnoreCase(String status) {
		try {
			return valueOf(status.toUpperCase());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
}
