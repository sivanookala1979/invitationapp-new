/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.example.tests;

import junit.framework.TestCase;

import java.util.List;

import com.example.dataobjects.*;
import com.example.syncher.BaseSyncher;
import com.example.syncher.EventSyncher;



/**
 * @author CeroneFedora
 * @version 1.0, Dec 18, 2015
 */
public class EventSyncherTest extends TestCase {

    public void testCreateEventTestHAPPYFlow() throws Exception {
        if (createNew) {
            // Setup fixture
            Event event = getDefaultEvent("Lunch", "2016-02-27 19:10:25", "2016-02-28 23:10:00", "Have fun", 14.902667857153673, 79.99365784227848, "Nellore", true, false, "Success", "adarsh", true, "No theam", "Weekly");
            // Exercise SUT
            ServerResponse response = sut.createEvent(event);
            String actual = response.getId();
            // Verify outcome
            assertNotNull(actual);
        }
    }

    public void testUpdateEventTestHAPPYFlow() throws Exception {
        if (createNew) {
            // Setup fixture
            Event event = getDefaultEvent("Lunch", "2016-02-27 19:10:25", "2016-02-28 23:10:00", "Have fun", 14.902667857153673, 79.99365784227848, "Nellore", true, false, "Success", "adarsh", true, "No theam", "Weekly");
            event.setEventId(39);
            // Exercise SUT
            ServerResponse response = sut.createEvent(event);
            String actual = response.getId();
            // Verify outcome
            assertNotNull(actual);
        }
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

    public void testGetMyEventsHAPPYFlow() throws Exception {
        // Setup fixture
        // Exercise SUT
        List<Event> listOfEvents = sut.getMyEvents();
        // Verify outcome
        assertEquals(1, listOfEvents.size());
    }

    public void testGetMyEventsDataHAPPYFlow() throws Exception {
        // Setup fixture
        // Exercise SUT
        List<Event> listOfEvents = sut.getMyEvents();
        // Verify outcome
        assertEquals(52, listOfEvents.get(0).getEventId());
        assertEquals(true, listOfEvents.get(0).isManualCheckIn());
        assertEquals("test", listOfEvents.get(0).getDescription());
        assertEquals("GPS test 4", listOfEvents.get(0).getName());
    }

    public void testGetMyEventsByIdsHAPPYFlow() throws Exception {
        // Setup fixture
        String eventIds = "52";
        // Exercise SUT
        List<Event> listOfEvents = sut.getMyEventsByIds(eventIds);
        // Verify outcome
        assertEquals(52, listOfEvents.get(0).getEventId());
    }

    public void testDeleteEventHappyFlow() throws Exception {
        // Setup fixture
        int eventId = 50;
        String expected = "50";
        String actual;
        ServerResponse deleteEventResponse = sut.deleteEvent(eventId);
        // Exercise SUT
        actual = deleteEventResponse.getId();
        // Verify outcome
        assertEquals(expected, actual);
    }

    public void testGetAllEventsHappyFlow() throws Exception {
        // Setup fixture
        List<Event> allEvents = sut.getAllEvents();
        int expected = 39;
        // Exercise SUT
        int actual = allEvents.get(0).getEventId();
        // Verify outcome
        assertEquals(expected, actual);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Setup
    //------------------------------------------------------------------------------------------------------------------
    EventSyncher sut;
    boolean createNew = false;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        sut = new EventSyncher();
        BaseSyncher.setAccessToken("9049d4b65472891215997844d");
        //9049d4b65472891215997844d Adarsh
        //b39b545dd564d649528233f2044f adarsh Local testing
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        sut = null;
    }
}