package com.example.syncher;

import com.example.dataobjects.ChatMessage;
import com.example.dataobjects.SaveResult;
import com.example.helper.FromJSONConvertor;
import com.example.helper.JSONHTTPUtils;
import com.example.utills.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import static com.example.utills.StringUtils.StringToDate;

/**
 * Created by adarsht on 15/02/17.
 */

public class SupportChatSyncher extends BaseSyncher {

    public SaveResult sendMessage(String message) {
        return new JSONHTTPUtils().simpleGet("chat_rooms/post_chat_message.json?message=" + StringUtils.enc(message));
    }

    public List<ChatMessage> getSupportChat() {
        return new JSONHTTPUtils<ChatMessage>().list("chat_rooms/get_support_chat_messages.json", new FromJSONConvertor<ChatMessage>() {

            @Override
            public ChatMessage fromJSON(JSONObject jsonObject) throws JSONException {
                ChatMessage message = new ChatMessage();
                message.setMessage(jsonObject.getString("message"));
                message.setFromID(jsonObject.getInt("from_id"));
                Date updatedAt = StringUtils.chatStringToDate(StringUtils.getFormatedDateFromServerFormatedDate(jsonObject.getString("updated_at")));
                message.setDate(updatedAt);
                return message;
            }
        });
    }
}