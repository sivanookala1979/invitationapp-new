package com.example.tests;

import com.example.dataobjects.ChatMessage;
import com.example.dataobjects.ChatRoom;
import com.example.dataobjects.SaveResult;
import com.example.dataobjects.ServerResponse;
import com.example.dataobjects.User;
import com.example.syncher.BaseSyncher;
import com.example.syncher.IntraChatSyncher;
import com.example.syncher.UserSyncher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adarsht on 15/02/17.
 */

public class IntraChatSyncherTest extends BaseSyncherTest {
    public void testNewInvitationTestHAPPYFlow() throws Exception {
        List<ChatRoom> chats = sut.getChats();
        assertEquals(1,chats.size());
    }
    public void testGetIntraChatTestHAPPYFlow() throws Exception {
        List<ChatMessage> intraChat = sut.getIntraChat(129);
        assertEquals(8,intraChat.size());
    }
    public void testsendMessageTestHAPPYFlow() throws Exception {
        SaveResult saveResult = sut.sendMessage(129, "129 Test 2");
        assertEquals(true,saveResult.isSuccess());
    }

    IntraChatSyncher sut;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        sut = new IntraChatSyncher();
        BaseSyncher.setAccessToken("a1d7d60e01da452fb31d7d372b02a740");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        sut = null;

    }
}
