/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.exception;


/**
 * @author cerone
 * @version 1.0, Apr 14, 2015
 */
public class InvtAppException  extends RuntimeException {

    private static final long serialVersionUID = 7119540893582232697L;
    String customMessage;

    public InvtAppException(String customMessage) {
        super();
        this.customMessage = customMessage;
    }

    public InvtAppException() {
    }

    public String getCustomMessage() {
        return customMessage;
    }
}
