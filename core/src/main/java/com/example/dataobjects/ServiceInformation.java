/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.example.dataobjects;

/**
 * @author adarsh
 * @version 1.0, Dec 31, 2015
 */
public class ServiceInformation {

    boolean event;
    String serviceStartTime;
    String servieEndTime;
    String eventStartTime;
    String enventEndTime;
    String checkInNotificationServiceStartTime;
    boolean showNotification;
    Event eventInfo;

    public boolean isEvent() {
        return event;
    }

    public void setEvent(boolean event) {
        this.event = event;
    }

    public String getServiceStartTime() {
        return serviceStartTime;
    }

    public void setServiceStartTime(String serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }

    public String getServieEndTime() {
        return servieEndTime;
    }

    public void setServieEndTime(String servieEndTime) {
        this.servieEndTime = servieEndTime;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getEnventEndTime() {
        return enventEndTime;
    }

    public void setEnventEndTime(String enventEndTime) {
        this.enventEndTime = enventEndTime;
    }

    public boolean isShowNotification() {
        return showNotification;
    }

    public void setShowNotification(boolean showNotification) {
        this.showNotification = showNotification;
    }

    public String getCheckInNotificationServiceStartTime() {
        return checkInNotificationServiceStartTime;
    }

    public void setCheckInNotificationServiceStartTime(String checkInNotificationServiceStartTime) {
        this.checkInNotificationServiceStartTime = checkInNotificationServiceStartTime;
    }

    public Event getEventInfo() {
        return eventInfo;
    }

    public void setEventInfo(Event eventInfo) {
        this.eventInfo = eventInfo;
    }
}
