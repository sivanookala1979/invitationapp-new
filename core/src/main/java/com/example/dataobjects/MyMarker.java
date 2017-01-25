/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.example.dataobjects;

/**
 * @author adarsh
 * @version 1.0, Dec 31, 2015
 */
public class MyMarker {

    private String inviteeeName = "Not found";
    private String contactNumber = "Not found";
    private Double latitude;
    private Double longitude;
    private String dateTime;

    public MyMarker() {
    }

    public String getInviteeName() {
        return inviteeeName;
    }

    public void setInviteeName(String mLabel) {
        inviteeeName = mLabel;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String icon) {
        contactNumber = icon;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double mLatitude) {
        latitude = mLatitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double mLongitude) {
        longitude = mLongitude;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
