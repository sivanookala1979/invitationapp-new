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
    private String image;
    private int distance;

    public MyMarker(String inviteeeName, String contactNumber, String image, Double latitude, Double longitude, int distance) {
        this.inviteeeName = inviteeeName;
        this.contactNumber = contactNumber;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
