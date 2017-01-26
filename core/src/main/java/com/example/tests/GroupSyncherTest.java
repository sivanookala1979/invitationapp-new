/*
 * (c) Copyright 2001-2016 COMIT AG
 * All Rights Reserved.
 */
package com.example.tests;

import junit.framework.TestCase;

import com.example.dataobjects.*;
import com.example.syncher.BaseSyncher;
import com.example.syncher.GroupSyncher;


/**
 * @author Adarsh.T
 * @version 1.0, 26-Feb-2016
 */
public class GroupSyncherTest extends TestCase {

    //
    //    public void testCreateGroupHappyFlow() throws Exception {
    //        if (ctreateNew) {
    //            // Setup fixture
    //            String expected;
    //            String actual;
    //            // Exercise SUT
    //            ServerResponse createGroup = sut.createGroup(getGroup("Cerone Group", "919949287789,919949257729,919052577844"));
    //            // Verify outcome
    //            //  assertNotNull(createGroup);
    //        }
    //        sut.getGroups();
    //    }
    //
    //    public void testGetDifferentContactsHappyFlow() throws Exception {
    //        //        // Setup fixture
    //        //        String mobileNumbers = "919949287789,900058626,919949257729,919052577844";
    //        //        ShareContact differentiateContacts = sut.getDifferentiateContacts(mobileNumbers);
    //        //        int expected = 2;
    //        //        // Exercise SUT
    //        //        int actual = differentiateContacts.getSmsContacts().size();
    //        //        // Verify outcome
    //        //        assertEquals(expected, actual);
    //        String dataFromServer = HTTPUtils.getDataFromServer(URLEncoder.encode("HTTPUtils.getDataFromServer(BASE_URL + \"/groups/differentiate_contacts.json?mobile_numbers=\" + contacts, \"GET\", true);"), "GET", false);
    //        System.out.println(dataFromServer);
    //    }
    public void testCreateGroupByNameAndEventIdHappyFlow() throws Exception {
        // Setup fixture
        String groupName = "myGroup";
        int eventId = 38;
        ServerResponse response = sut.createGroupByNameAndEventId(groupName, eventId);
        // Exercise SUT
        int actual = response.getId();
        // Verify outcome
        assertTrue(actual>0);
    }

    public Group getGroup(String name, String contacts) {
        Group group = new Group();
        group.setGroupName(name);
        group.setContacts(contacts);
        return group;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Setup
    //------------------------------------------------------------------------------------------------------------------
    GroupSyncher sut;
    boolean ctreateNew = false;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        sut = new GroupSyncher();
        BaseSyncher.setAccessToken("b39b545dd564d649528233f2044f");
        //9049d4b65472891215997844d adarsh
        //b39b545dd564d649528233f2044f ADARSH Local
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        sut = null;
    }
}