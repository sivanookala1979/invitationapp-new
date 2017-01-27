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
import com.example.utills.InvitationAppConstants;
import com.example.utills.StringUtils;


/**
 * @author CeroneFedora
 * @version 1.0, Dec 18, 2015
 */
public class EventSyncher extends BaseSyncher {

    public ServerResponse createEvent(Event event) {
        ServerResponse response = new ServerResponse();
        try {
            JSONObject object = new JSONObject();
            object.put("event_name", event.getName());
            object.put("start_date", StringUtils.StringToDate(event.getStartDateTime()));
            if (event.getEndDateTime() != null) {
                object.put("end_date", StringUtils.StringToDate(event.getEndDateTime()));
            }
            if (!event.isRecurring()) {
                object.put("recurring_type", "");
            } else {
                object.put("recurring_type", event.getRecurringType());
            }
            object.put("event_theme", event.getTheme());
            object.put("is_recurring_event", event.isRecurring());
            object.put("is_manual_check_in", event.isManualCheckIn());
            object.put("description", event.getDescription());
            object.put("latitude", event.getLatitude());
            object.put("longitude", event.getLongitude());
            object.put("address", event.getAddress());
            object.put("private", event.isPrivateType());
            object.put("remainder", event.isRemainder());
            object.put("status", event.getStatus());
            object.put("owner_name", event.getOwnerName());
            String extenction = (event.getEventId() == 0) ? "" : "?event_id=" + event.getEventId();
            JSONObject jsonResponse = new JSONObject(HTTPUtils.getDataFromServer(BASE_URL + "events/create_event.json" + extenction, "POST", object.toString(), true));
            if (jsonResponse != null) {
                response.setId(jsonResponse.getInt("id"));
                response.setStatus(jsonResponse.getString("status"));
            }
        } catch (Exception e) {
            handleException(e);
        }
        return response;
    }

    public List<Event> getMyEvents() {
        List<Event> listOfEvents = new ArrayList<Event>();
        try {
            JSONObject jsonResponse = new JSONObject(HTTPUtils.getDataFromServer(BASE_URL + "events/get_my_events.json", "GET", true));
            if (jsonResponse.has("events")) {
                JSONArray jsonArray = jsonResponse.getJSONArray("events");
                listOfEvents = geteventsByJsonArray(jsonArray);
            }
        } catch (Exception e) {
            handleException(e);
        }
        return listOfEvents;
    }

    public List<Event> getAllEvents() {
        List<Event> listOfEvents = new ArrayList<Event>();
        try {
            JSONObject jsonResponse = new JSONObject(HTTPUtils.getDataFromServer(BASE_URL + "events/get_all_events.json", "GET", true));
            if (jsonResponse.has("events")) {
                JSONArray jsonArray = jsonResponse.getJSONArray("events");
                listOfEvents = geteventsByJsonArray(jsonArray);
            }
        } catch (Exception e) {
            handleException(e);
        }
        return listOfEvents;
    }

    public List<Event> getMyEventsByIds(String eventIds) {
        List<Event> listOfEvents = new ArrayList<Event>();
        try {
            eventIds = eventIds.replace(" ", "%20");
            JSONObject jsonResponse = new JSONObject(HTTPUtils.getDataFromServer(BASE_URL + "events/get_my_events.json?event_ids=" + eventIds, "GET", true));
            if (jsonResponse.has("events")) {
                JSONArray jsonArray = jsonResponse.getJSONArray("events");
                listOfEvents = geteventsByJsonArray(jsonArray);
            }
        } catch (Exception e) {
            handleException(e);
        }
        return listOfEvents;
    }

    public List<Event> geteventsByJsonArray(JSONArray jsonArray) {
        List<Event> listOfEvents = new ArrayList<Event>();
        try {
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject != null) {
                        Event event = new Event();
                        event.setAddress(jsonObject.getString("address"));
                        event.setDescription(jsonObject.getString("description"));
                        if (!jsonObject.isNull("end_date")) {
                            event.setEndDateTime(StringUtils.getFormatedDateFromServerFormatedDate(jsonObject.getString("end_date")));
                            event.setEndDateTime(StringUtils.getNewDate(event.getEndDateTime(), InvitationAppConstants.TIME_DIFFERENCE));
                        }
                        if (!jsonObject.isNull("accepted_count")) {
                            event.setAcceptedCount(jsonObject.getInt("accepted_count"));
                        }
                        if (!jsonObject.isNull("rejected_count")) {
                            event.setRejectedCount(jsonObject.getInt("rejected_count"));
                        }
                        if (!jsonObject.isNull("invitees_count")) {
                            event.setInviteesCount(jsonObject.getInt("invitees_count"));
                        }
                        if (!jsonObject.isNull("is_manual_check_in")) {
                            event.setManualCheckIn(jsonObject.getBoolean("is_manual_check_in"));
                        }
                        if (!jsonObject.isNull("check_in_count")) {
                            event.setCheckedInCount(jsonObject.getInt("check_in_count"));
                        }
                        if (!jsonObject.isNull("is_recurring_event")) {
                            event.setRecurring(jsonObject.getBoolean("is_recurring_event"));
                        }
                        if (!jsonObject.isNull("event_theme")) {
                            event.setTheme(jsonObject.getString("event_theme"));
                        }
                        if (!jsonObject.isNull("recurring_type")) {
                            event.setRecurringType(jsonObject.getString("recurring_type"));
                        }
                        if (!jsonObject.isNull("hide")) {
                            event.setHide(jsonObject.getBoolean("hide"));
                        }
                        event.setLatitude(jsonObject.getDouble("latitude"));
                        event.setLongitude(jsonObject.getDouble("longitude"));
                        event.setName(jsonObject.getString("event_name"));
                        event.setOwnerId(jsonObject.getInt("owner_id"));
                        event.setPrivateType(jsonObject.getBoolean("private"));
                        event.setRemainder(jsonObject.getBoolean("remainder"));
                        event.setStartDateTime(StringUtils.getFormatedDateFromServerFormatedDate(jsonObject.getString("start_date")));
                        event.setStartDateTime(StringUtils.getNewDate(event.getStartDateTime(), InvitationAppConstants.TIME_DIFFERENCE));
                        event.setStatus(jsonObject.getString("status"));
                        event.setEventId(jsonObject.getInt("id"));
                        listOfEvents.add(event);
                    }
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
        return listOfEvents;
    }

    public ServerResponse deleteEvent(int eventId) {
        ServerResponse response = new ServerResponse();
        try {
            JSONObject jsonResponse = new JSONObject(HTTPUtils.getDataFromServer(BASE_URL + "events/delete_event.json?event_id=" + eventId, "GET", false));
            if (jsonResponse.has("id")) {
                response.setId(jsonResponse.getInt("id"));
                response.setStatus(jsonResponse.getString("status"));
            } else {
                response.setStatus(jsonResponse.getString("status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}