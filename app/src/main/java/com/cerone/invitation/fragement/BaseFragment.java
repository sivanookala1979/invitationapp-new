package com.cerone.invitation.fragement;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * Created by adarsht on 15/02/17.
 */

public class BaseFragment extends Fragment implements AdapterView.OnItemClickListener {
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
}
