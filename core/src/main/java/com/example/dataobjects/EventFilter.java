package com.example.dataobjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adarsht on 11/04/17.
 */

public class EventFilter {
    List<City> cityList= new ArrayList<City>();
    List<City> selectedCitysList= new ArrayList<City>();
    List<FavoriteTopic> favoriteTopicList= new ArrayList<FavoriteTopic>();
    City selectedCity;
    boolean favorites;
    boolean free;
    boolean isValid;

    public City getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCity(City selectedCity) {
        this.selectedCity = selectedCity;
    }

    public List<City> getSelectedCitysList() {
        return selectedCitysList;
    }

    public void setSelectedCitysList(List<City> selectedCitysList) {
        this.selectedCitysList = selectedCitysList;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }

    public List<FavoriteTopic> getFavoriteTopicList() {
        return favoriteTopicList;
    }

    public void setFavoriteTopicList(List<FavoriteTopic> favoriteTopicList) {
        this.favoriteTopicList = favoriteTopicList;
    }

    public boolean isFavorites() {
        return favorites;
    }

    public void setFavorites(boolean favorites) {
        this.favorites = favorites;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }
}
