package com.example.syncher;

import com.example.dataobjects.City;
import com.example.dataobjects.FavoriteTopic;
import com.example.utills.HTTPUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peekay on 14/3/17.
 */

public class PublicHomeSyncher extends BaseSyncher {

    public List<City> getCities() {
        List<City> listOfCities = new ArrayList<City>();
        try {
            String jsonResponse = HTTPUtils.getDataFromServer(BASE_URL + "cities/get_cities", "GET", true);
            if(jsonResponse!=null) {
                JSONArray cities = new JSONArray(jsonResponse);
                for (int i=0; i<cities.length(); i++) {
                    JSONObject citi = cities.getJSONObject(i);
                    City city = new City();
                    if (citi.has("id"))
                        city.setId(citi.getInt("id"));
                    if (citi.has("latitude"))
                        city.setLatitude(citi.getString("latitude"));
                    if (citi.has("longitude"))
                        city.setLongitude(citi.getString("longitude"));
                    if (citi.has("name"))
                        city.setName(citi.getString("name"));
                    if (citi.has("pincode"))
                        city.setPincode(citi.getString("pincode"));
                    if (citi.has("remarks"))
                        city.setRemarks(citi.getString("remarks"));
                    if (citi.has("image_path"))
                        city.setImage_path(citi.getString("image_path"));
                    listOfCities.add(city);
                }
            }

        } catch (Exception e) {
            handleException(e);
        }
        return listOfCities;
    }

    public List<FavoriteTopic> getEventItems() {
        List<FavoriteTopic> listOfEventItems = new ArrayList<FavoriteTopic>();
        try {
            String jsonResponse = HTTPUtils.getDataFromServer(BASE_URL + "services/get_services", "GET", true);
            if(jsonResponse!=null) {
                JSONArray eventItems = new JSONArray(jsonResponse);
                for (int i=0; i<eventItems.length(); i++) {
                    JSONObject eventItem = eventItems.getJSONObject(i);
                    FavoriteTopic event = new FavoriteTopic();
                    if (eventItem.has("id"))
                        event.setId(eventItem.getInt("id"));
                    if (eventItem.has("name"))
                        event.setName(eventItem.getString("name"));
                    if (eventItem.has("image_path"))
                        event.setImagePath(eventItem.getString("image_path"));
                    listOfEventItems.add(event);
                }
            }

        } catch (Exception e) {
            handleException(e);
        }
        return listOfEventItems;
    }

}
