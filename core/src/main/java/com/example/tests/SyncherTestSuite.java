package com.example.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class SyncherTestSuite extends TestSuite {

    public static Test suite() {
        TestSuite suite = new TestSuite("Syncher Test Suite");
        suite.addTestSuite(EventSyncherTest.class);
        suite.addTestSuite(InvitationSyncherTest.class);
        suite.addTestSuite(UserSyncherTest.class);
        return suite;
    }
}