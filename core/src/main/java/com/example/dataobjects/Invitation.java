/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.example.dataobjects;

/**
 * @author Adarsh.T
 * @version 1.0, Dec 18, 2015
 */
public class Invitation {

    int participintId;
    int eventId;
    boolean accepted;
    boolean selected;

    public int getParticipintId() {
        return participintId;
    }

    public void setParticipintId(int participintId) {
        this.participintId = participintId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
