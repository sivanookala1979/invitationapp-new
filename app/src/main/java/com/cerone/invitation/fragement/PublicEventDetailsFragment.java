package com.cerone.invitation.fragement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PublicEventDetailsFragment extends BaseFragment implements OnMapReadyCallback {


TextView eventDate, eventTime, eventLocation;
    private GoogleMap map;
    SupportMapFragment mapFragment;
    View eventBaseView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_public_event_details, container, false);
        eventBaseView = view;
        eventDate = (TextView) view.findViewById(R.id.event_date);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.publicEvent_map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng location = new LatLng(17.4325, 78.4070);
        map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.locatin_marker_user)).position(location).title("Kavali")).setVisible(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 5));
    }

    public static PublicEventDetailsFragment newInstance() {
        PublicEventDetailsFragment fragment = new PublicEventDetailsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

}
