package com.cerone.invitation.fragement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cerone.invitation.R;
import com.cerone.invitation.activities.PersonalizeActivity;
import com.cerone.invitation.adapter.ChooseCityAdapter;
import com.cerone.invitation.helpers.HomeScreenCommunicator;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.MobileHelper;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.City;
import com.example.dataobjects.EventFilter;
import com.example.syncher.PublicHomeSyncher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adarsh on 3/19/17.
 */

public class PublicHomeFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    List<City> cityList;
    ListView listViewCities;
    ChooseCityAdapter cityAdapter;
    private HomeScreenCommunicator activityCommunicator;


    public static PublicHomeFragment newInstance() {
        PublicHomeFragment fragment = new PublicHomeFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_public_home, container, false);
        listViewCities = (ListView) view.findViewById(R.id.listView_cities);
        listViewCities.setOnItemClickListener(this);
        activityCommunicator = (HomeScreenCommunicator) getActivity();
        getCities();
        return view;
    }

    private void getCities() {
        if (MobileHelper.hasNetwork(getActivity(), getActivity(), " to get cities", null)) {
            new InvtAppAsyncTask(getActivity()) {

                @Override
                public void process() {
                    PublicHomeSyncher Syncher = new PublicHomeSyncher();
                    cityList = new ArrayList<City>();
                    cityList = Syncher.getCities();
                }

                @Override
                public void afterPostExecute() {
                    if (cityList.size() > 0) {
                        cityAdapter = new ChooseCityAdapter(getActivity(), R.layout.public_city_item, cityList);
                        listViewCities.setAdapter(cityAdapter);
                    } else {
                        ToastHelper.blueToast(getActivity(), "No events found.");
                    }
                }
            }.execute();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EventFilter eventFilters = InvtAppPreferences.getEventFilters();
        eventFilters.getSelectedCitysList().add(cityList.get(position));
        InvtAppPreferences.setEventFilters(eventFilters);
        startActivity(new Intent(getActivity(), PersonalizeActivity.class));
    }
}
