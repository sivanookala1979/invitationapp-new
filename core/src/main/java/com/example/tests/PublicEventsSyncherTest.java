package com.example.tests;

import com.example.dataobjects.PublicEvent;
import com.example.syncher.BaseSyncher;
import com.example.syncher.PublicEventsSyncher;

import junit.framework.TestCase;

import java.util.List;

/**
 * Created by peekay on 7/4/17.
 */

public class PublicEventsSyncherTest extends TestCase {

    public void testGetPublicEventsHAPPYFlow() throws Exception {
        // Setup fixture
        // Exercise SUT
        List<PublicEvent> listOfPublicEvents = sut.getPublicEvents();
        // Verify outcome
        assertEquals(1, listOfPublicEvents.size());
    }

//        public void testaddFavouritesHAPPYFlow() throws Exception {
//            // Setup fixture
//            // Exercise SUT
//            SaveResult saveResult = sut.addFavourites();
//            // Verify outcome
//            assertEquals(1, listOfPublicEvents.size());
//        }
//
//            public void testGetMyFavouritesHAPPYFlow() throws Exception {
//                // Setup fixture
//                // Exercise SUT
//                List<PublicEvent> listOfPublicEvents = sut.getPublicEvents();
//                // Verify outcome
//                assertEquals(1, listOfPublicEvents.size());
//            }


    //------------------------------------------------------------------------------------------------------------------
    // Setup
    //------------------------------------------------------------------------------------------------------------------
    PublicEventsSyncher sut;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        sut = new PublicEventsSyncher();
        BaseSyncher.setAccessToken("ced34ed2c58a4c82a167fd0a24e8ef14");
        //9049d4b65472891215997844d Adarsh
        //b39b545dd564d649528233f2044f adarsh Local testing
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        sut = null;
    }
}
