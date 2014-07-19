package org.bitbucket.keiki.jcurse.data;

public enum ReleaseStatus {
    ALPHA('a',"alpha"), BETA('b', "beta"), RELEASE('r', "release");
    
    private final char statusAbbr;
    private final String status;
    
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
        default:
            throw new IllegalArgumentException(status + " is no valid status");
        }
    }
    
    public static ReleaseStatus valueOfIgnoreCase(String status) {
        if (ALPHA.getStatus().equalsIgnoreCase(status)) {
            return ALPHA;
        }
        if (BETA.getStatus().equalsIgnoreCase(status)) {
            return BETA;
        }
        if (RELEASE.getStatus().equalsIgnoreCase(status)) {
            return RELEASE;
        }
        return null;
    }
}
