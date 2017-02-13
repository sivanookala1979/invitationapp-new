/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.example.tests;

import com.example.dataobjects.ServerResponse;
import com.example.dataobjects.User;
import com.example.syncher.BaseSyncher;
import com.example.syncher.InvitationSyncher;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;


/**
 * @author CeroneFedora
 * @version 1.0, Dec 18, 2015
 */
public class InvitationSyncherTest extends TestCase {

//    public void testInvitationTestHAPPYFlow() throws Exception {
//        if (createNew) {
//            // Setup fixture
//            int eventId = 61;
//            List<String> mobileNumbers = getDefaultNumbers();
//            List<String> groupIds = new ArrayList<String>();
//            groupIds.add("1");
//            groupIds.add("2");
//            // Exercise SUT
//            ServerResponse response = sut.createInvitation(eventId, mobileNumbers, groupIds);
//            String actual = response.getStatus();
//            // Verify outcome
//            assertNotNull(actual);
//        }
//    }

    public void testNewInvitationTestHAPPYFlow() throws Exception {
        if (createNew) {
            // Setup fixture
            int eventId = 28;
            List<User> mobileNumbers = getDefaultNumbers();
            List<String> groupIds = new ArrayList<String>();
            // Exercise SUT
            ServerResponse response = sut.createNewInvitation(eventId, mobileNumbers, groupIds);
            String actual = response.getStatus();
            // Verify outcome
            assertNotNull(actual);
            assertEquals("Success", response.getStatus());
            assertEquals(12, response.getTotalInvites());
        }
    }

    private List<User> getDefaultNumbers() {
        List<User> mobileNUmbers = new ArrayList<User>();
        User status1 = new User();
        status1.setPhoneNumber("9000451544");
        status1.setAdmin(true);
        mobileNUmbers.add(status1);
        User status2 = new User();
        status2.setPhoneNumber("9848223344");
        status2.setAdmin(false);
        mobileNUmbers.add(status2);
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
        BaseSyncher.setAccessToken("db5d5012964d3bf01f79905379");
        //9049d4b65472891215997844d Adarsh 
        //b39b545dd564d649528233f2044f Adarsh testing
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        sut = null;
    }
}