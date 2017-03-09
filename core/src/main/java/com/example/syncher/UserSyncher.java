/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.example.syncher;

import com.example.dataobjects.SaveResult;
import com.example.dataobjects.ServerResponse;
import com.example.dataobjects.User;
import com.example.utills.HTTPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


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

    public String getOtp(String mobileNumber) {
        String status = "";
        try {
            String object = HTTPUtils.getDataFromServer(BASE_URL + "/users/log_in_with_mobile.json?mobile_number=" + mobileNumber, "GET", true);
            JSONObject jsonObject = new JSONObject(object);
            if (jsonObject.has("status"))
                status = jsonObject.getString("status");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;
    }

    public ServerResponse getSignInWithMobileAndOtp(String mobileNumber, String otp, String name) {
        ServerResponse response = new ServerResponse();
        try {
            String object = HTTPUtils.getDataFromServer(BASE_URL + "/users/register_with_mobile.json?mobile_number="+mobileNumber+"&otp="+otp+"&user_name="+name, "GET", true);
            JSONObject jsonObject = new JSONObject(object);
            if (jsonObject.has("user_id"))
                response.setId(jsonObject.getInt("user_id"));
            if (jsonObject.has("access_token"))
                response.setToken(jsonObject.getString("access_token"));
            if (jsonObject.has("is_profile_given"))
                response.setProfileGiven(jsonObject.getBoolean("is_profile_given"));
            else
            if (jsonObject.has("status"))
                response.setStatus(jsonObject.getString("status"));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }

    public User getUserdetails() {
        User user = new User();
        try {
            String object = HTTPUtils.getDataFromServer(BASE_URL + "/users/get_user_details", "GET", true);
            JSONObject jsonObject = new JSONObject(object);
            if (jsonObject.has("user_name"))
                user.setUserName(jsonObject.getString("user_name"));
            if (jsonObject.has("phone_number"))
                user.setPhoneNumber(jsonObject.getString("phone_number"));
            if (jsonObject.has("status"))
                user.setStatus(jsonObject.getString("status"));
            if (jsonObject.has("image_url"))
                user.setImage(jsonObject.getString("image_url"));
            if (jsonObject.has("is_profile_given"))
                user.setProfileGiven(jsonObject.getBoolean("is_profile_given"));
            if (jsonObject.has("is_app_login"))
                user.setAppLogin(jsonObject.getBoolean("is_app_login"));
            else
            if (jsonObject.has("error_message"))
                user.setErrorMessage(jsonObject.getString("error_message"));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User updateUserDetails(User user) {
        User userDetails = new User();
        try {
            JSONObject userObject = new JSONObject();
            userObject.put("user_name", user.getUserName());
            userObject.put("phone_number", user.getPhoneNumber());
            userObject.put("status", user.getStatus());
            userObject.put("image", user.getImage());
            String object = HTTPUtils.getDataFromServer(BASE_URL + "/users/update_user_details", "POST", userObject.toString(), true);
            JSONObject jsonObject = new JSONObject(object);
            if (jsonObject.has("user_name"))
                userDetails.setUserName(jsonObject.getString("user_name"));
            if (jsonObject.has("phone_number"))
                userDetails.setPhoneNumber(jsonObject.getString("phone_number"));
            if (jsonObject.has("status"))
                userDetails.setStatus(jsonObject.getString("status"));
            if (jsonObject.has("image_url"))
                userDetails.setImage(jsonObject.getString("image_url"));
            if (jsonObject.has("is_profile_given"))
                userDetails.setProfileGiven(jsonObject.getBoolean("is_profile_given"));
            if (jsonObject.has("is_app_login"))
                userDetails.setAppLogin(jsonObject.getBoolean("is_app_login"));
            else
            if (jsonObject.has("error_message"))
                userDetails.setErrorMessage(jsonObject.getString("error_message"));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userDetails;
    }
    public void updateGcmCode(String gcmCode) {
        if (BaseSyncher.getAccessToken() != null && !BaseSyncher.getAccessToken().isEmpty()) {
            SaveResult result = new SaveResult();
            try {
                String response = HTTPUtils.getDataFromServer(BASE_URL + "users/store_gcm_code.json?gcm_code=" + gcmCode, "GET");
//            JSONObject responseJSON = new JSONObject(response);
//            if (responseJSON.has("user_id")) {
//                result.setSuccess(true);
//                result.setUserId(responseJSON.getInt("user_id"));
//            } else if (responseJSON.has("error_message")) {
//                result.setSuccess(false);
//                result.setErrorMessage(responseJSON.getString("error_message"));
//            }
            } catch (Exception Ex) {
                handleException(Ex);
            }
            //return result;
        }
    }


}
