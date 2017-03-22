/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.example.tests;

import com.example.dataobjects.Notifications;
import com.example.syncher.BaseSyncher;
import com.example.syncher.NotificationSyncher;


/**
 * @author abdul
 * @version 1.0, 21 Dec 2015
 */
public class NotificationSyncherTest extends BaseSyncherTest {

    public void testGetAllNotificationsHAPPYFlow() throws Exception {

        Notifications allNotificatons;

        allNotificatons = sut.getAllNotificatons();
        assertEquals(1, allNotificatons.getNotifications().size());
    }
    //------------------------------------------------------------------------------------------------------------------
    // Setup
    //------------------------------------------------------------------------------------------------------------------
    NotificationSyncher sut;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        sut = new NotificationSyncher();
        BaseSyncher.setAccessToken("035588cd58f947a396fdc58d401c7ce5");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        sut = null;
    }
}