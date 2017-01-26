/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.example.syncher;

import android.util.Log;

import com.example.dataobjects.*;
import com.example.utills.HTTPUtils;

import org.json.JSONObject;


/**
 * @author CeroneFedora
 * @version 1.0, Dec 18, 2015
 */
public class UserSyncher extends BaseSyncher {

    public ServerResponse createUser(User user) {
        ServerResponse response = new ServerResponse();
        try {
            String userStatusResponse = HTTPUtils.getDataFromServer(BASE_URL + "users/create_user.json?user_name=" + user.getUserName().replace(" ", "%20") + "&phone_number=" + user.getPhoneNumber(), "GET", false);
            JSONObject object = new JSONObject(userStatusResponse);
            if (object.has("id")) {
                response.setId(object.getInt("id"));
                response.setStatus(object.getString("status"));
                response.setToken(object.getString("access_token"));
            } else {
                response.setStatus(object.getString("status"));
            }
        } catch (Exception e) {
            handleException(e);
        }
        return response;
    }

    public ServerResponse getAccesToken(String mobileNumber) {
        ServerResponse response = new ServerResponse();
        try {
            JSONObject object = new JSONObject(HTTPUtils.getDataFromServer(BASE_URL + "users/login.json?phone_number=" + mobileNumber, "GET", false));
            if (object != null) {
                if (object.has("access_token")) {
                    response.setToken(object.getString("access_token"));
                    response.setId(object.getInt("id"));
                } else if (object.has("status"))
                    response.setStatus(object.getString("status"));
            }
        } catch (Exception e) {
            handleException(e);
        }
        return response;
    }

    public ServerResponse getDistanceFromEvent(int eventId) {
        ServerResponse response = new ServerResponse();
        try {
            JSONObject object = new JSONObject(HTTPUtils.getDataFromServer(BASE_URL + "events/get_distance_from_event.json?event_id=" + eventId, "GET", true));
            if (object != null) {
                String status = object.getString("status");
                response.setStatus(status);
                if (status.equals("Success"))
                    response.setDistance("" + object.getDouble("distance"));
            }
        } catch (Exception e) {
            handleException(e);
        }
        return response;
    }

    public ServerResponse updateInviteeCheckInStaus(int eventId, String checkInStatus) {
        ServerResponse response = new ServerResponse();
        try {
            JSONObject object = new JSONObject(HTTPUtils.getDataFromServer(BASE_URL + "events/invitee_check_in_Status.json?id=" + eventId + "&status=" + checkInStatus, "GET", true));
            if (object != null) {
                String status = object.getString("status");
                response.setStatus(status);
            }
        } catch (Exception e) {
            handleException(e);
        }
        return response;
    }
}
