package com.example.syncher;

import com.example.dataobjects.MyMarker;
import com.example.dataobjects.ServerResponse;
import com.example.dataobjects.UserLocation;
import com.example.utills.HTTPUtils;
import com.example.utills.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class LocationSyncher extends BaseSyncher {

    private static final String LOCATION = "location";
    private static final String GEOMETRY = "geometry";
    private static final String STATUS = "status";
    private static final String OK = "OK";
    private static final String RESULTS = "results";
    private static final String KEY_DESCRIPTION = "description";
    private static final String PREDICTIONS = "predictions";
    private static final String API_KEY = "AIzaSyAr8xCnXF4DmGqHV1JGibUlw57mkNrAPLs";
    String URL_START = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=";
    String URL_END = "&regions=(administrative_area_level_1)&sensor=true&key=" + API_KEY;
    String LAT_LON_URL_START = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    String LAT_LON_URL_END = "&key=" + API_KEY;
    String URL = "";

    public List<String> getLocations(String searchLocation) {
        List<String> list = new ArrayList<String>();
        try {
            URL = URL_START + URLEncoder.encode(searchLocation, "utf-8") + URL_END;
            String data = HTTPUtils.getDataFromServer(URL, "GET", false);
            System.out.println(data);
            JSONObject json = HTTPUtils.getJSONFromUrl(URL);
            if (json != null) {
                JSONArray contacts;
                try {
                    contacts = json.getJSONArray(PREDICTIONS);
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String description = c.getString(KEY_DESCRIPTION);
                        // Log.d(KEY_DESCRIPTION, description);
                        list.add(description);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return list;
    }

    public UserLocation getLocationDetailsByName(String locationName) {
        UserLocation details = null;
        try {
            String completeUrl = LAT_LON_URL_START + URLEncoder.encode(locationName, "utf-8") + LAT_LON_URL_END;
            String response = HTTPUtils.getDataFromServer(completeUrl, "GET", false);
            JSONObject json = new JSONObject(response);
            if (json != null) {
                JSONArray contacts;
                try {
                    if (json.getString(STATUS).equals(OK)) {
                        details = new UserLocation();
                        contacts = json.getJSONArray(RESULTS);
                        for (int i = 0; i < contacts.length(); i++) {
                            JSONObject myLocation = contacts.getJSONObject(i);
                            if (myLocation.has(GEOMETRY)) {
                                JSONObject gemetryInfo = myLocation.getJSONObject(GEOMETRY);
                                JSONObject latLong = gemetryInfo.getJSONObject(LOCATION);
                                details.setLatitude(latLong.getDouble("lat"));
                                details.setLongitude(latLong.getDouble("lng"));
                                details.setAddress(locationName);//myLocation.getString("formatted_address")
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return details;
    }

    public ServerResponse postUserLocation(UserLocation userLocation) {
        ServerResponse response = new ServerResponse();
        try {
            JSONObject object = new JSONObject();
            object.put("latitude", userLocation.getLatitude());
            object.put("longitude", userLocation.getLongitude());
            object.put("time", StringUtils.StringToDate(userLocation.getDateTime()));
            JSONObject jsonResponse = new JSONObject(HTTPUtils.getDataFromServer(BASE_URL + "events/post_location.json", "POST", object.toString(), true));
            if (jsonResponse != null) {
                response.setStatus(jsonResponse.getString("status"));
            }
        } catch (Exception e) {
            handleException(e);
        }
        return response;
    }

    public List<MyMarker> checkInviteesLocation(int eventId) {
        List<MyMarker> listOflocations = new ArrayList<MyMarker>();
        try {
            JSONObject jsonResponse = new JSONObject(HTTPUtils.getDataFromServer(BASE_URL + "events/invitees_locations.json?event_id=" + eventId, "GET", true));
            JSONArray jsonArray = jsonResponse.getJSONArray("locations_of_invitees");
            if (jsonArray != null) {
                {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        MyMarker location = new MyMarker();
                        location.setDateTime(jsonObject.getString("time"));
                        if (!jsonObject.isNull("latitude")) {
                            location.setLatitude(jsonObject.getDouble("latitude"));
                        }
                        if (!jsonObject.isNull("longitude")) {
                            location.setLongitude(jsonObject.getDouble("longitude"));
                        }
                        if (!jsonObject.isNull("user_name")) {
                            location.setInviteeName(jsonObject.getString("user_name"));
                        }
                        if (!jsonObject.isNull("user_mobile_number")) {
                            location.setContactNumber(jsonObject.getString("user_mobile_number"));
                        }
                        listOflocations.add(location);
                    }
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
        return listOflocations;
    }
}