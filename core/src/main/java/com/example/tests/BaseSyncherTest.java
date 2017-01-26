package com.example.tests;

import com.example.dataobjects.Event;
import com.example.dataobjects.User;
import com.example.dataobjects.UserLocation;

import junit.framework.TestCase;

/**
 * Created by adarsht on 26/01/17.
 */

public class BaseSyncherTest extends TestCase {

    public User getDefaultUser(String name, String phoneNumber) {
        User user = new User();
        user.setUserName(name);
        user.setPhoneNumber(phoneNumber);
        return user;
    }

    public Event getDefaultEvent(String eventName, String startDateTime, String endDateTime, String description, double latitude, double longitude, String address, boolean privateType, boolean reaminder, String status, String ownerName, boolean recurring, String theam, String recurringType) {
        Event event = new Event();
        event.setName(eventName);
        event.setStartDateTime(startDateTime);
        event.setEndDateTime(endDateTime);
        event.setDescription(description);
        event.setLatitude(latitude);
        event.setLongitude(longitude);
        event.setAddress(address);
        event.setPrivateType(privateType);
        event.setRemainder(reaminder);
        event.setStatus(status);
        event.setOwnerName(ownerName);
        event.setManualCheckIn(true);
        event.setRecurring(recurring);
        event.setRecurringType(recurringType);
        event.setTheme(theam);
        return event;
    }
    public UserLocation getDefaultLocation(double latitude, double longitude, String dateTime) {
        UserLocation location = new UserLocation();
        location.setDateTime(dateTime);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }
}
