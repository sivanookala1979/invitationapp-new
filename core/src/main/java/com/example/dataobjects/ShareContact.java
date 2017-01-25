/*
 * (c) Copyright 2001-2016 COMIT AG
 * All Rights Reserved.
 */
package com.example.dataobjects;

import java.util.List;

/**
 * @author Adarsh.T
 * @version 1.0, 26-Feb-2016
 */
public class ShareContact {

    List<String> appContacts;
    List<String> smsContacts;

    public List<String> getAppContacts() {
        return appContacts;
    }

    public void setAppContacts(List<String> appContacts) {
        this.appContacts = appContacts;
    }

    public List<String> getSmsContacts() {
        return smsContacts;
    }

    public void setSmsContacts(List<String> smsContacts) {
        this.smsContacts = smsContacts;
    }
}
