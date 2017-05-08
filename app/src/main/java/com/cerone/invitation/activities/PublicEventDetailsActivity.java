package com.cerone.invitation.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.example.dataobjects.PublicEvent;
import com.example.utills.StringUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;

import static com.example.utills.StringUtils.getFormatedDateFromServerFormatedDate;

public class PublicEventDetailsActivity extends BaseActivity implements View.OnClickListener {

    TextView title, seatsCount;
    Button bookNow;
    ImageView publicClose;
    LinearLayout layoutLocation, layoutMap, btnSub, BtnAdd;
    boolean isMap;
    int noOfSeats = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_event_details);
        addToolbarView();

        title = (TextView) findViewById(R.id.toolbar_title);
        seatsCount = (TextView) findViewById(R.id.seats_count);
        bookNow = (Button) findViewById(R.id.book_now);
        publicClose = (ImageView) findViewById(R.id.public_close);
        layoutLocation = (LinearLayout) findViewById(R.id.layoutLocation);
        layoutMap = (LinearLayout) findViewById(R.id.layoutMap);
        btnSub = (LinearLayout) findViewById(R.id.btn_sub);
        BtnAdd = (LinearLayout) findViewById(R.id.btn_add);
        bookNow.setOnClickListener(this);
        btnSub.setOnClickListener(this);
        BtnAdd.setOnClickListener(this);
        publicClose.setOnClickListener(this);
        layoutLocation.setOnClickListener(this);
        showPublicEventData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layoutLocation:
                if(!isMap){
                    layoutMap.setVisibility(View.VISIBLE);
                    isMap = true;
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
                Toast.makeText(getApplicationContext(), "to be implement", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void showPublicEventData() {
        PublicEvent publicEvent = InvtAppPreferences.getPublicEventDetails();
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
}
