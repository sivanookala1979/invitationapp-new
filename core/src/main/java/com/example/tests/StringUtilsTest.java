/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.example.tests;

import com.example.dataobjects.Event;
import com.example.dataobjects.User;
import com.example.utills.StringUtils;

import junit.framework.TestCase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author CeroneFedora
 * @version 1.0, Dec 28, 2015
 */
public class StringUtilsTest extends TestCase {

    public void testDiffBeteenTwoDates() throws Exception {
        // Setup fixture
        // Exercise SUT
        long actual = StringUtils.DiffBetweenTwoDates();
        // Verify outcome
        assertEquals(119, actual);
    }

    public void testisGivenDateGreaterThanOrEqualToCurrentDatre() throws Exception {
        // Setup fixture
        // Exercise SUT
        boolean actual = StringUtils.isGivenDateGreaterThanOrEqualToCurrentDate("2015-12-30 14:55:00");
        // Verify outcome
        assertEquals(false, actual);
    }

    public void testGetFutureEventsHappyFlow() throws Exception {
        // Setup fixture
        List<Event> eventsList = getEventsLis();
        // Exercise SUT
        List<Event> actual = StringUtils.getFutureEvents(eventsList);
        // Verify outcome
        assertEquals(1, actual.size());
    }

    public void testSortEventsBadsedonStartDateHappyFlow() throws Exception {
        // Setup fixture
        List<Event> eventsList = getEventsLis();
        List<Event> actual = StringUtils.getFutureEvents(eventsList);
        // Exercise SUT
        StringUtils.sortEventsBadsedonStartDate(actual);
        // Verify outcome
        assertEquals("2015-12-30T20:10:25Z", actual.get(0).getStartDateTime());
    }

    public List<Event> getEventsLis() {
        List<Event> events = new ArrayList<Event>();
        //        Event event = new Event();
        //        event.setStartDateTime("2014-12-27T01:10:25Z");
        Event event1 = new Event();
        event1.setStartDateTime("2015-12-30T20:10:25Z");
        //        Event event2 = new Event();
        //        event2.setStartDateTime("2015-12-31T01:10:25Z");
        //        Event event3 = new Event();
        //        event3.setStartDateTime("2015-12-30T21:10:25Z");
        //        events.add(event);
        //        events.add(event1);
        //        events.add(event2);
        events.add(event1);
        return events;
    }

    public void testNewDateHappyFlow() throws Exception {
        testDifferentDates("2016-02-04 19:10:25", "2016-02-05 05:40:25");
        testDifferentDates("2016-02-04 23:10:00", "2016-02-05 09:40:00");
        testDifferentDatesBySubstracting("2016-02-05 05:40:25", "2016-02-04 19:10:25");
        testDifferentDatesBySubstracting("2016-02-05 09:40:00", "2016-02-04 23:10:00");
    }

    private void testDifferentDates(String oldDate, String expected) {
        int hours = (60 * 10) + 30;
        String actual = StringUtils.getNewDate(oldDate, hours);
        assertEquals(expected, actual);
    }

    public void testEventDateFormatHappyFlow() throws ParseException {
        String expected = "Tue, 14/03/2017";
        String actual = StringUtils.getEventDateFormat("2017-03-14T16:09:00+05:30");
        assertEquals(expected, actual);
    }

    public void testEventTimeFormatHappyFlow() throws ParseException {
        String expected = "04:09 PM-04:09 PM";
        String actual = StringUtils.getEventTimeFormat("2017-03-14T16:09:00+05:30", "2017-03-14T16:09:00+05:30");
        assertEquals(expected, actual);
    }

    private void testDifferentDatesBySubstracting(String oldDate, String expected) {
        int hours = -(60 * 11) + 30;
        String actual = StringUtils.getNewDate(oldDate, hours);
        assertEquals(expected, actual);
    }

    public void testGetContactsListFromUsersHappyFlow() throws Exception {
        // Setup fixture
        List<User> users = new ArrayList<User>();
        User adarsh = new User();
        adarsh.setPhoneNumber("919963186079");
        User raja = new User();
        raja.setPhoneNumber("917865421");
        User ramu = new User();
        ramu.setPhoneNumber("573213576");
        users.add(adarsh);
        users.add(raja);
        users.add(ramu);
        String expected = "919963186079,917865421,573213576";
        String actual = StringUtils.getContactsListFromUsers(users);
        // Exercise SUT
        // Verify outcome
        assertEquals(expected, actual);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Setup
    //------------------------------------------------------------------------------------------------------------------
    StringUtils sut;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        sut = new StringUtils();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        sut = null;
    }
}