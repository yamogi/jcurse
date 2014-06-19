package org.bitbucket.keiki.jcurse;

public enum ReleaseStatus {
	STABLE('r'), BETA('b'), ALPHA('a');
	
	char statusAbbr;
	
	private ReleaseStatus(char statusAbbreviation) {
		this.statusAbbr = statusAbbreviation;
	}
}
