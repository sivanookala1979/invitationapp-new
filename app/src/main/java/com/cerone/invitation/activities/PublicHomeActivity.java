package com.cerone.invitation.activities;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.adapter.ChooseCityAdapter;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.MobileHelper;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.City;
import com.example.syncher.PublicHomeSyncher;

import java.util.ArrayList;
import java.util.List;

public class PublicHomeActivity extends BaseActivity {
    List<City> cityList;
    ListView listViewCities;
    ChooseCityAdapter cityAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_home);
        addToolbarView();
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("Choose a City");
        listViewCities = (ListView) findViewById(R.id.listView_cities);
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
                   if(cityList.size()>0){
                       cityAdapter = new ChooseCityAdapter(getApplicationContext(), R.layout.activity_public_home, cityList);
                       listViewCities.setAdapter(cityAdapter);
                   } else {
                        ToastHelper.blueToast(getApplicationContext(), "No events found.");
                    }
                }
            }.execute();
        }
    }


}
