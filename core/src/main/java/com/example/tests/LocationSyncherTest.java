/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.example.tests;

import com.example.dataobjects.MyMarker;
import com.example.dataobjects.ServerResponse;
import com.example.dataobjects.UserLocation;
import com.example.syncher.BaseSyncher;
import com.example.syncher.LocationSyncher;

import java.util.List;


/**
 * @author abdul
 * @version 1.0, 21 Dec 2015
 */
public class LocationSyncherTest extends BaseSyncherTest {

        public void testForPostUserLocationHAPPYFlow() throws Exception {
            // Setup fixture
            UserLocation location = getDefaultLocation(14.4426, 79.9865, "2017-04-10 01:11:23");
            // Exercise SUT
            ServerResponse response = sut.postUserLocation(location);
            String actual = response.getStatus();
            // Verify outcome
            assertEquals("Success", actual);
        }

    public void testCheckInviteesLocationHAPPYFlow() throws Exception {
        // Setup fixture
        int eventId = 50;
        // Exercise SUT
        List<MyMarker> locations = sut.checkInviteesLocation(eventId);
        // Verify outcome
        assertEquals(14.9026017, locations.get(0).getLatitude());
        assertEquals("Not found", locations.get(0).getInviteeName());
    }

    //------------------------------------------------------------------------------------------------------------------
    // Setup
    //------------------------------------------------------------------------------------------------------------------
    LocationSyncher sut;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        sut = new LocationSyncher();
        BaseSyncher.setAccessToken("47b9625b4f806f82d5bb04");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        sut = null;
    }
}