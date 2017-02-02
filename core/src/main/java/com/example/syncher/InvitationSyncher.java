/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.example.syncher;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.dataobjects.*;
import com.example.utills.HTTPUtils;
import com.example.utills.StringUtils;


/**
 * @author CeroneFedora
 * @version 1.0, Dec 18, 2015
 */
public class InvitationSyncher extends BaseSyncher {

    public ServerResponse createInvitation(int eventId, List<String> mobileNumbers, List<String> groupIds) {
        ServerResponse response = new ServerResponse();
        try {
            JSONArray array = new JSONArray();
            JSONArray groupArray = new JSONArray();
            JSONObject object = new JSONObject();
            for (String number : mobileNumbers) {
                array.put(number);
            }
            for (String groupId : groupIds) {
                groupArray.put(groupId);
            }
            object.put("event_id", eventId);
            object.put("participant_mobile_numbers", array);
            object.put("group_ids", groupArray);
            String dataFromServer = HTTPUtils.getDataFromServer(BASE_URL + "events/create_invitations.json", "POST", object.toString(), true);
            if (StringUtils.isJSONValid(dataFromServer)) {
                JSONObject jsonResponse = new JSONObject(dataFromServer);
                if (jsonResponse != null) {
                    response.setStatus(jsonResponse.getString("status"));
                    if (jsonResponse.has("total_invites")) {
                        response.setId(Integer.parseInt(jsonResponse.getString("total_invites")));
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

    public Eventstatistics getInvitationStatus(int eventId, boolean accepted) {
        Eventstatistics eventstatistics = new Eventstatistics();
        try {
            String dataFromServer = HTTPUtils.getDataFromServer(BASE_URL + "events/accept_or_reject_invitation.json?event_id=" + eventId + "&accepted=" + accepted, "GET", true);
            if (StringUtils.isJSONValid(dataFromServer)) {
                JSONObject response = new JSONObject(dataFromServer);
                if (response != null) {
                    eventstatistics.setValid(true);
                    eventstatistics.setMessage(response.getString("status"));
                    eventstatistics.setAcceptCount(response.getInt("accepted_count"));
                    eventstatistics.setTotalInviteesCount(response.getInt("invitees_count"));
                    eventstatistics.setRejectCount(response.getInt("rejected_count"));
                    eventstatistics.setCheckedInCount(response.getInt("check_in_count"));
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
                        invitee.setMobileNumber(jsonObject.getString("mobile"));
                        invitee.setInviteeName(jsonObject.getString("name"));
                        invitee.setMobileNumber(jsonObject.getString("mobile"));
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