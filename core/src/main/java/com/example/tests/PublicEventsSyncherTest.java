package com.example.tests;

import com.example.dataobjects.PublicEvent;
import com.example.dataobjects.SaveResult;
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

    public void testGetTrendingPublicEventsHAPPYFlow() throws Exception {
        // Setup fixture
        // Exercise SUT
        List<PublicEvent> trendingEvents = sut.getTrendingPublicEvents(3);
        // Verify outcome
        assertEquals(1, trendingEvents.size());
    }

    public void testGetFreePublicEventsHAPPYFlow() throws Exception {
        // Setup fixture
        // Exercise SUT
        List<PublicEvent> freeEvents = sut.getFreePublicEvents(1);
        // Verify outcome
        assertEquals(1, freeEvents.size());
    }

    public void testGetWeekendPublicEventsHAPPYFlow() throws Exception {
        // Setup fixture
        // Exercise SUT
        List<PublicEvent> weekendEvents = sut.getWeekendPublicEvents(3);
        // Verify outcome
        assertEquals(1, weekendEvents.size());
    }

    public void testRemoveFavouritesHAPPYFlow() throws Exception {
        // Setup fixture
        // Exercise SUT
        SaveResult saveResult = sut.removeFavourites(4, 1);
        // Verify outcome
        assertEquals("", saveResult.isSuccess());
    }

    public void testAddViewsHAPPYFlow() throws Exception {
        // Setup fixture
        // Exercise SUT
        SaveResult saveResult = sut.addViews(4);
        // Verify outcome
        assertEquals("", saveResult.isSuccess());
    }

    public void testCancelPublicEventsHAPPYFlow() throws Exception {
        // Setup fixture
        // Exercise SUT
        SaveResult saveResult = sut.cancelPublicEvents(4);
        // Verify outcome
        assertEquals("", saveResult.isSuccess());
    }


    //------------------------------------------------------------------------------------------------------------------
    // Setup
    //------------------------------------------------------------------------------------------------------------------
    PublicEventsSyncher sut;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        sut = new PublicEventsSyncher();
        BaseSyncher.setAccessToken("035588cd58f947a396fdc58d401c7ce5");
        //9049d4b65472891215997844d Adarsh
        //b39b545dd564d649528233f2044f adarsh Local testing
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        sut = null;
    }
}
