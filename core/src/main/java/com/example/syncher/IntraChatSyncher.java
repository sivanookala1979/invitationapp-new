package com.example.syncher;

import com.example.dataobjects.ChatMessage;
import com.example.dataobjects.ChatRoom;
import com.example.dataobjects.SaveResult;
import com.example.helper.FromJSONConvertor;
import com.example.helper.JSONHTTPUtils;
import com.example.utills.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import static com.example.utills.StringUtils.enc;

/**
 * Created by adarsht on 15/02/17.
 */

public class IntraChatSyncher extends BaseSyncher {

    public SaveResult sendMessage(int user_id, String message) {
        return new JSONHTTPUtils().simpleGet("chat_rooms/post_inter_chat_message.json?other_id=" + user_id + "&message=" + enc(message));
    }
    public SaveResult sendMessageToGroup(int groupOrEventId, String message) {
        return new JSONHTTPUtils().simpleGet("chat_rooms/post_inter_chat_message.json?other_id=" + groupOrEventId + "&message=" + enc(message)+"&is_group=true");
    }

    public List<ChatMessage> getIntraChat(int user_id) {
        return new JSONHTTPUtils<ChatMessage>().list("chat_rooms/get_inter_chat_messages.json?other_id=" + user_id, new FromJSONConvertor<ChatMessage>() {

            public ChatMessage fromJSON(JSONObject jsonObject) throws JSONException {
                ChatMessage message = new ChatMessage();
                message.setMessage(jsonObject.getString("message"));
                message.setFromID(jsonObject.getInt("from_id"));
                if(jsonObject.has("id")) {
                    message.setId(jsonObject.getInt("id"));
                }
                message.setUserName(jsonObject.getString("user_name"));

                Date updatedAt = StringUtils.chatStringToDate(StringUtils.getFormatedDateFromServerFormatedDate(jsonObject.getString("updated_at")));
                message.setDate(updatedAt);
                return message;
            }
        });
    }
    public List<ChatMessage> getGroupChat(int user_id) {
        return new JSONHTTPUtils<ChatMessage>().list("chat_rooms/get_inter_chat_messages.json?other_id=" + user_id+"&is_group=true", new FromJSONConvertor<ChatMessage>() {

            public ChatMessage fromJSON(JSONObject jsonObject) throws JSONException {
                ChatMessage message = new ChatMessage();
                message.setMessage(jsonObject.getString("message"));
                message.setFromID(jsonObject.getInt("from_id"));
                if(jsonObject.has("id")) {
                    message.setId(jsonObject.getInt("id"));
                }
                message.setUserName(jsonObject.getString("user_name"));
                Date updatedAt = StringUtils.chatStringToDate(StringUtils.getFormatedDateFromServerFormatedDate(jsonObject.getString("updated_at")));
                message.setDate(updatedAt);
                return message;
            }
        });
    }

    public List<ChatRoom> getChats() {
        return new JSONHTTPUtils<ChatRoom>().list("chat_rooms/get_chats.json", new FromJSONConvertor<ChatRoom>() {

            @Override
            public ChatRoom fromJSON(JSONObject jsonObject) throws JSONException {
                ChatRoom chatRoom = new ChatRoom();
                chatRoom.setOtherUserId(jsonObject.getInt("other_user_id"));
                chatRoom.setOtherUserName(jsonObject.getString("other_user_name"));
                chatRoom.setUnreadMessagesCount(jsonObject.getInt("unread_messages_count"));
                if (jsonObject.has("image_url"))
                    chatRoom.setImage(jsonObject.getString("image_url"));
                if (jsonObject.has("latest_msg_date_time"))
                    chatRoom.setLatestMsgDateTime(jsonObject.getString("latest_msg_date_time"));
                if (jsonObject.has("latest_message"))
                    chatRoom.setLatestMessage(jsonObject.getString("latest_message"));
                if (jsonObject.has("chat_room_id"))
                    chatRoom.setChatRoomId(jsonObject.getInt("chat_room_id"));
                return chatRoom;
            }
        });
    }
}