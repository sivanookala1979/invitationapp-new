/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.example.tests;

import junit.framework.TestCase;

import java.util.List;

import com.example.dataobjects.*;
import com.example.syncher.BaseSyncher;
import com.example.syncher.LocationSyncher;


/**
 * @author abdul
 * @version 1.0, 21 Dec 2015
 */
public class LocationSyncherTest extends BaseSyncherTest {

    //    public void t1estForPostUserLocationHAPPYFlow() throws Exception {
    //        // Setup fixture
    //        UserLocation location = getDefaultLocation(14.912731, 79.988869, "2015-12-30 11:23:23");
    //        // Exercise SUT
    //        ServerResponse response = sut.postUserLocation(location);
    //        String actual = response.getStatus();
    //        // Verify outcome
    //        assertEquals("Success", actual);
    //    }

    public void testCheckInviteesLocationHAPPYFlow() throws Exception {
        // Setup fixture
        int eventId = 8;
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
        BaseSyncher.setAccessToken("9b593f9dd9f7478db4f62803b492d8");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        sut = null;
    }
}