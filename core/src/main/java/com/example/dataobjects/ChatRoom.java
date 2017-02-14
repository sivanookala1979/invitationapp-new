/*
 * (c) Copyright 2001-2016 COMIT AG
 * All Rights Reserved.
 */
package com.example.dataobjects;

/**
 * @author sivanookala
 * @version 1.0, 05-Mar-2016
 */
public class ChatRoom {

    int otherUserId;
    String otherUserName;
    int unreadMessagesCount;
    String image;
    String latestMsgDateTime;
    String latestMessage;
    int chatRoomId;

    public int getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(int otherUserId) {
        this.otherUserId = otherUserId;
    }

    public String getOtherUserName() {
        return otherUserName;
    }

    public void setOtherUserName(String otherUserName) {
        this.otherUserName = otherUserName;
    }

    public int getUnreadMessagesCount() {
        return unreadMessagesCount;
    }

    public void setUnreadMessagesCount(int unreadMessagesCount) {
        this.unreadMessagesCount = unreadMessagesCount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLatestMsgDateTime() {
        return latestMsgDateTime;
    }

    public void setLatestMsgDateTime(String latestMsgDateTime) {
        this.latestMsgDateTime = latestMsgDateTime;
    }

    public String getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(String latestMessage) {
        this.latestMessage = latestMessage;
    }

    public int getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(int chatRoomId) {
        this.chatRoomId = chatRoomId;
    }
}
