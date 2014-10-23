package org.bitbucket.keiki.jcurse.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ReleaseStatus {
    ALPHA('a'), BETA('b'), RELEASE('r');

    private static final Logger LOG = LoggerFactory.getLogger(ReleaseStatus.class);
    
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
            LOG.debug("Error while parsing release status.", e);
            return null;
        }
    }
}
