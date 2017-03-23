/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.example.syncher;

import com.example.dataobjects.Eventstatistics;
import com.example.dataobjects.Invitation;
import com.example.dataobjects.Invitee;
import com.example.dataobjects.ServerResponse;
import com.example.dataobjects.User;
import com.example.utills.HTTPUtils;
import com.example.utills.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.key;


/**
 * @author CeroneFedora
 * @version 1.0, Dec 18, 2015
 */
public class InvitationSyncher extends BaseSyncher {

    public ServerResponse createNewInvitation(int eventId, List<User> contactList, List<String> groupIds) {
        ServerResponse response = new ServerResponse();
        try {
            JSONArray array = new JSONArray();
            JSONArray groupArray = new JSONArray();
            JSONObject object = new JSONObject();
            for (User user : contactList) {
                JSONObject contactObject = new JSONObject();
                contactObject.put("mobile_number", user.getPhoneNumber());
                contactObject.put("event_admin", user.isAdmin());
                array.put(contactObject);
            }
            for (String groupId : groupIds) {
                groupArray.put(groupId);
            }
            object.put("event_id", eventId);
            object.put("participant_mobile_numbers", array);
            object.put("group_ids", groupArray);
            String dataFromServer = HTTPUtils.getDataFromServer(BASE_URL + "events/create_new_invitations.json?event_id="+eventId, "POST", object.toString(), true);
            if (StringUtils.isJSONValid(dataFromServer)) {
                JSONObject jsonResponse = new JSONObject(dataFromServer);
                if (jsonResponse != null) {
                    response.setStatus(jsonResponse.getString("status"));
                    if (jsonResponse.has("total_invites")) {
                        response.setTotalInvites(jsonResponse.getInt("total_invites"));
                    }
                }
            } else {
                response.setStatus("Invalid response");
            }
        } catch (Exception e) {
            handleException(e);
        }
        return response;
    }

    public List<Invitation> getMyInvitations() {
        List<Invitation> listOfInvitaioons = new ArrayList<Invitation>();
        try {
            String dataFromServer = HTTPUtils.getDataFromServer(BASE_URL + "events/get_my_invitations.json", "GET", true);
            if (StringUtils.isJSONValid(dataFromServer)) {
                JSONObject jsonResponse = new JSONObject(dataFromServer);
                JSONArray jsonArray = (JSONArray) jsonResponse.get("invitation");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Invitation invitation = new Invitation();
                        if (!jsonObject.isNull("is_accepted")) {
                            invitation.setSelected(true);
                            invitation.setAccepted(jsonObject.getBoolean("is_accepted"));
                        }
                        if (!jsonObject.isNull("event_id")) {
                            invitation.setEventId(jsonObject.getInt("event_id"));
                        }
                        invitation.setParticipintId(jsonObject.getInt("participant_id"));
                        listOfInvitaioons.add(invitation);
                    }
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
        return listOfInvitaioons;
    }

    public Eventstatistics getInvitationStatus(int eventId, boolean accepted, String permission) {
        Eventstatistics eventstatistics = new Eventstatistics();
        try {
            String dataFromServer = HTTPUtils.getDataFromServer(BASE_URL + "events/accept_or_reject_invitation.json?event_id=" + eventId + "&accepted=" + accepted+((accepted)?"&permission=" +permission:""), "GET", true);
            if (StringUtils.isJSONValid(dataFromServer)) {
                JSONObject response = new JSONObject(dataFromServer);
                if (response != null) {
                    eventstatistics.setValid(true);
                    eventstatistics.setMessage(response.getString("status"));
                    if(response.has("accepted_count")) {
                        eventstatistics.setAcceptCount(response.getInt("accepted_count"));
                        eventstatistics.setTotalInviteesCount(response.getInt("invitees_count"));
                        eventstatistics.setRejectCount(response.getInt("rejected_count"));
                        eventstatistics.setCheckedInCount(response.getInt("check_in_count"));
                    }
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
        return eventstatistics;
    }

    public List<Invitee> getInvitees(int eventId, String key) {
        List<Invitee> listOfInvitees = new ArrayList<Invitee>();
        try {
            String statusKey = (key.length() > 0) ? "&status=" + key : "";
            String dataFromServer = HTTPUtils.getDataFromServer(BASE_URL + "events/event_invitations.json?id=" + eventId + statusKey, "GET", true);
            if (StringUtils.isJSONValid(dataFromServer)) {
                JSONObject jsonResponse = new JSONObject(dataFromServer);
                JSONArray jsonArray = jsonResponse.getJSONArray("participants_list");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Invitee invitee = new Invitee();
                        if (jsonObject.isNull("is_accepted")) {
                            invitee.setAccepted(false);
                        } else {
                            invitee.setAccepted(jsonObject.getBoolean("is_accepted"));
                        }
                        if (jsonObject.has("distance"))
                            invitee.setDistance("" + jsonObject.getDouble("distance"));
                        if (jsonObject.has("update_at"))
                            invitee.setStatus(jsonObject.getString("update_at"));
                        if (jsonObject.has("user_id"))
                            invitee.setInviteeId(jsonObject.getInt("user_id"));
                        invitee.setMobileNumber(jsonObject.getString("mobile"));
                        invitee.setInviteeName(jsonObject.getString("name"));
                        invitee.setMobileNumber(jsonObject.getString("mobile"));
                        invitee.setEmail(jsonObject.getString("email"));
                        invitee.setImage(jsonObject.getString("img_url"));
                        listOfInvitees.add(invitee);
                    }
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
        return listOfInvitees;
    }
}