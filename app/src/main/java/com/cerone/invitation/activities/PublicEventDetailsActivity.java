package com.cerone.invitation.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cerone.invitation.R;
import com.cerone.invitation.adapter.SimilarEventsAdapter;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.MobileHelper;
import com.cerone.invitation.helpers.RecyclerTouchListener;
import com.example.dataobjects.PublicEvent;
import com.example.syncher.PublicEventsSyncher;
import com.example.utills.StringUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.example.utills.StringUtils.getFormatedDateFromServerFormatedDate;

public class PublicEventDetailsActivity extends BaseActivity implements View.OnClickListener, OnMapReadyCallback {

    TextView title, seatsCount,emptyView;
    Button bookNow;
    ImageView publicClose;
    LinearLayout layoutLocation, layoutMap, btnSub, BtnAdd;
    RecyclerView similarEvents;
    List<PublicEvent> similarEventsList = new ArrayList<PublicEvent>();
    PublicEventsSyncher publicEventSyncher = new PublicEventsSyncher();
    SimilarEventsAdapter adapter;
    PublicEvent publicEvent;
    boolean isMap;
    int noOfSeats = 2;
    Integer eventId, cityId;
    private GoogleMap map;
    SupportMapFragment mapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_event_details);
        addToolbarView();
        eventId = getIntent().getIntExtra("eventId", 0);
        cityId = getIntent().getIntExtra("cityId", 0);
        title = (TextView) findViewById(R.id.toolbar_title);
        seatsCount = (TextView) findViewById(R.id.seats_count);
        emptyView = (TextView) findViewById(R.id.empty_view);
        bookNow = (Button) findViewById(R.id.book_now);
        publicClose = (ImageView) findViewById(R.id.public_close);
        similarEvents = (RecyclerView) findViewById(R.id.similarEventsLayout);
        similarEvents.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        adapter = new SimilarEventsAdapter(this, similarEventsList);
        similarEvents.setAdapter(adapter);
        layoutLocation = (LinearLayout) findViewById(R.id.layoutLocation);
        layoutMap = (LinearLayout) findViewById(R.id.layoutMap);
        btnSub = (LinearLayout) findViewById(R.id.btn_sub);
        BtnAdd = (LinearLayout) findViewById(R.id.btn_add);
        bookNow.setOnClickListener(this);
        btnSub.setOnClickListener(this);
        BtnAdd.setOnClickListener(this);
        publicClose.setOnClickListener(this);
        layoutLocation.setOnClickListener(this);
        similarEvents.addOnItemTouchListener(new RecyclerTouchListener(this,
                similarEvents, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, final int position) {
                PublicEvent similarEvent = similarEventsList.get(position);
                InvtAppPreferences.setPublicEventDetails(similarEvent);
                Intent intent = new Intent(getApplicationContext(), PublicEventDetailsActivity.class);
                intent.putExtra("eventId", similarEvent.getId());
                intent.putExtra("cityId", cityId);
                Log.d("city, eventId", cityId+" "+similarEvent.getId());
                startActivity(intent);
                finish();
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        showPublicEventData();
        getSimilarEvents();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layoutLocation:
                if(!isMap){
                    if(publicEvent!=null&&publicEvent.getLatitude()!=0&&publicEvent.getLongitude()!=0) {
                        layoutMap.setVisibility(View.VISIBLE);
                        isMap = true;
                    }else{
                        Toast.makeText(getApplicationContext(), "Lat, Long not provided", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    layoutMap.setVisibility(View.GONE);
                    isMap = false;
                }
                break;
            case R.id.btn_sub:
                if(noOfSeats>0) {
                    noOfSeats--;
                    seatsCount.setText(""+noOfSeats);
                }
                break;
            case R.id.btn_add:
                noOfSeats ++;
                seatsCount.setText(""+noOfSeats);
                break;
            case R.id.book_now:
                Toast.makeText(getApplicationContext(), "to be implement", Toast.LENGTH_LONG).show();
                break;
            case R.id.public_close:
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra("eventId", eventId);
                        setResult(Activity.RESULT_OK,intent);
                        finish();
                    }
                });
                alertDialog.setNegativeButton("No", null);
                alertDialog.setMessage("Do you want to delete?");
                alertDialog.show();
                break;
        }
    }

    private void showPublicEventData() {
        publicEvent = InvtAppPreferences.getPublicEventDetails();
        TextView eventDate = (TextView) findViewById(R.id.event_date);
        TextView eventTime = (TextView) findViewById(R.id.event_time);
        TextView eventLocation = (TextView) findViewById(R.id.event_address);
        ImageView eventImage = (ImageView) findViewById(R.id.eventImage);
        ImageView publicFavourite = (ImageView) findViewById(R.id.public_favourite);
        ImageView publicFriendsAttending = (ImageView) findViewById(R.id.public_friendsAttending);
        title.setText(publicEvent.getEventName());
        if (publicEvent.getImage() != null && !publicEvent.getImage().isEmpty()) {
            Picasso.with(getApplicationContext()).load(publicEvent.getImage()).placeholder(R.drawable.event_picture).into(eventImage);
        }
        try {
            eventDate.setText(StringUtils.getEventDateFormat(getFormatedDateFromServerFormatedDate(publicEvent.getStartRime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            eventTime.setText(StringUtils.getEventTimeFormat(getFormatedDateFromServerFormatedDate(publicEvent.getStartRime()), getFormatedDateFromServerFormatedDate(publicEvent.getEndTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (publicEvent.getAddress() != null && !publicEvent.getAddress().isEmpty()) {
            eventLocation.setText(publicEvent.getAddress());
        }
        if(publicEvent.isFavourite()){
            publicFavourite.setColorFilter(getResources().getColor(R.color.happening_orange));
        }else{
            publicFavourite.setColorFilter(getResources().getColor(R.color.darkgray));
        }
        if(publicEvent.isFriendsAttending()){
            publicFriendsAttending.setColorFilter(getResources().getColor(R.color.happening_orange));
        }else{
            publicFriendsAttending.setColorFilter(getResources().getColor(R.color.darkgray));
        }
    }

    private void getSimilarEvents() {
        if (MobileHelper.isNetworkAvailable(getApplicationContext())) {
            new InvtAppAsyncTask(this) {

                @Override
                public void process() {
                    similarEventsList = publicEventSyncher.getSimilarEvents(eventId, cityId);
                }

                @Override
                public void afterPostExecute() {
                    if(similarEventsList!=null) {
                        adapter.updateAdapter(similarEventsList);
                        if (similarEventsList.size() == 0) {
                            emptyView.setVisibility(View.VISIBLE);
                        } else {
                            emptyView.setVisibility(View.GONE);
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "no trending events", Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        }else{
            Toast.makeText(getApplicationContext(), "Network problem", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(publicEvent!=null&&publicEvent.getLatitude()!=0&&publicEvent.getLongitude()!=0) {
            map = googleMap;
            LatLng location = new LatLng(publicEvent.getLatitude(), publicEvent.getLongitude());
            map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker_default)).position(location).title(publicEvent.getAddress())).setVisible(true);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 5));
        }
    }
}
