/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.example.tests;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import com.example.dataobjects.*;
import com.example.syncher.BaseSyncher;
import com.example.syncher.InvitationSyncher;


/**
 * @author CeroneFedora
 * @version 1.0, Dec 18, 2015
 */
public class InvitationSyncherTest extends TestCase {

    public void testInvitationTestHAPPYFlow() throws Exception {
        if (createNew) {
            // Setup fixture
            int eventId = 61;
            List<String> mobileNumbers = getDefaultNumbers();
            List<String> groupIds = new ArrayList<String>();
            groupIds.add("1");
            groupIds.add("2");
            // Exercise SUT
            ServerResponse response = sut.createInvitation(eventId, mobileNumbers, groupIds);
            String actual = response.getStatus();
            // Verify outcome
            assertNotNull(actual);
        }
    }

    //
    private List<String> getDefaultNumbers() {
        List<String> mobileNUmbers = new ArrayList<String>();
        mobileNUmbers.add("917569887778");
        //mobileNUmbers.add("918686514735");
        return mobileNUmbers;
    }

    //
    //    public void testGetMyInvitationsHAPPYFlow() throws Exception {
    //        // Setup fixture
    //        // Exercise SUT
    //        List<Invitation> listOfEvents = sut.getMyInvitations();
    //        // Verify outcome
    //        assertEquals(39, listOfEvents.get(0).getEventId());
    //    }
    //
    //    public void testGetInvitationStatusHAPPYFlow() throws Exception {
    //        // Setup fixture
    //        int eventId = 39;
    //        boolean accepted = false;
    //        // Exercise SUT
    //        String response = sut.getInvitationStatus(eventId, accepted);
    //        // Verify outcome
    //        assertNotNull(response);
    //    }
    //
    //    public void testInviteesTestHAPPYFlow() throws Exception {
    //        // Setup fixture
    //        int eventId = 39;
    //        // Exercise SUT
    //        List<Invitee> invitees = sut.getInvitees(eventId, "");
    //        // String actual = response.getStatus();
    //        // Verify outcome
    //        assertNotNull(invitees);
    //        assertEquals(1, invitees.get(0).getMobileNumber());
    //    }
    //------------------------------------------------------------------------------------------------------------------
    // Setup
    //------------------------------------------------------------------------------------------------------------------
    InvitationSyncher sut;
    boolean createNew = true;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        sut = new InvitationSyncher();
        BaseSyncher.setAccessToken("9049d4b65472891215997844d");
        //9049d4b65472891215997844d Adarsh 
        //b39b545dd564d649528233f2044f Adarsh testing
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        sut = null;
    }
}