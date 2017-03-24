/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.example.dataobjects;

/**
 * @author CeroneFedora
 * @version 1.0, Dec 18, 2015
 */
public class ServerResponse {

    int id;
    String status;
    String token;
    String distance;
    int totalInvites;
    boolean profileGiven;
    boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isProfileGiven() {
        return profileGiven;
    }

    public void setProfileGiven(boolean profileGiven) {
        this.profileGiven = profileGiven;
    }

    public int getTotalInvites() {
        return totalInvites;
    }

    public void setTotalInvites(int totalInvites) {
        this.totalInvites = totalInvites;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
