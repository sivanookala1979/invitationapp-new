/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.example.exception;

/**
 * @author sivanookala
 * @version 1.0, 16-Dec-2015
 */
public class InvtAppException extends RuntimeException {

    private static final long serialVersionUID = -4103999309724194764L;
    Exception rootCause;

    public InvtAppException(Exception rootCause) {
        super();
        this.rootCause = rootCause;
    }

    public Exception getRootCause() {
        return rootCause;
    }
}
