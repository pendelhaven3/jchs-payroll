package com.jchs.payrollapp.exception;

public class NoMatchingSssContributionTableEntryException extends RuntimeException {

    private static final long serialVersionUID = -8074456251905639551L;

    public NoMatchingSssContributionTableEntryException(String message) {
        super(message);
    }

}
