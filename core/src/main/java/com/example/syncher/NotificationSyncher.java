package com.example.syncher;

import com.example.dataobjects.Notification;
import com.example.dataobjects.Notifications;
import com.example.dataobjects.SaveResult;
import com.example.utills.HTTPUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peekay on 14/3/17.
 */

public class NotificationSyncher extends BaseSyncher {

    public Notifications getAllNotificatons() {

        Notifications myNotification = new Notifications();
        List<Notification> allNotificatons = new ArrayList<Notification>();
        try {
            String jsonResponse = HTTPUtils.getDataFromServer(BASE_URL + "currencies/my_notifications.json", "GET", true);
            JSONObject responseJSON = new JSONObject(jsonResponse);
            if (responseJSON.has("notifications")) {
                JSONArray jsonArray = responseJSON.getJSONArray("notifications");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Notification notification = new Notification();
                    notification.setId(jsonObject.getInt("id"));
                    notification.setUserId(jsonObject.getInt("user_id"));
                    notification.setOrderId(jsonObject.getInt("event_id"));
                    notification.setContent(jsonObject.getString("content"));
                    notification.setCategoryName(jsonObject.getString("notification_type"));
                    notification.setIsnotified(jsonObject.getBoolean("notified"));
                    allNotificatons.add(notification);
                }
            }
            myNotification.setNotifications(allNotificatons);
            if(responseJSON.has("pending_notifications")){
                myNotification.setPendingNotifications(responseJSON.getInt("pending_notifications"));
            }
        } catch (Exception e) {
            handleException(e);
        }
        return myNotification;
    }

    public SaveResult clearNotifications(){
        SaveResult result = new SaveResult();
        try{
            String response = HTTPUtils.getDataFromServer(BASE_URL + "currencies/clear_notifications.json", "GET");
            JSONObject responseJSON = new JSONObject(response);
            if (responseJSON.has("status")) {
                result.setStatus(responseJSON.getString("status"));
                result.setSuccess(true);
            } else {
                result.setSuccess(false);
            }
        }
        catch (Exception ex) {
            handleException(ex);
        }
        return result;
    }

}
