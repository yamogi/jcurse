package org.bitbucket.keiki.jcurse;


public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BusinessException(String message, Exception ex) {
        super(message, ex);
    }
    
    public BusinessException(String message) {
        super(message);
    }
} 
