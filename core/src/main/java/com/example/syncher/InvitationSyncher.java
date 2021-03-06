/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.example.syncher;

import com.example.dataobjects.Eventstatistics;
import com.example.dataobjects.Invitation;
import com.example.dataobjects.Invitee;
import com.example.dataobjects.Invitees;
import com.example.dataobjects.ServerResponse;
import com.example.dataobjects.User;
import com.example.utills.HTTPUtils;
import com.example.utills.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


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
                if(jsonResponse.has("invitation")) {
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
                        invitee.setAdmin(jsonObject.getBoolean("is_admin"));
                        invitee.setBlocked(jsonObject.getBoolean("is_blocked"));
                        listOfInvitees.add(invitee);
                    }
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
        return listOfInvitees;
    }

    public ServerResponse makeInviteeAdmin(int eventId, int userId) {
        ServerResponse response = new ServerResponse();
        try {
            String JSONresponse = HTTPUtils.getDataFromServer(BASE_URL + "events/make_invite_as_admin_to_event.json?user_id="+userId+"&event_id="+eventId, "GET", true);
            JSONObject responseObject = new JSONObject(JSONresponse);
            if (responseObject.has("status")) {
                response.setStatus(responseObject.getString("status") );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public ServerResponse blockInvitee(int eventId, int userId) {
        ServerResponse response = new ServerResponse();
        try {
            String JSONresponse = HTTPUtils.getDataFromServer(BASE_URL + "/events/block_invitations.json?event_id="+eventId+"&user_id="+userId, "GET", true);
            JSONObject responseObject = new JSONObject(JSONresponse);
            if (responseObject.has("is_success")) {
                response.setSuccess(responseObject.getBoolean("is_success") );
            }
            if (responseObject.has("status")) {
                response.setStatus(responseObject.getString("status") );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public List<Invitees> getAllInviteesList(int eventId) {
        List<Invitees> listOfAllInvitees = new ArrayList<>();
        try {
            String dataFromServer = HTTPUtils.getDataFromServer(BASE_URL + "events/differentiate_invitees.json?event_id="+eventId, "GET", true);
            if (StringUtils.isJSONValid(dataFromServer)) {
                JSONObject jsonResponse = new JSONObject(dataFromServer);
                JSONArray allInviteesList = jsonResponse.getJSONArray("all_invitees_list");
                if (allInviteesList != null) {
                    for (int i = 0; i < allInviteesList.length(); i++) {
                        JSONObject jsonObject = allInviteesList.getJSONObject(i);
                        Invitees invitees = new Invitees();
                        if (jsonObject.has("title"))
                            invitees.setTitle(jsonObject.getString("title"));
                        if (jsonObject.has("total_invitees"))
                            invitees.setTotalInvitees(jsonObject.getInt("total_invitees"));
                        if (jsonObject.has("invitees_list")){
                            JSONArray inviteesList = jsonObject.getJSONArray("invitees_list");
                            List<Invitee> listOfInvitees = new ArrayList<>();
                            if (inviteesList != null) {
                                for (int j = 0; j < inviteesList.length(); j++) {
                                    JSONObject jsonInvitees = inviteesList.getJSONObject(j);
                                    Invitee invitee = new Invitee();
                                    if (jsonInvitees.has("name"))
                                        invitee.setInviteeName(jsonInvitees.getString("name"));
                                    if (jsonInvitees.has("mobile"))
                                        invitee.setMobileNumber(jsonInvitees.getString("mobile"));
                                    if (jsonInvitees.has("email"))
                                        invitee.setEmail(jsonInvitees.getString("email"));
                                    if (jsonInvitees.has("user_id"))
                                        invitee.setInviteeId(jsonInvitees.getInt("user_id"));
                                    if (jsonInvitees.has("img_url"))
                                        invitee.setImage(jsonInvitees.getString("img_url"));
                                    if (jsonInvitees.has("distance"))
                                        invitee.setDistance(jsonInvitees.getString("distance"));
                                    if (jsonInvitees.has("update_at"))
                                        invitee.setUpdatedAt(jsonInvitees.getString("update_at"));
                                    if (jsonInvitees.has("is_admin"))
                                        invitee.setAdmin(jsonInvitees.getBoolean("is_admin"));
                                    listOfInvitees.add(invitee);
                                }
                            }
                            invitees.setInviteesList(listOfInvitees);
                        }
                        if(invitees.getTotalInvitees()>0) {
                            listOfAllInvitees.add(invitees);
                        }
                    }
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
        return listOfAllInvitees;
    }

}
