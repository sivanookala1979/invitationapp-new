/*
 * (c) Copyright 2001-2016 COMIT AG
 * All Rights Reserved.
 */
package com.example.dataobjects;

import java.util.Date;

/**
 * @author sivanookala
 * @version 1.0, 05-Mar-2016
 */
public class ChatMessage {

    int fromID;
    String message;
    Date date;
    int id;
    String userName;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getFromID() {
        return fromID;
    }

    public void setFromID(int fromID) {
        this.fromID = fromID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
    
}
