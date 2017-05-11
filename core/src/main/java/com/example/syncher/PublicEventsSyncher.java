package com.example.syncher;

import com.example.dataobjects.PublicEvent;
import com.example.dataobjects.SaveResult;
import com.example.utills.HTTPUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chakri on 7/4/17.
 */

public class PublicEventsSyncher extends BaseSyncher{

    public List<PublicEvent> getPublicEvents() {
        List<PublicEvent> listOfPublicEvents = new ArrayList<PublicEvent>();
        try {
            JSONObject jsonResponse = new JSONObject(HTTPUtils.getDataFromServer(BASE_URL + "public_events/get_public_events.json", "GET", true));
            if (jsonResponse.has("public_events")) {
                JSONArray jsonArray = jsonResponse.getJSONArray("public_events");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        PublicEvent publicEvent = new PublicEvent();
                        if(jsonObject.has("id"))
                            publicEvent.setId(jsonObject.getInt("id"));
                        if(jsonObject.has("event_name"))
                            publicEvent.setEventName(jsonObject.getString("event_name"));
                        if(jsonObject.has("start_time"))
                            publicEvent.setStartRime(jsonObject.getString("start_time"));
                        if(jsonObject.has("end_time"))
                            publicEvent.setEndTime(jsonObject.getString("end_time"));
                        if(jsonObject.has("entry_fee"))
                            publicEvent.setEntryFee(jsonObject.getString("entry_fee"));
                        if(jsonObject.has("address"))
                            publicEvent.setAddress(jsonObject.getString("address"));
                        if(jsonObject.has("latitude"))
                            publicEvent.setLatitude(jsonObject.getDouble("latitude"));
                        if(jsonObject.has("longitude"))
                            publicEvent.setLongitude(jsonObject.getDouble("longitude"));
                        if(jsonObject.has("is_weekend"))
                            publicEvent.setWeekend(jsonObject.getBoolean("is_weekend"));
                        if(jsonObject.has("city"))
                            publicEvent.setCity(jsonObject.getString("city"));
                        if(jsonObject.has("service"))
                            publicEvent.setService(jsonObject.getString("service"));
                        if(jsonObject.has("img_url"))
                            publicEvent.setImage(jsonObject.getString("img_url"));
                        if(jsonObject.has("is_favourite"))
                            publicEvent.setFavourite(jsonObject.getBoolean("is_favourite"));
                        listOfPublicEvents.add(publicEvent);
                    }
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
        return listOfPublicEvents;
    }

    public SaveResult addFavourites(int eventId, int cityId){
        SaveResult result = new SaveResult();
        try{
            String response = HTTPUtils.getDataFromServer(BASE_URL + "public_events/add_favourite_event.json?public_event_id="+eventId+"&city_id="+cityId, "GET", true);
            JSONObject responseJSON = new JSONObject(response);
            if (responseJSON.has("status")) {
                result.setStatus(responseJSON.getString("status"));
                result.setSuccess(true);
            } else if(responseJSON.has("error_message")) {
                result.setErrorMessage(responseJSON.getString("error_message"));
            }
        }
        catch (Exception ex) {
            handleException(ex);
        }
        return result;
    }

    public SaveResult removeFavourites(int eventId, int cityId){
        SaveResult result = new SaveResult();
        try{
            String response = HTTPUtils.getDataFromServer(BASE_URL + "public_events/remove_favourite_event.json?public_event_id="+eventId+"&city_id="+cityId, "GET", true);
            JSONObject responseJSON = new JSONObject(response);
            if (responseJSON.has("status")) {
                result.setStatus(responseJSON.getString("status"));
                result.setSuccess(true);
            } else if(responseJSON.has("error_message")) {
                result.setErrorMessage(responseJSON.getString("error_message"));
            }
        }
        catch (Exception ex) {
            handleException(ex);
        }
        return result;
    }

    public SaveResult addViews(int eventId){
        SaveResult result = new SaveResult();
        try{
            String response = HTTPUtils.getDataFromServer(BASE_URL + "public_events/add_views.json?public_event_id="+eventId, "GET", true);
            JSONObject responseJSON = new JSONObject(response);
            if (responseJSON.has("status")) {
                result.setStatus(responseJSON.getString("status"));
                result.setSuccess(true);
            } else if(responseJSON.has("error_message")) {
                result.setErrorMessage(responseJSON.getString("error_message"));
            }
        }
        catch (Exception ex) {
            handleException(ex);
        }
        return result;
    }

    public SaveResult cancelPublicEvents(int eventId){
        SaveResult result = new SaveResult();
        try{
            String response = HTTPUtils.getDataFromServer(BASE_URL + "public_events/cancel_public_events.json?public_event_id="+eventId, "GET", true);
            JSONObject responseJSON = new JSONObject(response);
            if (responseJSON.has("status")) {
                result.setStatus(responseJSON.getString("status"));
                result.setSuccess(true);
            } else if(responseJSON.has("error_message")) {
                result.setErrorMessage(responseJSON.getString("error_message"));
            }
        }
        catch (Exception ex) {
            handleException(ex);
        }
        return result;
    }

    public List<PublicEvent> getMyFavourites(int cityId) {
        List<PublicEvent> listOfPublicEvents = new ArrayList<PublicEvent>();
        try {
            JSONObject jsonResponse = new JSONObject(HTTPUtils.getDataFromServer(BASE_URL + "public_events/my_city_favourites.json?city_id="+cityId, "GET", true));
            if (jsonResponse.has("favourite_events")) {
                JSONArray jsonArray = jsonResponse.getJSONArray("favourite_events");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        PublicEvent publicEvent = new PublicEvent();
                        if(jsonObject.has("id"))
                            publicEvent.setId(jsonObject.getInt("id"));
                        if(jsonObject.has("event_name"))
                            publicEvent.setEventName(jsonObject.getString("event_name"));
                        if(jsonObject.has("start_time"))
                            publicEvent.setStartRime(jsonObject.getString("start_time"));
                        if(jsonObject.has("end_time"))
                            publicEvent.setEndTime(jsonObject.getString("end_time"));
                        if(jsonObject.has("entry_fee"))
                            publicEvent.setEntryFee(jsonObject.getString("entry_fee"));
                        if(jsonObject.has("address"))
                            publicEvent.setAddress(jsonObject.getString("address"));
                        if(jsonObject.has("latitude"))
                            publicEvent.setLatitude(jsonObject.getDouble("latitude"));
                        if(jsonObject.has("longitude"))
                            publicEvent.setLongitude(jsonObject.getDouble("longitude"));
                        if(jsonObject.has("is_weekend"))
                            publicEvent.setWeekend(jsonObject.getBoolean("is_weekend"));
                        if(jsonObject.has("city"))
                            publicEvent.setCity(jsonObject.getString("city"));
                        if(jsonObject.has("service"))
                            publicEvent.setService(jsonObject.getString("service"));
                        if(jsonObject.has("img_url"))
                            publicEvent.setImage(jsonObject.getString("img_url"));
                        if(jsonObject.has("is_favourite"))
                            publicEvent.setFavourite(jsonObject.getBoolean("is_favourite"));
                        listOfPublicEvents.add(publicEvent);
                    }
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
        return listOfPublicEvents;
    }

    public List<PublicEvent> getTrendingPublicEvents(int cityId) {
        List<PublicEvent> listOfPublicEvents = new ArrayList<PublicEvent>();
        try {
            JSONObject jsonResponse = new JSONObject(HTTPUtils.getDataFromServer(BASE_URL + "public_events/trending_events.json?city_id="+cityId, "GET", true));
            if (jsonResponse.has("public_events")) {
                JSONArray jsonArray = jsonResponse.getJSONArray("public_events");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        PublicEvent publicEvent = new PublicEvent();
                        if(jsonObject.has("id"))
                            publicEvent.setId(jsonObject.getInt("id"));
                        if(jsonObject.has("event_name"))
                            publicEvent.setEventName(jsonObject.getString("event_name"));
                        if(jsonObject.has("start_time"))
                            publicEvent.setStartRime(jsonObject.getString("start_time"));
                        if(jsonObject.has("end_time"))
                            publicEvent.setEndTime(jsonObject.getString("end_time"));
                        if(jsonObject.has("entry_fee"))
                            publicEvent.setEntryFee(jsonObject.getString("entry_fee"));
                        if(jsonObject.has("address"))
                            publicEvent.setAddress(jsonObject.getString("address"));
                        if(jsonObject.has("latitude"))
                            publicEvent.setLatitude(jsonObject.getDouble("latitude"));
                        if(jsonObject.has("longitude"))
                            publicEvent.setLongitude(jsonObject.getDouble("longitude"));
                        if(jsonObject.has("is_weekend"))
                            publicEvent.setWeekend(jsonObject.getBoolean("is_weekend"));
                        if(jsonObject.has("city"))
                            publicEvent.setCity(jsonObject.getString("city"));
                        if(jsonObject.has("service"))
                            publicEvent.setService(jsonObject.getString("service"));
                        if(jsonObject.has("img_url"))
                            publicEvent.setImage(jsonObject.getString("img_url"));
                        if(jsonObject.has("is_favourite"))
                            publicEvent.setFavourite(jsonObject.getBoolean("is_favourite"));
                        listOfPublicEvents.add(publicEvent);
                    }
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
        return listOfPublicEvents;
    }


    public List<PublicEvent> getFreePublicEvents(int cityId) {
        List<PublicEvent> listOfPublicEvents = new ArrayList<PublicEvent>();
        try {
            JSONObject jsonResponse = new JSONObject(HTTPUtils.getDataFromServer(BASE_URL + "public_events/free_public_events.json?city_id="+cityId, "GET", true));
            if (jsonResponse.has("free_events")) {
                JSONArray jsonArray = jsonResponse.getJSONArray("free_events");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        PublicEvent publicEvent = new PublicEvent();
                        if(jsonObject.has("id"))
                            publicEvent.setId(jsonObject.getInt("id"));
                        if(jsonObject.has("event_name"))
                            publicEvent.setEventName(jsonObject.getString("event_name"));
                        if(jsonObject.has("start_time"))
                            publicEvent.setStartRime(jsonObject.getString("start_time"));
                        if(jsonObject.has("end_time"))
                            publicEvent.setEndTime(jsonObject.getString("end_time"));
                        if(jsonObject.has("entry_fee"))
                            publicEvent.setEntryFee(jsonObject.getString("entry_fee"));
                        if(jsonObject.has("address"))
                            publicEvent.setAddress(jsonObject.getString("address"));
                        if(jsonObject.has("latitude"))
                            publicEvent.setLatitude(jsonObject.getDouble("latitude"));
                        if(jsonObject.has("longitude"))
                            publicEvent.setLongitude(jsonObject.getDouble("longitude"));
                        if(jsonObject.has("is_weekend"))
                            publicEvent.setWeekend(jsonObject.getBoolean("is_weekend"));
                        if(jsonObject.has("city"))
                            publicEvent.setCity(jsonObject.getString("city"));
                        if(jsonObject.has("service"))
                            publicEvent.setService(jsonObject.getString("service"));
                        if(jsonObject.has("img_url"))
                            publicEvent.setImage(jsonObject.getString("img_url"));
                        if(jsonObject.has("is_favourite"))
                            publicEvent.setFavourite(jsonObject.getBoolean("is_favourite"));
                        listOfPublicEvents.add(publicEvent);
                    }
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
        return listOfPublicEvents;
    }


    public List<PublicEvent> getWeekendPublicEvents(int cityId) {
        List<PublicEvent> listOfPublicEvents = new ArrayList<PublicEvent>();
        try {
            JSONObject jsonResponse = new JSONObject(HTTPUtils.getDataFromServer(BASE_URL + "public_events/weekend_public_events.json?city_id="+cityId, "GET", true));
            if (jsonResponse.has("weekend_events")) {
                JSONArray jsonArray = jsonResponse.getJSONArray("weekend_events");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        PublicEvent publicEvent = new PublicEvent();
                        if(jsonObject.has("id"))
                            publicEvent.setId(jsonObject.getInt("id"));
                        if(jsonObject.has("event_name"))
                            publicEvent.setEventName(jsonObject.getString("event_name"));
                        if(jsonObject.has("start_time"))
                            publicEvent.setStartRime(jsonObject.getString("start_time"));
                        if(jsonObject.has("end_time"))
                            publicEvent.setEndTime(jsonObject.getString("end_time"));
                        if(jsonObject.has("entry_fee"))
                            publicEvent.setEntryFee(jsonObject.getString("entry_fee"));
                        if(jsonObject.has("address"))
                            publicEvent.setAddress(jsonObject.getString("address"));
                        if(jsonObject.has("latitude"))
                            publicEvent.setLatitude(jsonObject.getDouble("latitude"));
                        if(jsonObject.has("longitude"))
                            publicEvent.setLongitude(jsonObject.getDouble("longitude"));
                        if(jsonObject.has("is_weekend"))
                            publicEvent.setWeekend(jsonObject.getBoolean("is_weekend"));
                        if(jsonObject.has("city"))
                            publicEvent.setCity(jsonObject.getString("city"));
                        if(jsonObject.has("service"))
                            publicEvent.setService(jsonObject.getString("service"));
                        if(jsonObject.has("img_url"))
                            publicEvent.setImage(jsonObject.getString("img_url"));
                        if(jsonObject.has("is_favourite"))
                            publicEvent.setFavourite(jsonObject.getBoolean("is_favourite"));
                        listOfPublicEvents.add(publicEvent);
                    }
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
        return listOfPublicEvents;
    }

    public List<PublicEvent> getPublicEventsWithKeywords(int cityId, String keyWords) {
        List<PublicEvent> listOfPublicEvents = new ArrayList<PublicEvent>();
        try {
            JSONObject jsonResponse = new JSONObject(HTTPUtils.getDataFromServer(BASE_URL + "public_events/public_events_with_search_keywords.json?city_id="+cityId+"&keywords="+keyWords, "GET", true));
            if (jsonResponse.has("searched_events")) {
                JSONArray jsonArray = jsonResponse.getJSONArray("searched_events");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        PublicEvent publicEvent = new PublicEvent();
                        if(jsonObject.has("id"))
                            publicEvent.setId(jsonObject.getInt("id"));
                        if(jsonObject.has("event_name"))
                            publicEvent.setEventName(jsonObject.getString("event_name"));
                        if(jsonObject.has("start_time"))
                            publicEvent.setStartRime(jsonObject.getString("start_time"));
                        if(jsonObject.has("end_time"))
                            publicEvent.setEndTime(jsonObject.getString("end_time"));
                        if(jsonObject.has("entry_fee"))
                            publicEvent.setEntryFee(jsonObject.getString("entry_fee"));
                        if(jsonObject.has("address"))
                            publicEvent.setAddress(jsonObject.getString("address"));
                        if(jsonObject.has("latitude"))
                            publicEvent.setLatitude(jsonObject.getDouble("latitude"));
                        if(jsonObject.has("longitude"))
                            publicEvent.setLongitude(jsonObject.getDouble("longitude"));
                        if(jsonObject.has("is_weekend"))
                            publicEvent.setWeekend(jsonObject.getBoolean("is_weekend"));
                        if(jsonObject.has("city"))
                            publicEvent.setCity(jsonObject.getString("city"));
                        if(jsonObject.has("service"))
                            publicEvent.setService(jsonObject.getString("service"));
                        if(jsonObject.has("img_url"))
                            publicEvent.setImage(jsonObject.getString("img_url"));
                        if(jsonObject.has("is_favourite"))
                            publicEvent.setFavourite(jsonObject.getBoolean("is_favourite"));
                        listOfPublicEvents.add(publicEvent);
                    }
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
        return listOfPublicEvents;
    }

    public List<PublicEvent> getSimilarEvents(int eventId, int cityId) {
        List<PublicEvent> listOfPublicEvents = new ArrayList<PublicEvent>();
        try {
            JSONObject jsonResponse = new JSONObject(HTTPUtils.getDataFromServer(BASE_URL + "public_events/similar_events_list.json?public_event_id="+eventId+"&city_id="+cityId, "GET", true));
            if (jsonResponse.has("similar_events_list")) {
                JSONArray jsonArray = jsonResponse.getJSONArray("similar_events_list");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        PublicEvent publicEvent = new PublicEvent();
                        if(jsonObject.has("id"))
                            publicEvent.setId(jsonObject.getInt("id"));
                        if(jsonObject.has("event_name"))
                            publicEvent.setEventName(jsonObject.getString("event_name"));
                        if(jsonObject.has("start_time"))
                            publicEvent.setStartRime(jsonObject.getString("start_time"));
                        if(jsonObject.has("end_time"))
                            publicEvent.setEndTime(jsonObject.getString("end_time"));
                        if(jsonObject.has("entry_fee"))
                            publicEvent.setEntryFee(jsonObject.getString("entry_fee"));
                        if(jsonObject.has("address"))
                            publicEvent.setAddress(jsonObject.getString("address"));
                        if(jsonObject.has("latitude"))
                            publicEvent.setLatitude(jsonObject.getDouble("latitude"));
                        if(jsonObject.has("longitude"))
                            publicEvent.setLongitude(jsonObject.getDouble("longitude"));
                        if(jsonObject.has("is_weekend"))
                            publicEvent.setWeekend(jsonObject.getBoolean("is_weekend"));
                        if(jsonObject.has("city"))
                            publicEvent.setCity(jsonObject.getString("city"));
                        if(jsonObject.has("service"))
                            publicEvent.setService(jsonObject.getString("service"));
                        if(jsonObject.has("img_url"))
                            publicEvent.setImage(jsonObject.getString("img_url"));
                        if(jsonObject.has("is_favourite"))
                            publicEvent.setFavourite(jsonObject.getBoolean("is_favourite"));
                        listOfPublicEvents.add(publicEvent);
                    }
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
        return listOfPublicEvents;
    }

    public List<PublicEvent> getRecommendedEventsList(int cityId) {
        List<PublicEvent> listOfPublicEvents = new ArrayList<PublicEvent>();
        try {
            JSONObject jsonResponse = new JSONObject(HTTPUtils.getDataFromServer(BASE_URL + "public_events/recommended_events_list.json?city_id="+cityId, "GET", true));
            if (jsonResponse.has("recommended_events_list")) {
                JSONArray jsonArray = jsonResponse.getJSONArray("recommended_events_list");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        PublicEvent publicEvent = new PublicEvent();
                        if(jsonObject.has("id"))
                            publicEvent.setId(jsonObject.getInt("id"));
                        if(jsonObject.has("event_name"))
                            publicEvent.setEventName(jsonObject.getString("event_name"));
                        if(jsonObject.has("start_time"))
                            publicEvent.setStartRime(jsonObject.getString("start_time"));
                        if(jsonObject.has("end_time"))
                            publicEvent.setEndTime(jsonObject.getString("end_time"));
                        if(jsonObject.has("entry_fee"))
                            publicEvent.setEntryFee(jsonObject.getString("entry_fee"));
                        if(jsonObject.has("address"))
                            publicEvent.setAddress(jsonObject.getString("address"));
                        if(jsonObject.has("latitude"))
                            publicEvent.setLatitude(jsonObject.getDouble("latitude"));
                        if(jsonObject.has("longitude"))
                            publicEvent.setLongitude(jsonObject.getDouble("longitude"));
                        if(jsonObject.has("is_weekend"))
                            publicEvent.setWeekend(jsonObject.getBoolean("is_weekend"));
                        if(jsonObject.has("city"))
                            publicEvent.setCity(jsonObject.getString("city"));
                        if(jsonObject.has("service"))
                            publicEvent.setService(jsonObject.getString("service"));
                        if(jsonObject.has("img_url"))
                            publicEvent.setImage(jsonObject.getString("img_url"));
                        if(jsonObject.has("is_favourite"))
                            publicEvent.setFavourite(jsonObject.getBoolean("is_favourite"));
                        listOfPublicEvents.add(publicEvent);
                    }
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
        return listOfPublicEvents;
    }

    public List<PublicEvent> getOfferedPublicEvents(int cityId) {
        List<PublicEvent> listOfPublicEvents = new ArrayList<PublicEvent>();
        try {
            JSONObject jsonResponse = new JSONObject(HTTPUtils.getDataFromServer(BASE_URL + "public_events/offered_public_events.json?city_id="+cityId, "GET", true));
            if (jsonResponse.has("offered_public_events")) {
                JSONArray jsonArray = jsonResponse.getJSONArray("offered_public_events");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        PublicEvent publicEvent = new PublicEvent();
                        if(jsonObject.has("id"))
                            publicEvent.setId(jsonObject.getInt("id"));
                        if(jsonObject.has("event_name"))
                            publicEvent.setEventName(jsonObject.getString("event_name"));
                        if(jsonObject.has("start_time"))
                            publicEvent.setStartRime(jsonObject.getString("start_time"));
                        if(jsonObject.has("end_time"))
                            publicEvent.setEndTime(jsonObject.getString("end_time"));
                        if(jsonObject.has("entry_fee"))
                            publicEvent.setEntryFee(jsonObject.getString("entry_fee"));
                        if(jsonObject.has("address"))
                            publicEvent.setAddress(jsonObject.getString("address"));
                        if(jsonObject.has("latitude"))
                            publicEvent.setLatitude(jsonObject.getDouble("latitude"));
                        if(jsonObject.has("longitude"))
                            publicEvent.setLongitude(jsonObject.getDouble("longitude"));
                        if(jsonObject.has("is_weekend"))
                            publicEvent.setWeekend(jsonObject.getBoolean("is_weekend"));
                        if(jsonObject.has("city"))
                            publicEvent.setCity(jsonObject.getString("city"));
                        if(jsonObject.has("service"))
                            publicEvent.setService(jsonObject.getString("service"));
                        if(jsonObject.has("img_url"))
                            publicEvent.setImage(jsonObject.getString("img_url"));
                        if(jsonObject.has("is_favourite"))
                            publicEvent.setFavourite(jsonObject.getBoolean("is_favourite"));
                        listOfPublicEvents.add(publicEvent);
                    }
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
        return listOfPublicEvents;
    }


}
