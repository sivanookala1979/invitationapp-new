package com.cerone.invitation.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.cerone.invitation.R.id.layout_start_date;


public class CreateNewEventActivity extends BaseActivity implements OnClickListener {

    LinearLayout layoutStartDate, layoutEndDate, layoutCreateEvent, layoutShareEvent, layoutCancel;
    TextView toolbarTitle,extraAddress,startDateTime,endDateTime;
    EditText eventName, eventLocation ;
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
    DatePicker dp;
    TimePicker tp;
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        addToolbarView();
        setFontType(R.id.event_name, R.id.event_address, R.id.event_name, R.id.event_location);
        layoutStartDate = (LinearLayout) findViewById(R.id.layout_start_date);
        layoutEndDate = (LinearLayout) findViewById(R.id.layout_end_date);
        layoutCreateEvent = (LinearLayout) findViewById(R.id.layout_createEvent);
        layoutShareEvent = (LinearLayout) findViewById(R.id.layout_shareEvent);
        layoutCancel = (LinearLayout) findViewById(R.id.layout_cancelEvent);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        eventName = (EditText) findViewById(R.id.event_name);
        eventLocation = (EditText) findViewById(R.id.event_location);
        extraAddress = (TextView) findViewById(R.id.extra_address);
        eventImage = (ImageView) findViewById(R.id.event_image);
        getLocation = (ImageView) findViewById(R.id.get_location);
        recurringInfo = (Spinner) findViewById(R.id.recurringInfo);
        imageCamera = (ImageView) findViewById(R.id.image_camera);
        startDateTime = (TextView)findViewById(R.id.startDateTime);
        endDateTime = (TextView)findViewById(R.id.endDateTime);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_text_view, recurringData);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_text_view);
        recurringInfo.setAdapter(arrayAdapter);
        String startDate = getCurrentDate();
        String startTime = getCurrentTime(30);
        String endTime = getCurrentTime(90);
        updateDateTime(startDateTime,startDate+" "+startTime);
        updateDateTime(endDateTime,startDate+" "+endTime);
        Log.d("Date-Time",startDate+" "+startTime+" "+endTime);
        Event eventDetails = InvtAppPreferences.getEventDetails();
        if (eventDetails != null) {
            layoutShareEvent.setVisibility(View.GONE);
            eventName.setText(eventDetails.getName());
            Log.d("start Date-Time",eventDetails.getStartDateTime()+" "+eventDetails.getEndDateTime());
            startDateTime.setText(StringUtils.formatDateAndTime(eventDetails.getStartDateTime(), 6));
            endDateTime.setText(StringUtils.formatDateAndTime(eventDetails.getEndDateTime(), 6));
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
        setlisteners(layoutStartDate, layoutEndDate, layoutCreateEvent, layoutShareEvent, layoutCancel, eventImage, getLocation);
        createAndSetOnclickListeners(layout_start_date, R.id.layout_end_date, R.id.layout_createEvent, R.id.layout_shareEvent, R.id.layout_cancelEvent,
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
                startDateAndTimeDialog();
                break;
            case R.id.layout_end_date :
                endDateAndTimeDialog();
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
            if (MobileHelper.hasNetwork(getApplicationContext(), CreateNewEventActivity.this, " to create Event", null)) {
                new InvtAppAsyncTask(this) {
                    @Override
                    public void process() {
                        EventSyncher eventSyncher = new EventSyncher();
                        createEvent = eventSyncher.createEvent(event);
                    }

                    @Override
                    public void afterPostExecute() {
                        if (createEvent != null) {
                            if (createEvent.getId() > 0) {
                                Toast.makeText(getApplicationContext(), "Event Created.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(CreateNewEventActivity.this, HomeScreenActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                ToastHelper.redToast(getApplicationContext(), createEvent.getStatus());
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                }.execute();
            }
        }
    }

    private void collectEventData() {
        event.setName(eventName.getText().toString());
        event.setStartDateTime(startDateTime.getText().toString());
        event.setEndDateTime(endDateTime.getText().toString());
        event.setImageData(eventPictureInfo);
        event.setAddress(eventLocation.getText().toString());
        event.setExtraAddress(extraAddress.getText().toString());
        event.setRecurringType(recurringInfo.getSelectedItem().toString());
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        return sdf.format(new Date());
    }

    public static String getCurrentTime(int postMinutes) {

        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE,postMinutes);
        Format formatter = new SimpleDateFormat("hh:mm a");
        return formatter.format(now.getTime());
    }

    private void startDateAndTimeDialog(){
        calendar.add(Calendar.MINUTE, 30);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_datetime);
        TextView day = (TextView) dialog.findViewById(R.id.day);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        Button accept = (Button) dialog.findViewById(R.id.ok);
        dp = (DatePicker) dialog.findViewById(R.id.datePicker);
        tp = (TimePicker) dialog.findViewById(R.id.timePicker);
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = formatter.format(d);
        day.setText(dayOfTheWeek);
        String startDate = startDateTime.getText().toString();
        formatter = new SimpleDateFormat("MMM dd,yyyy EEE hh:mm a");
        try {
            calendar.setTime(formatter.parse(startDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dp.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        tp.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        tp.setCurrentMinute(calendar.get(Calendar.MINUTE));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {dialog.cancel();}
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dayOfMonth = String.valueOf(dp.getDayOfMonth());
                String month = String.valueOf(dp.getMonth()+1);
                int year = dp.getYear();
                int hourOfDay = tp.getCurrentHour();
                String minute = String.valueOf(tp.getCurrentMinute());
                String AMPM = "AM";
                if(hourOfDay>11)
                {
                    hourOfDay = hourOfDay-12;
                    if(hourOfDay==0)
                        hourOfDay = 12;
                    AMPM = "PM";
                }
                String hour = String.valueOf(hourOfDay);
                String selectedDate = dayOfMonth+"-"+month+"-"+year;
                String selectedtime = hour+":"+minute+" "+AMPM;
                String selectedDateTime = selectedDate+" "+selectedtime;
                Log.d("selectedDate-time1", selectedDateTime);
                if (StringUtils.isDateAndTimeAfterCurrentDate(selectedDateTime, 1)) {
                    updateDateTime(startDateTime,selectedDateTime);
                    String endDT = updateEndTime(selectedtime, 60);
                    String selectedEndDT = selectedDate+" "+endDT;
                    updateDateTime(endDateTime,selectedEndDT);
                }else{
                    ToastHelper.redToast(getApplicationContext(), "Start date should not be past date.");
                }
                dialog.cancel();
            }
        });
        dialog.show();
    }
    public void updateDateTime(TextView dateTimeField, String formattedDateTime) {
        dateTimeField.setText(StringUtils.getStringDateFromDate(StringUtils.getDateFromString(formattedDateTime,5),3));
    }

    private void endDateAndTimeDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_datetime);
        TextView day = (TextView) dialog.findViewById(R.id.day);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        Button accept = (Button) dialog.findViewById(R.id.ok);
        dp = (DatePicker) dialog.findViewById(R.id.datePicker);
        tp = (TimePicker) dialog.findViewById(R.id.timePicker);
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = formatter.format(d);
        day.setText(dayOfTheWeek);
        String endDate = endDateTime.getText().toString();
        formatter = new SimpleDateFormat("MMM dd,yyyy EEE hh:mm a");
        try {
            calendar.setTime(formatter.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dp.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        tp.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        tp.setCurrentMinute(calendar.get(Calendar.MINUTE));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {dialog.cancel();}
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dayOfMonth = String.valueOf(dp.getDayOfMonth());
                String month = String.valueOf(dp.getMonth()+1);
                int year = dp.getYear();
                int hourOfDay = tp.getCurrentHour();
                String minute = String.valueOf(tp.getCurrentMinute());
                String AMPM = "AM";
                if(hourOfDay>11)
                {
                    hourOfDay = hourOfDay-12;
                    if(hourOfDay==0)
                        hourOfDay = 12;
                    AMPM = "PM";
                }
                String hour = String.valueOf(hourOfDay);
                String startDT = eventDateTimeToDateTimeFormat(startDateTime.getText().toString());
                String[] startDateTime = startDT.split(" ",2);
                String selectedDate = dayOfMonth+"-"+month+"-"+year;
                String selectedtime = hour+":"+minute+" "+AMPM;
                String selectedDateTime = selectedDate+" "+selectedtime;
                Log.d("selectedDate-time2", selectedDateTime);
                if (StringUtils.validateStartAndEndDates(startDateTime[0], selectedDate)&&StringUtils.validateStartAndEndTimes(startDateTime[1], selectedtime)) {
                    updateDateTime(endDateTime,selectedDateTime);
                } else {
                    ToastHelper.redToast(getApplicationContext(), "end date & Time should not be past to start date.");
                }
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != InvitationAppConstants.IMAG_LOAD && requestCode != InvitationAppConstants.IMAGE_CAPTURE && requestCode == GOOGLE_MAPS_REQUEST) {
            UserLocation eventLocationDetails = InvtAppPreferences.getEventLocationDetails();
            event.setLatitude(eventLocationDetails.getLatitude());
            event.setLongitude(eventLocationDetails.getLongitude());
            event.setAddress(eventLocationDetails.getAddress());
            count++;
            updateLocationDetails();
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

    private void updateLocationDetails() {
        if (event.getAddress() != null && event.getAddress().length() > 0) {
            Log.d("Location","Count "+count+" new address "+eventLocation.getText());
            if(count == 1) {
                extraAddress.setText(eventLocation.getText().toString());
                extraAddress.setVisibility(View.VISIBLE);
            }
            eventLocation.setText(event.getAddress());

        } else {
            eventLocation.setText("");
            eventLocation.setHint("Event Location");
        }
    }

    private static String updateEndTime(String time, int upTime){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        try {
            calendar.setTime(formatter.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.MINUTE, upTime);
        return formatter.format(calendar.getTime());
    }

    private static String eventDateTimeToDateTimeFormat(String dateTime){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd,yyyy EEE hh:mm a");
        try {
            calendar.setTime(formatter.parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        return formatter.format(calendar.getTime());
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