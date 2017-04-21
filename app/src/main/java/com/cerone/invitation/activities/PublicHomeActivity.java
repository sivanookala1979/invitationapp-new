package com.cerone.invitation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cerone.invitation.R;
import com.cerone.invitation.adapter.ChooseCityAdapter;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.MobileHelper;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.City;
import com.example.dataobjects.EventFilter;
import com.example.syncher.PublicHomeSyncher;

import java.util.ArrayList;
import java.util.List;

public class PublicHomeActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    List<City> cityList;
    ListView listViewCities;
    ChooseCityAdapter cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_public);
        addToolbarView();
        listViewCities = (ListView) findViewById(R.id.listView_cities);
        listViewCities.setOnItemClickListener(this);
        getCities();

    }

    private void getCities() {
        if (MobileHelper.hasNetwork(getApplicationContext(), PublicHomeActivity.this, " to get cities", null)) {
            new InvtAppAsyncTask(this) {

                @Override
                public void process() {
                    PublicHomeSyncher Syncher = new PublicHomeSyncher();
                    cityList = new ArrayList<City>();
                    cityList = Syncher.getCities();
                }

                @Override
                public void afterPostExecute() {
                    if (cityList.size() > 0) {
                        cityAdapter = new ChooseCityAdapter(getApplicationContext(), R.layout.public_city_item, cityList);
                        listViewCities.setAdapter(cityAdapter);
                    } else {
                        ToastHelper.blueToast(getApplicationContext(), "No events found.");
                    }
                }
            }.execute();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EventFilter eventFilters = InvtAppPreferences.getEventFilters();
        eventFilters.setSelectedCity(cityList.get(position));
        InvtAppPreferences.setEventFilters(eventFilters);
        startActivity(new Intent(getApplicationContext(), PersonalizeActivity.class));
    }
}
