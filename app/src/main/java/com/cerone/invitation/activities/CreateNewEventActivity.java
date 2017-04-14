package com.cerone.invitation.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppDatePicker;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.InvtAppTimePicker;
import com.cerone.invitation.helpers.MobileHelper;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.Event;
import com.example.dataobjects.ServerResponse;
import com.example.dataobjects.UserLocation;
import com.example.syncher.EventSyncher;
import com.example.utills.InvitationAppConstants;
import com.example.utills.StringUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.cerone.invitation.R.id.layout_start_date;


public class CreateNewEventActivity extends BaseActivity implements OnClickListener {

    LinearLayout layoutStartDate, layoutStartTime, layoutEndDate, layoutEndTime, layoutCreateEvent, layoutShareEvent, layoutCancel;
    InvtAppTimePicker startTimePicker, endTimePicker;
    InvtAppDatePicker startDatePicker, endDatePicker;
    TextView startDay, startMonth, startYear, startHour, startMin, startMeridiem,
            endDay, endMonth, endYear, endHour, endMin, endMeridiem, toolbarTitle;
    EditText eventName, eventLocation, extraAddress;
    String eventPictureInfo = "", eventTitle;
    ServerResponse createEvent;
    Spinner recurringInfo;
    String recurringData[] = {
            " Does not repeate",
            " Every day",
            " Every week",
            " Every month"};
    Event event = new Event();
    ImageView eventImage, getLocation,imageCamera;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        addToolbarView();
        setFontType(R.id.event_name, R.id.event_address, R.id.start_day, R.id.start_month, R.id.start_year, R.id.start_hour, R.id.start_min, R.id.start_meridiem,
                R.id.end_day, R.id.end_month, R.id.end_year, R.id.end_hour, R.id.end_min, R.id.end_meridiem, R.id.event_name, R.id.event_location, R.id.extra_address);
        layoutStartDate = (LinearLayout) findViewById(layout_start_date);
        layoutStartTime = (LinearLayout) findViewById(R.id.layout_start_time);
        layoutEndDate = (LinearLayout) findViewById(R.id.layout_end_date);
        layoutEndTime = (LinearLayout) findViewById(R.id.layout_end_time);
        layoutCreateEvent = (LinearLayout) findViewById(R.id.layout_createEvent);
        layoutShareEvent = (LinearLayout) findViewById(R.id.layout_shareEvent);
        layoutCancel = (LinearLayout) findViewById(R.id.layout_cancelEvent);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        eventName = (EditText) findViewById(R.id.event_name);
        eventLocation = (EditText) findViewById(R.id.event_location);
        extraAddress = (EditText) findViewById(R.id.extra_address);
        startDay = (TextView) findViewById(R.id.start_day);
        startMonth = (TextView) findViewById(R.id.start_month);
        startYear = (TextView) findViewById(R.id.start_year);
        startHour = (TextView) findViewById(R.id.start_hour);
        startMin = (TextView) findViewById(R.id.start_min);
        startMeridiem = (TextView) findViewById(R.id.start_meridiem);
        endDay = (TextView) findViewById(R.id.end_day);
        endMonth = (TextView) findViewById(R.id.end_month);
        endYear = (TextView) findViewById(R.id.end_year);
        endHour = (TextView) findViewById(R.id.end_hour);
        endMin = (TextView) findViewById(R.id.end_min);
        endMeridiem = (TextView) findViewById(R.id.end_meridiem);
        eventImage = (ImageView) findViewById(R.id.event_image);
        getLocation = (ImageView) findViewById(R.id.get_location);
        recurringInfo = (Spinner) findViewById(R.id.recurringInfo);
        imageCamera = (ImageView) findViewById(R.id.image_camera);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_text_view, recurringData);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_text_view);
        recurringInfo.setAdapter(arrayAdapter);
        String startDate = getCurrentDate();
        String[] currentDate = startDate.split(" ");
        startDay.setText(currentDate[0]);
        startMonth.setText(currentDate[1]);
        startYear.setText(currentDate[2]);
        endDay.setText(currentDate[0]);
        endMonth.setText(currentDate[1]);
        endYear.setText(currentDate[2]);
        String[] currentStartTime = getCurrentTime(30).split(" ");
        startHour.setText(currentStartTime[0]);
        startMin.setText(currentStartTime[1]);
        startMeridiem.setText(currentStartTime[2]);
        String[] currentEndTime = getCurrentTime(90).split(" ");
        endHour.setText(currentEndTime[0]);
        endMin.setText(currentEndTime[1]);
        endMeridiem.setText(currentEndTime[2]);
        Event eventDetails = InvtAppPreferences.getEventDetails();
        if (eventDetails != null) {
            layoutShareEvent.setVisibility(View.GONE);
            eventName.setText(eventDetails.getName());
            String[] s1 = StringUtils.formatDateAndTime(eventDetails.getStartDateTime(), 3).split(" ");
            startDay.setText(s1[0]);
            startMonth.setText(s1[1]);
            startYear.setText(s1[2]);
            String[] s2 = StringUtils.formatDateAndTime(eventDetails.getStartDateTime(), 4).split(" ");
            startHour.setText(s2[0]);
            startMin.setText(s2[1]);
            startMeridiem.setText(s2[2]);
            String[] s3 = StringUtils.formatDateAndTime(eventDetails.getEndDateTime(), 3).split(" ");
            endDay.setText(s3[0]);
            endMonth.setText(s3[1]);
            endYear.setText(s3[2]);
            String[] s4 = StringUtils.formatDateAndTime(eventDetails.getEndDateTime(), 4).split(" ");
            endHour.setText(s4[0]);
            endMin.setText(s4[1]);
            endMeridiem.setText(s4[2]);
            if(eventDetails.getImageUrl()!=null && !eventDetails.getImageUrl().isEmpty()) {
                Picasso.with(getApplicationContext()).load(eventDetails.getImageUrl()).into(eventImage);
                loadBitmap(eventDetails.getImageUrl());
                imageCamera.setVisibility(View.GONE);
            }
            event.setEventId(eventDetails.getEventId());
            event.setLatitude(eventDetails.getLatitude());
            event.setLongitude(eventDetails.getLongitude());
            event.setAddress(eventDetails.getAddress());
            if (event.getAddress() != null && event.getAddress().length() > 0) {
                eventLocation.setText(event.getAddress());
            }
        }
        loadMostRecentAddress();
        setlisteners(layoutStartDate, layoutStartTime, layoutEndDate, layoutEndTime, layoutCreateEvent, layoutShareEvent, layoutCancel, eventImage, getLocation);
        createAndSetOnclickListeners(layout_start_date, R.id.layout_start_time, R.id.layout_end_date, R.id.layout_end_time, R.id.layout_createEvent, R.id.layout_shareEvent, R.id.layout_cancelEvent,
                R.id.event_image, R.id.get_location);

        eventName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                eventTitle = eventName.getText().toString();
                toolbarTitle.setText(eventTitle);
                if(eventTitle.isEmpty()){
                    toolbarTitle.setText("New Event");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
    }
    private Target loadtarget;

    public void loadBitmap(String url) {

        if (loadtarget == null) loadtarget = new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                handleLoadedBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(this).load(url).into(loadtarget);
    }

    public void handleLoadedBitmap(Bitmap bitmap) {
        eventPictureInfo = MobileHelper.BitMapToString(bitmap);
    }
    private void loadMostRecentAddress() {
        new InvtAppAsyncTask(CreateNewEventActivity.this) {

            @Override
            public void process() {
                InvtAppPreferences.setRecentAddressDetails(getDefaultAddress());
            }

            @Override
            public void afterPostExecute() {
            }
        }.execute();
    }

    void createAndSetOnclickListeners(int... ids) {
        for (int id : ids) {
            findViewById(id).setOnClickListener(this);
        }
    }

    void setlisteners(View... view) {
        for (View view2 : view) {
            view2.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        switch (v.getId()) {
            case R.id.layout_start_date :
                String endDate = endDay.getText().toString()+" "+endMonth.getText().toString()+" "+endYear.getText().toString();
                startDatePicker = new InvtAppDatePicker(startDay, startMonth, startYear, endDay, endMonth, endYear, Calendar.getInstance(), endDate, true);
                startDatePicker.createAndUpdateDate(startDay,startMonth,startYear, startDay.getText().toString()+" "+startMonth.getText().toString()+" "+startYear.getText().toString(), CreateNewEventActivity.this);
                break;
            case R.id.layout_end_date :
                String startDate = startDay.getText().toString()+" "+startMonth.getText().toString()+" "+startYear.getText().toString();
                endDatePicker = new InvtAppDatePicker(startDay, startMonth, startYear, endDay, endMonth, endYear, Calendar.getInstance(), startDate, false);
                endDatePicker.createAndUpdateDate(endDay,endMonth,endYear, endDay.getText().toString()+" "+endMonth.getText().toString()+" "+endYear.getText().toString(), CreateNewEventActivity.this);
                break;
            case R.id.layout_start_time :
                startTimePicker = new InvtAppTimePicker(startHour, startMin, startMeridiem, endHour, endMin, endMeridiem, Calendar.getInstance(), true);
                startTimePicker.createAndUpdateTime(startHour.getText().toString()+" "+startMin.getText().toString()+" "+startMeridiem.getText().toString(), CreateNewEventActivity.this);
                break;
            case R.id.layout_end_time :
                endTimePicker = new InvtAppTimePicker(startHour, startMin, startMeridiem, endHour, endMin, endMeridiem, Calendar.getInstance(), false);
                endTimePicker.createAndUpdateTime(endHour.getText().toString()+" chac"+endMin.getText().toString()+" "+endMeridiem.getText().toString(), CreateNewEventActivity.this);
                break;
            case R.id.get_location :
                Intent locationIntent = new Intent(getApplicationContext(), LocationActivity.class);
                startActivityForResult(locationIntent, GOOGLE_MAPS_REQUEST);
                break;
            case R.id.event_image :
                pictureDialog();
                break;
            case R.id.layout_createEvent :
            case R.id.create :
                collectEventData();
                validateAndPostEvent();
                break;
            case R.id.layout_shareEvent :
            case R.id.share :
                collectEventData();
                if (isEventDataValid()) {
                    InvtAppPreferences.setEventDetails(event);
                    Intent intent = new Intent(this, ShareEventActivity.class);
                    intent.putExtra("newEvent", true);
                    startActivity(intent);
                }
                break;
            case R.id.layout_cancelEvent :
            case R.id.cancel :
                finish();
                break;
        }
    }

    public void validateAndPostEvent() {
        if (isEventDataValid()) {
            new InvtAppAsyncTask(this) {

                @Override
                public void process() {
                    EventSyncher eventSyncher = new EventSyncher();
                    createEvent = eventSyncher.createEvent(event);
                }

                @Override
                public void afterPostExecute() {
                    if ( createEvent != null && createEvent.getId() > 0) {
                        Toast.makeText(getApplicationContext(), "Event Created.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(CreateNewEventActivity.this, HomeScreenActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        ToastHelper.redToast(getApplicationContext(), createEvent.getStatus());
                    }
                }
            }.execute();
        }
    }

    private void collectEventData() {
        event.setName(eventName.getText().toString());
        String startDateInfo = startDay.getText().toString()+"-"+startMonth.getText().toString()+"-"+startYear.getText().toString() + " " + startHour.getText().toString()+":"+startMin.getText().toString() +" "+startMeridiem.getText().toString();
        String endDateInfo = endDay.getText().toString()+"-"+endMonth.getText().toString()+"-"+endYear.getText().toString() + " " + endHour.getText().toString()+":"+endMin.getText().toString() +" "+endMeridiem.getText().toString();
        event.setStartDateTime(startDateInfo);
        event.setEndDateTime(endDateInfo);
        event.setImageData(eventPictureInfo);
        event.setRecurringType(recurringInfo.getSelectedItem().toString());
        event.setExtraAddress(extraAddress.getText().toString());
    }

    private boolean isEventDataValid() {
        boolean status = true;
        if(event.getAddress()==null || event.getAddress().length() == 0) {
            event.setAddress("");
            event.setLatitude(0.0);
            event.setLongitude(0.0);
        }
        if(event.getName()==null || event.getName().isEmpty()){
            status = false;
            ToastHelper.redToast(getApplicationContext(),"Event name should not be empty.");
        }
        return status;
    }

    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy", Locale.US);
        return sdf.format(new Date());
    }

    public static String getCurrentTime(int postMinutes) {

        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE,postMinutes);
        Format formatter = new SimpleDateFormat("hh mm a");
        return formatter.format(now.getTime());
    }

    private static String getValidText(int number) {
        return (number >= 10) ? number + "" : "0" + number;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != InvitationAppConstants.IMAG_LOAD && requestCode != InvitationAppConstants.IMAGE_CAPTURE && requestCode == GOOGLE_MAPS_REQUEST) {
            UserLocation eventLocationDetails = InvtAppPreferences.getEventLocationDetails();
            event.setLatitude(eventLocationDetails.getLatitude());
            event.setLongitude(eventLocationDetails.getLongitude());
            event.setAddress(eventLocationDetails.getAddress());
            updateLocationDetails(count);
            count++;
        }
        if ((requestCode == InvitationAppConstants.IMAG_LOAD || requestCode == InvitationAppConstants.IMAGE_CAPTURE) && resultCode == RESULT_OK) {
            Bitmap bitmap;
            if (requestCode == InvitationAppConstants.IMAGE_CAPTURE) {
                bitmap = (Bitmap) data.getExtras().get("data");
            } else {
                bitmap = MobileHelper.getBitmapFromCameraData(data, CreateNewEventActivity.this);
            }
            if (bitmap != null) {
                eventImage.setImageBitmap(bitmap);
                eventPictureInfo = MobileHelper.BitMapToString(bitmap);
                imageCamera.setVisibility(View.GONE);
            }
        }
    }

    private void updateLocationDetails(int i) {
        if (event.getAddress() != null && event.getAddress().length() > 0) {
            if(count == 0)
            extraAddress.setText(eventLocation.getText().toString());
            eventLocation.setText(event.getAddress());
        } else {
            eventLocation.setText("");
            eventLocation.setHint("Event Location");
        }
    }

    private static String updateEndTime(String time, int upTime){
        Calendar calendar = Calendar.getInstance();
        String timeInfo[] = time.split(":");
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeInfo[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeInfo[1]));
        calendar.add(Calendar.MINUTE, upTime);
        Format format = new SimpleDateFormat("hh mm a");
        return format.format(calendar.getTime());
    }

    public List<UserLocation> getDefaultAddress() {
        List<UserLocation> list = new ArrayList<UserLocation>();
//        UserLocation kavali = new UserLocation();
//        kavali.setLatitude(14.913181);
//        kavali.setLongitude(79.992980);
//        kavali.setAddress("Santhi Nagar, kavali");
//        list.add(kavali);
//        UserLocation nellore = new UserLocation();
//        nellore.setLatitude(14.442599);
//        nellore.setLongitude(79.986456);
//        nellore.setAddress("Nellore, nellore");
//        list.add(nellore);
//        UserLocation hyd = new UserLocation();
//        hyd.setLatitude(14.913181);
//        hyd.setLongitude(79.992980);
//        hyd.setAddress("Hyd, Hyderabad");
//        list.add(hyd);
        return list;
    }
}