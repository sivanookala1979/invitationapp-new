/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import com.example.dataobjects.*;


/**
 * @author cerone
 * @version 1.0, Apr 28, 2015
 */
public class InvtAppPreferences {

    private static final String ACCESS_TOKEN = "accessToken";
    private static final String OWNER_ID = "ownerId";
    private static final String LOGGEDIN = "loggedIn";
    public static SharedPreferences pref;
    private static final String EVENT_DETAILS = "Event Details";
    private static final String EVENT_LOCATION = "eventLocation";
    private static final String MOST_RECENT_ADDRESS = "mostRecentAddress";
    private static final String ZOOM_POSITION = "zoom";
    private static final String PARTICIPANT_LOCATIONS = "ParticipantLocations";
    private static final String SERVICE_INFORMATION = "serviceInformation";
    private static final String SELECTED_CONTACTS = "contacts";
    private static final String INVITATION = "invitation";
    private static final String SELECTED_GROUPS = "groups";
    private static final String REFRESH_SERVICE = "refreshService";

    public static SharedPreferences getPref() {
        return pref;
    }

    public static void setPref(Context context) {
        if (pref == null) {
            InvtAppPreferences.pref = context.getSharedPreferences("com.example.invtapp_android", Context.MODE_PRIVATE);
        }
    }

    public static void reset() {
        pref.edit().clear().commit();
    }

    public static void setLoggedIn(boolean loggedIn) {
        pref.edit().putBoolean(LOGGEDIN, loggedIn).commit();
    }

    public static boolean isLoggedIn() {
        return pref.getBoolean(LOGGEDIN, false);
    }

    public static void setServiceRefresh(boolean loggedIn) {
        pref.edit().putBoolean(REFRESH_SERVICE, loggedIn).commit();
    }

    public static boolean isServiceRefresh() {
        return pref.getBoolean(REFRESH_SERVICE, false);
    }

    public static void setAccessToken(String accessToken) {
        Log.d("setAccessToken", accessToken);
        if (!accessToken.isEmpty() && accessToken.length() > 10)
            pref.edit().putString(ACCESS_TOKEN, accessToken).commit();
    }

    public static String getAccessToken() {
        return pref.getString(ACCESS_TOKEN, "");
    }

    public static void setZoomPosition(String accessToken) {
        pref.edit().putString(ACCESS_TOKEN, accessToken).commit();
    }

    public static String getZoomPosition() {
        return pref.getString(ACCESS_TOKEN, "");
    }

    public static Event getEventDetails() {
        java.lang.reflect.Type type = new TypeToken<Event>() {
        }.getType();
        Event event = new Gson().fromJson(pref.getString(EVENT_DETAILS, ""), type);
        return event;
    }

    public static void setEventDetails(Event event) {
        String eventDetails = new Gson().toJson(event);
        pref.edit().putString(EVENT_DETAILS, eventDetails).commit();
    }

    public static void setParticipantLocations(List<MyMarker> inviteesPositions) {
        String eventDetails = new Gson().toJson(inviteesPositions);
        pref.edit().putString(PARTICIPANT_LOCATIONS, eventDetails).commit();
    }

    public static List<MyMarker> getParticipantLocations() {
        java.lang.reflect.Type type = new TypeToken<List<MyMarker>>() {
        }.getType();
        List<MyMarker> inviteesPositions = new Gson().fromJson(pref.getString(PARTICIPANT_LOCATIONS, ""), type);
        if (inviteesPositions == null) {
            inviteesPositions = new ArrayList<MyMarker>();
        }
        return inviteesPositions;
    }

    public static void setEventLocationDetails(UserLocation location) {
        String eventDetails = new Gson().toJson(location);
        pref.edit().putString(EVENT_LOCATION, eventDetails).commit();
    }

    public static UserLocation getEventLocationDetails() {
        java.lang.reflect.Type type = new TypeToken<UserLocation>() {
        }.getType();
        UserLocation location = new Gson().fromJson(pref.getString(EVENT_LOCATION, ""), type);
        if (location == null) {
            location = new UserLocation();
        }
        return location;
    }

    public static void setServiceDetails(ServiceInformation serviceInformation) {
        String serviceInformationString = new Gson().toJson(serviceInformation);
        pref.edit().putString(SERVICE_INFORMATION, serviceInformationString).commit();
    }

    public static ServiceInformation getServiceDetails() {
        java.lang.reflect.Type type = new TypeToken<ServiceInformation>() {
        }.getType();
        ServiceInformation serviceInformation = new Gson().fromJson(pref.getString(SERVICE_INFORMATION, ""), type);
        if (serviceInformation == null) {
            serviceInformation = new ServiceInformation();
        }
        return serviceInformation;
    }

    public static List<User> getContacts() {
        java.lang.reflect.Type type = new TypeToken<List<User>>() {
        }.getType();
        List<User> contacts = new Gson().fromJson(pref.getString(SELECTED_CONTACTS, ""), type);
        if (contacts == null) {
            contacts = new ArrayList<User>();
        }
        return contacts;
    }

    public static void setContacts(List<User> contacts) {
        String contactDetails = new Gson().toJson(contacts);
        pref.edit().putString(SELECTED_CONTACTS, contactDetails).commit();
    }

    public static List<Group> getGroups() {
        java.lang.reflect.Type type = new TypeToken<List<Group>>() {
        }.getType();
        List<Group> groupsList = new Gson().fromJson(pref.getString(SELECTED_GROUPS, ""), type);
        if (groupsList == null) {
            groupsList = new ArrayList<Group>();
        }
        return groupsList;
    }

    public static void setGroups(List<Group> groups) {
        String contactDetails = new Gson().toJson(groups);
        pref.edit().putString(SELECTED_GROUPS, contactDetails).commit();
    }

    public static Invitation getInvitation() {
        java.lang.reflect.Type type = new TypeToken<Invitation>() {
        }.getType();
        Invitation invitation = new Gson().fromJson(pref.getString(INVITATION, ""), type);
        if (invitation == null) {
            invitation = new Invitation();
        }
        return invitation;
    }

    public static void setInvitation(Invitation invitation) {
        String contactDetails = new Gson().toJson(invitation);
        pref.edit().putString(INVITATION, contactDetails).commit();
    }

    public static void setOwnerId(String accessToken) {
        pref.edit().putString(OWNER_ID, accessToken).commit();
    }

    public static String getOwnerId() {
        return pref.getString(OWNER_ID, "");
    }

    public static void setRecentAddressDetails(List<UserLocation> contacts) {
        String eventDetails = new Gson().toJson(contacts);
        pref.edit().putString(MOST_RECENT_ADDRESS, eventDetails).commit();
    }

    public static List<UserLocation> getRecentAddressDetails() {
        java.lang.reflect.Type type = new TypeToken<List<UserLocation>>() {
        }.getType();
        List<UserLocation> locations = new Gson().fromJson(pref.getString(MOST_RECENT_ADDRESS, ""), type);
        if (locations == null) {
            locations = new ArrayList<UserLocation>();
        }
        return locations;
    }
}