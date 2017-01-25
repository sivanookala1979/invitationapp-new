/*
 * (c) Copyright 2001-2016 COMIT AG
 * All Rights Reserved.
 */
package com.example.syncher;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.dataobjects.*;
import com.example.utills.HTTPUtils;


/**
 * @author Adarsh.T
 * @version 1.0, 26-Feb-2016
 */
public class GroupSyncher extends BaseSyncher {

    private static final String SMS_CONTACTS = "sms_contacts";
    private static final String INVITATION_APP_CONTACTS = "invitation_app_contacts";

    public ServerResponse createGroup(Group group) {
        ServerResponse response = new ServerResponse();
        try {
            String dataFromServer = HTTPUtils.getDataFromServer(BASE_URL + "groups/create_group.json?name=" + group.getGroupName().replace(" ", "%20") + "&contact_numbers=" + group.getContacts().replace(" ", "%20"), "GET", true);
            JSONObject groupres = new JSONObject(dataFromServer);
            if (groupres.has("id")) {
                response.setId(groupres.getInt("id") + "");
            }
            response.setStatus(groupres.getString("status"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public ServerResponse createGroupByNameAndEventId(String groupName, int eventId) {
        ServerResponse response = new ServerResponse();
        try {
            String dataFromServer = HTTPUtils.getDataFromServer(BASE_URL + "groups/create_group_by_invites.json?event_id=" + eventId + "&name=" + groupName.replace(" ", "%20"), "GET", true);
            JSONObject groupres = new JSONObject(dataFromServer);
            if (groupres.has("id")) {
                response.setId(groupres.getInt("id") + "");
            }
            response.setStatus(groupres.getString("status"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public List<Group> getGroups() {
        List<Group> groups = new ArrayList<Group>();
        ServerResponse response = null;
        try {
            String dataFromServer = HTTPUtils.getDataFromServer(BASE_URL + "groups/get_my_groups.json", "GET", true);
            JSONObject groupInfo = new JSONObject(dataFromServer);
            if (groupInfo.has("groups")) {
                JSONArray groupsArray = groupInfo.getJSONArray("groups");
                for (int i = 0; i < groupsArray.length(); i++) {
                    Group myGroup = new Group();
                    JSONObject group = groupsArray.getJSONObject(i);
                    myGroup.setOwnerId(group.getInt("owner_id"));
                    myGroup.setContacts(group.getString("contact_numbers"));
                    myGroup.setGroupId(group.getInt("id"));
                    myGroup.setGroupName(group.getString("group_name"));
                    groups.add(myGroup);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return groups;
    }

    public ShareContact getDifferentiateContacts(String contacts) {
        ShareContact contact = new ShareContact();
        List<String> invtAppContacts = new ArrayList<String>();
        List<String> smsContacts = new ArrayList<String>();
        try {
            String dataFromServer = HTTPUtils.getDataFromServer(BASE_URL + "/groups/differentiate_contacts.json?mobile_numbers=" + contacts, "GET", true);
            JSONObject jsonObject = new JSONObject(dataFromServer);
            if (jsonObject.has(INVITATION_APP_CONTACTS)) {
                JSONArray appContactsArray = jsonObject.getJSONArray(INVITATION_APP_CONTACTS);
                for (int i = 0; i < appContactsArray.length(); i++) {
                    invtAppContacts.add(appContactsArray.getString(i));
                }
            }
            if (jsonObject.has(SMS_CONTACTS)) {
                JSONArray appContactsArray = jsonObject.getJSONArray(SMS_CONTACTS);
                for (int i = 0; i < appContactsArray.length(); i++) {
                    smsContacts.add(appContactsArray.getString(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        contact.setAppContacts(invtAppContacts);
        contact.setSmsContacts(smsContacts);
        return contact;
    }
}