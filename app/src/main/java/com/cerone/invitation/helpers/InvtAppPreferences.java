/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.dataobjects.ChatInfo;
import com.example.dataobjects.CurrencyTypes;
import com.example.dataobjects.Event;
import com.example.dataobjects.Group;
import com.example.dataobjects.Invitation;
import com.example.dataobjects.MyMarker;
import com.example.dataobjects.ServiceInformation;
import com.example.dataobjects.User;
import com.example.dataobjects.UserLocation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


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
    private static final String CURRENCY_COUNTRY_CODE_DETAILS = "currencyCountryCodeDetails";
    private static final String PROFILE_DETAILS = "profileDetails";
    private static final String UPLOAD_PICTURE_QUALITY = "uploadpicturequality";
    private static final String ACCOUNT_SETTINGS_IMAGE = "accountImage";
    private static final String IS_PROFILE_GIVEN = "profilegiven";
    private static final String CHAT_INFO = "chatinfo";
    private static final String SCREEN_REFRESH_STATUS = "screenRefreshScreen";
    private static final String PROFILE_SCREEN_STATUS = "profileScreenStatus";



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

    public static void setScreenRefreshStatus(boolean loggedIn) {
        pref.edit().putBoolean(SCREEN_REFRESH_STATUS, loggedIn).commit();
    }

    public static boolean isScreenRefresh() {
        return pref.getBoolean(SCREEN_REFRESH_STATUS, false);
    }

    public static void setProfileUpdatedStatus(boolean loggedIn) {
        pref.edit().putBoolean(PROFILE_SCREEN_STATUS, loggedIn).commit();
    }

    public static boolean isProfileUpdated() {
        return pref.getBoolean(PROFILE_SCREEN_STATUS, false);
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

    public static void setServiceDetails(List<ServiceInformation> serviceInformation) {
        String serviceInformationString = new Gson().toJson(serviceInformation);
        pref.edit().putString(SERVICE_INFORMATION, serviceInformationString).commit();
    }

    public static List<ServiceInformation> getServiceDetails() {
        java.lang.reflect.Type type = new TypeToken<List<ServiceInformation>>() {
        }.getType();
        List<ServiceInformation> serviceInformationList = new Gson().fromJson(pref.getString(SERVICE_INFORMATION, ""), type);
        if (serviceInformationList == null) {
            serviceInformationList = new ArrayList<ServiceInformation>();
        }
        return serviceInformationList;
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

    public static void setOwnerId(int ownerId) {
        pref.edit().putInt(OWNER_ID, ownerId).commit();
    }

    public static int getOwnerId() {
        return pref.getInt(OWNER_ID, 0);
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

    public static void setCurrencyCountryCodeDetails(List<CurrencyTypes> addressDetails) {
        String addressData = new Gson().toJson(addressDetails);
        pref.edit().putString(CURRENCY_COUNTRY_CODE_DETAILS, addressData).commit();
    }

    public static List<CurrencyTypes> getCurrencyCountryCodeDetails() {
        java.lang.reflect.Type type = new TypeToken<List<CurrencyTypes>>() {
        }.getType();
        List<CurrencyTypes> addressDetails = new Gson().fromJson(
                pref.getString(CURRENCY_COUNTRY_CODE_DETAILS, ""), type);
        if (addressDetails == null) {
            addressDetails = new ArrayList<CurrencyTypes>();
        }
        return addressDetails;
    }

    public static User getProfileDetails() {
        java.lang.reflect.Type type = new TypeToken<User>() {
        }.getType();
        return new Gson().fromJson(pref.getString(PROFILE_DETAILS, ""), type);
    }

    public static void setProfileDetails(User profileDetails) {
        String profileDetail = new Gson().toJson(profileDetails);
        pref.edit().putString(PROFILE_DETAILS, profileDetail).commit();
    }

    public static void setUploadPictureQuality(int serviceType) {
        pref.edit().putInt(UPLOAD_PICTURE_QUALITY, serviceType).commit();
    }

    public static int getUploadPictureQuality() {
        return pref.getInt(UPLOAD_PICTURE_QUALITY, 100);
    }

    public static void setAccountImage(String accountImage) {
        pref.edit().putString(ACCOUNT_SETTINGS_IMAGE, accountImage).commit();
    }

    public static String getAccountImage() {
        return pref.getString(ACCOUNT_SETTINGS_IMAGE, "");
    }

    public static void setProfileGiven(boolean loggedIn) {
        pref.edit().putBoolean(IS_PROFILE_GIVEN, loggedIn).commit();
    }

    public static boolean isProfileGiven() {
        return pref.getBoolean(IS_PROFILE_GIVEN, false);
    }

    public static void setChatInfo(ChatInfo chatInfo) {
        String chatInfoDetails = new Gson().toJson(chatInfo);
        pref.edit().putString(CHAT_INFO, chatInfoDetails).commit();
    }

    public static ChatInfo getChatInfo() {
        java.lang.reflect.Type type = new TypeToken<ChatInfo>() {
        }.getType();
        return new Gson().fromJson(pref.getString(CHAT_INFO, ""), type);
    }

}