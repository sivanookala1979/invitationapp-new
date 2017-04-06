/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.example.tests;


import android.util.Log;

import com.example.dataobjects.ServerResponse;
import com.example.syncher.BaseSyncher;
import com.example.syncher.UserSyncher;


/**
 * @author CeroneFedora
 * @version 1.0, Dec 18, 2015
 */
public class UserSyncherTest extends BaseSyncherTest {

//    public void testCreateUserHappyflow() throws Exception {
//        // Setup fixture
//        String userName = "adarsh";
//        String phoneNumber = "9949257729";
//        String expected = "Success";
//        User user = getDefaultUser(userName, phoneNumber);
//        ServerResponse response = sut.createUser(user);
//        String actual = response.getStatus();
//        // Verify outcome
//        validateCreateUserResponse(expected, response, actual);
//    }
//
//    //
//    public void testDuplicateUserHappyflow() throws Exception {
//        // Setup fixture
//        String userName = "adarsh";
//        String phoneNumber = "9949257729";
//        String expected = "User already existed";
//        User user = getDefaultUser(userName, phoneNumber);
//        // Exercise SUT
//        ServerResponse response = sut.createUser(user);
//        String actual = response.getStatus();
//        // Verify outcome
//        validateCreateUserResponse(expected, response, actual);
//
//    }
//
    public void testToGetAccessTokenHAPPYFlow() throws Exception {
        // Setup fixture
        String mobileNumber = "918008446701";
        String otp = "5555";
        String name = "Srikanth";
        // Exercise SUT
        ServerResponse response = sut.getSignInWithMobileAndOtp(mobileNumber, otp, name);
        String token = response.getToken();
        Log.d("Access Token", token);
        // Verify outcome
        assertNotNull(token);
        assertTrue(response.getId() > 0);
    }
//
//    public void testgetDistanceFromEventHAPPYFlow() throws Exception {
//        // Setup fixture
//        Event event = getDefaultEvent("Lunch", "2017-01-26 19:10:25", "2017-01-28 23:10:00", "Have fun", 14.442599, 79.986456, "Nellore", true, false, "Success", "adarsh", true, "No theam", "Weekly");
//        EventSyncher eventSyncher = new EventSyncher();
//        LocationSyncher locationSyncher = new LocationSyncher();
//        UserLocation location = getDefaultLocation(14.912731, 79.988869, "2015-12-30 11:23:23");
//        // Exercise SUT
//        locationSyncher.postUserLocation(location);
//        ServerResponse eventResponse = eventSyncher.createEvent(event);
//        ServerResponse response = sut.getDistanceFromEvent(eventResponse.getId());
//        String actual = response.getDistance();
//        // Verify outcome
//        assertEquals("52.276937609990604", actual);
//    }
//
//
//    public void testupdateInviteeCheckInStausHAPPYFlow() throws Exception {
//        // Setup fixture
//        String status = "CheckIn";
//        EventSyncher eventSyncher = new EventSyncher();
//        Event event = getDefaultEvent("Lunch", "2017-01-26 19:10:25", "2017-01-28 23:10:00", "Have fun", 14.442599, 79.986456, "Nellore", true, false, "Success", "adarsh", true, "No theam", "Weekly");
//        // Exercise SUT
//        ServerResponse eventResponse = eventSyncher.createEvent(event);
//        ServerResponse response = sut.updateInviteeCheckInStaus(eventResponse.getId(), status);
//        String actual = response.getStatus();
//        // Verify outcome
//        assertEquals("Success", actual);
//    }


    private void validateCreateUserResponse(String expected, ServerResponse response, String actual) {
        if (response.getId() != 0) {
            assertEquals(expected, actual);
            assertNotNull(response.getToken());
            assertTrue(!response.getToken().isEmpty());
        } else {
            expected = "User already existed";
            assertEquals(expected, actual);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // Setup
    //------------------------------------------------------------------------------------------------------------------
    UserSyncher sut;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        sut = new UserSyncher();
        BaseSyncher.setAccessToken("9d089f87274db55d250b65");//ADARSH
        //b1b97ff047047d9308fbb764826 ADARSH
        //963496fd7054654b2984066d8585 Local testing
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        sut = null;
    }
}