package com.cerone.invitation.fragement;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.FontTypes;
import com.example.dataobjects.Event;

/**
 * Created by adarsht on 15/02/17.
 */

public class BaseFragment extends Fragment implements AdapterView.OnItemClickListener {
    String locationPermission = "";
    Event eventDetails = new Event();
    public static final String LOCATION = "LOCATION";
    public static final String DISTANCE = "DISTANCE";
    public static final String NOTHING = "NOTHING";

    public void callToUser(String mobileNumber) {
        try {
            Intent my_callIntent = new Intent(Intent.ACTION_CALL);
            my_callIntent.setData(Uri.parse("tel:" + mobileNumber));
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(my_callIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "Error in your phone call"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
    public void showLocationPermissionDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.logout_dialog);
        Button accept = (Button) dialog.findViewById(R.id.btn_submit);
        Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        cancel.setVisibility(View.GONE);
        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.invt_RadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId){
                    case R.id.radio_location:
                        locationPermission = LOCATION;
                        Log.d("lacation", locationPermission);
                        break;
                    case R.id.radio_distance:
                        locationPermission = DISTANCE;
                        Log.d("lacation", locationPermission);
                        break;
                    case R.id.radio_nothing:
                        locationPermission = NOTHING;
                        Log.d("lacation", locationPermission);
                        break;
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptOrRejectInvitation(true, eventDetails, locationPermission);
                dialog.cancel();
            }


        });
        dialog.show();

    }
    public void acceptOrRejectInvitation(boolean b, Event eventDetails, String locationPermission) {
    }

    public void setFontType(int... viewId) {
        FontTypes fontType = new FontTypes(getActivity());
        for (int element : viewId) {
            TextView textview = (TextView) getView().findViewById(element);
            fontType.setTypeface(textview);
        }
    }

}
