package com.example.syncher;

import com.example.dataobjects.PublicEvent;
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
                        if(jsonObject.has("is_weekend"))
                            publicEvent.setWeekend(jsonObject.getBoolean("is_weekend"));
                        if(jsonObject.has("city"))
                            publicEvent.setCity(jsonObject.getString("city"));
                        if(jsonObject.has("service"))
                            publicEvent.setService(jsonObject.getString("service"));
                        if(jsonObject.has("img_url"))
                            publicEvent.setImage(jsonObject.getString("img_url"));
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
