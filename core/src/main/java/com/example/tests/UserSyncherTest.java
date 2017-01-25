/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.example.tests;

import junit.framework.TestCase;

import com.example.dataobjects.*;
import com.example.syncher.BaseSyncher;
import com.example.syncher.UserSyncher;
import com.example.utills.StringUtils;


/**
 * @author CeroneFedora
 * @version 1.0, Dec 18, 2015
 */
public class UserSyncherTest extends TestCase {

    //    public void testForExistingUserHAPPYFlow() throws Exception {
    //        // Setup fixture
    //        User user = getDefaultUser("ramki", "22222");
    //        ServerResponse response = sut.createUser(user);
    //        String actual = response.getStatus();
    //        // Verify outcome
    //        assertEquals("User already existed", actual);
    //    }
    //
    //    public void testForNewUserHAPPYFlow() throws Exception {
    //        if (createNew) {
    //            // Setup fixture
    //            User user = getDefaultUser("ramki", "9292004545");
    //            // Exercise SUT
    //            ServerResponse response = sut.createUser(user);
    //            String actual = response.getStatus();
    //            // Verify outcome
    //            assertEquals("Success", actual);
    //        }
    //    }
    //
    //    public void testForCreateUserwithEmptyDataHAPPYFlow() throws Exception {
    //        if (createNew) {
    //            // Setup fixture
    //            User user = getDefaultUser("", "");
    //            // Exercise SUT
    //            ServerResponse response = sut.createUser(user);
    //            String actual = response.getStatus();
    //            // Verify outcome
    //            assertEquals("Success", actual);
    //        }
    //    }
    //
    public void testToGetAccessTokenHAPPYFlow() throws Exception {
        // Setup fixture
        String mobileNumber = "9949257729";
        // Exercise SUT
        ServerResponse response = sut.getAccesToken(mobileNumber);
        String actual = response.getToken();
        // Verify outcome
        assertEquals("hi", actual);
        assertNotNull(actual);
    }

    //    public void testgetDistanceFromEventHAPPYFlow() throws Exception {
    //        // Setup fixture
    //        int eventId = 49;
    //        // Exercise SUT
    //        ServerResponse response = sut.getDistanceFromEvent(eventId);
    //        String actual = response.getDistance();
    //        // Verify outcome
    //        assertEquals("8932.608654294429", actual);
    //    }
    //
    //    public void testupdateInviteeCheckInStausHAPPYFlow() throws Exception {
    //        if (createNew) {
    //            // Setup fixture
    //            int eventId = 27;
    //            String status = "CheckIn";
    //            // Exercise SUT
    //            ServerResponse response = sut.updateInviteeCheckInStaus(eventId, status);
    //            String actual = response.getStatus();
    //            // Verify outcome
    //            assertEquals("Success", actual);
    //        }
    //    }
    public User getDefaultUser(String name, String phoneNumber) {
        User user = new User();
        user.setUserName(name);
        user.setPhoneNumber(phoneNumber);
        return user;
    }

    public void testIsJsonFormat() {
        // Setup fixture
        String json = "[\"status\":\"User already existed\"}";
        // Exercise SUT
        boolean actual = StringUtils.isJSONValid(json);
        // Verify outcome
        assertEquals(false, actual);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Setup
    //------------------------------------------------------------------------------------------------------------------
    UserSyncher sut;
    boolean createNew = false;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        sut = new UserSyncher();
        BaseSyncher.setAccessToken("9b593f9dd9f7478db4f62803b492d8");//ADARSH
        //9b593f9dd9f7478db4f62803b492d8 ADARSH
        //963496fd7054654b2984066d8585 Local testing
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        sut = null;
    }
}