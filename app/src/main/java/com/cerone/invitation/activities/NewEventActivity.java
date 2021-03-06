package com.cerone.invitation.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.CircleTransform;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppDatePicker;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.InvtAppTimePicker;
import com.cerone.invitation.helpers.InvtTextWatcher;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class NewEventActivity extends BaseActivity implements OnClickListener {

    LinearLayout startDateLayout;
    InvtAppTimePicker startTimePicker, endTimePicker;
    InvtAppDatePicker startDatePicker, endDatePicker;
    EditText startTime, startDate, extraAddress, endDate, endTime, eventName, description, theme;
    TextView eventAddress;
    String eventPictureInfo = "";
    ServerResponse createEvent;
    CheckBox manualCheckIn, recurring;
    Spinner recurringInfo;
    String recurringData[] = {
            "Daily",
            "Weekly",
            "Monthly"};
    Event event = new Event();
    Button editOrCreate, shareEvent;
    ImageView cameraIcon;
    TextInputLayout eventNameInput, eventDescriptionInput, eventThemeInput, extraAddressInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event_layout);
        addToolbarView();
        setFontType(R.id.txt_eventPicture, R.id.eventName, R.id.description, R.id.manualCheckIn, R.id.recurring, R.id.eventAddress, R.id.extraAddress, R.id.startDate,
                R.id.startTime, R.id.endDate, R.id.endTime, R.id.txt_startDatetime, R.id.txt_endDateTime, R.id.getLocation, R.id.createEvent, R.id.shareEvent, R.id.Cancel);
        startDateLayout = (LinearLayout) findViewById(R.id.startTimeLayout1);
        eventNameInput = (TextInputLayout) findViewById(R.id.eventNameInput);
        eventDescriptionInput = (TextInputLayout) findViewById(R.id.eventDescriptionInput);
        extraAddressInput = (TextInputLayout) findViewById(R.id.extraAddressInput);
        eventThemeInput = (TextInputLayout) findViewById(R.id.eventThemeInput);
        cameraIcon = (ImageView) findViewById(R.id.cameraIcon);
        cameraIcon.setOnClickListener(this);
        startTime = (EditText) findViewById(R.id.startTime);
        startDate = (EditText) findViewById(R.id.startDate);
        endDate = (EditText) findViewById(R.id.endDate);
        endTime = (EditText) findViewById(R.id.endTime);
        extraAddress = (EditText) findViewById(R.id.extraAddress);
        editOrCreate = (Button) findViewById(R.id.createEvent);
        shareEvent = (Button) findViewById(R.id.shareEvent);
        eventName = (EditText) findViewById(R.id.eventName);
        description = (EditText) findViewById(R.id.description);
        theme = (EditText) findViewById(R.id.theam);
        eventName.addTextChangedListener(new InvtTextWatcher(eventName, eventNameInput, "Event name should not be empty."));
        description.addTextChangedListener(new InvtTextWatcher(description, eventDescriptionInput, "Event name should not be empty."));
        extraAddress.addTextChangedListener(new InvtTextWatcher(extraAddress, extraAddressInput, ""));
        eventAddress = (TextView) findViewById(R.id.eventAddress);
        manualCheckIn = (CheckBox) findViewById(R.id.manualCheckIn);
        recurring = (CheckBox) findViewById(R.id.recurring);
        recurringInfo = (Spinner) findViewById(R.id.recurringInfo);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_text_view, recurringData);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_text_view);
        recurringInfo.setAdapter(arrayAdapter);
        startTime.setText(getCurrentTime(30));
        startDate.setText(getCurrentDate());
        endTime.setText(getCurrentTime(90));
        endDate.setText(getCurrentDate());
//        startTimePicker = new InvtAppTimePicker(startTime, Calendar.getInstance());
//        endTimePicker = new InvtAppTimePicker(endTime, Calendar.getInstance());
//        startDatePicker = new InvtAppDatePicker(startDate, Calendar.getInstance(), endDate, true);
//        endDatePicker = new InvtAppDatePicker(endDate, Calendar.getInstance(), startDate, false);
        Event eventDetails = InvtAppPreferences.getEventDetails();
        if (eventDetails != null) {
            shareEvent.setVisibility(View.GONE);
            editOrCreate.setText("Update");
            eventName.setText(eventDetails.getName());
            description.setText(eventDetails.getDescription());
            theme.setText(eventDetails.getTheme());
            manualCheckIn.setChecked(eventDetails.isManualCheckIn());
            recurring.setChecked(eventDetails.isRecurring());
            recurringInfo.setVisibility(eventDetails.isRecurring() ? View.VISIBLE : View.GONE);
            startTime.setText(StringUtils.formatDateAndTime(eventDetails.getStartDateTime(), 4));
            startDate.setText(StringUtils.formatDateAndTime(eventDetails.getStartDateTime(), 3));
            endTime.setText(StringUtils.formatDateAndTime(eventDetails.getEndDateTime(), 4));
            endDate.setText(StringUtils.formatDateAndTime(eventDetails.getEndDateTime(), 3));
            event.setEventId(eventDetails.getEventId());
            event.setLatitude(eventDetails.getLatitude());
            if(eventDetails.getImageUrl()!=null && !eventDetails.getImageUrl().isEmpty()) {
                Picasso.with(getApplicationContext()).load(eventDetails.getImageUrl()).transform(new CircleTransform()).into(cameraIcon);
                loadBitmap(eventDetails.getImageUrl());
            }
            event.setLongitude(eventDetails.getLongitude());
            event.setAddress(eventDetails.getAddress());
            extraAddress.setText(eventDetails.getExtraAddress() + "");
            event.setExtraAddress(eventDetails.getExtraAddress());
            if (event.getAddress() != null && event.getAddress().length() > 0) {
                eventAddress.setText(event.getAddress());
            }
        }
        loadMostRecentAddress();
        setlisteners(recurring, startDateLayout, startTime, startDate, endDate, endTime);
        createAndSetOnclickListeners(R.id.createEvent, R.id.shareEvent, R.id.Cancel, R.id.getLocation, R.id.startDateLogo, R.id.endDateLogo, R.id.startTimeLogo, R.id.endTimeLogo);
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
        new InvtAppAsyncTask(NewEventActivity.this) {

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
            case R.id.startTimeLogo :
            case R.id.startTime :
            case R.id.startTimeLayout1 :
                startTimePicker.createAndUpdateTime(startTime.getText().toString(), NewEventActivity.this);
                endTime.setText(updateEndTime(startTime.getText().toString(),2));
                break;
            case R.id.startDateLayout :
            case R.id.startDate :
            case R.id.startDateLogo :
                //startDatePicker.createAndUpdateDate(startDate, startDate.getText().toString(), NewEventActivity.this);
                break;
            case R.id.endDate :
            case R.id.endDateLayout :
            case R.id.endDateLogo :
                //endDatePicker.createAndUpdateDate(endDate, endDate.getText().toString(), NewEventActivity.this);
                break;
            case R.id.endTimeLayout :
            case R.id.endTime :
            case R.id.endTimeLogo :
                //endTimePicker.createAndUpdateTime(endTime.getText().toString(), NewEventActivity.this);
                break;
            case R.id.getLocation :
                Intent locationIntent = new Intent(getApplicationContext(), LocationActivity.class);
                startActivityForResult(locationIntent, GOOGLE_MAPS_REQUEST);
                break;
            case R.id.cameraIcon :
                pictureDialog();
                break;
            case R.id.createEvent :
                collectEventData();
                validateAndPostEvent();
                break;
            case R.id.shareEvent :
                collectEventData();
                if (isEventDataValid()) {
                    InvtAppPreferences.setEventDetails(event);
                    Intent intent = new Intent(this, ShareEventActivity.class);
                    intent.putExtra("newEvent", true);
                    startActivity(intent);
                } else {
                    ToastHelper.redToast(getApplicationContext(), "Fields should not be empty");
                }
                break;
            case R.id.recurring :
                if (recurringInfo.getVisibility() == View.VISIBLE) {
                    recurringInfo.setVisibility(View.GONE);
                } else {
                    recurringInfo.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.Cancel :
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
                    if ( createEvent.getId() > 0) {
                        Toast.makeText(getApplicationContext(), "Event Created.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(NewEventActivity.this, HomeScreenActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        ToastHelper.redToast(getApplicationContext(), createEvent.getStatus());
                    }
                }
            }.execute();
        } else {
            ToastHelper.redToast(getApplicationContext(), "Fields should not be empty");
        }
    }

    private void collectEventData() {
        event.setDescription(description.getText().toString());
        event.setName(eventName.getText().toString());
        String startDateInfo = startDate.getText() + " " + startTime.getText() + ":00";
        String endDateInfo = endDate.getText() + " " + endTime.getText() + ":00";
        event.setStartDateTime(startDateInfo);
        event.setEndDateTime(endDateInfo);
        event.setManualCheckIn(manualCheckIn.isChecked());
        event.setRecurring(recurring.isChecked());
        event.setTheme(theme.getText().toString());
        event.setRecurringType(recurringInfo.getSelectedItem().toString());
        event.setExtraAddress(extraAddress.getText().toString());
        event.setImageData(eventPictureInfo);
    }

    private boolean isEventDataValid() {
        if(event.getAddress()==null || event.getAddress().length() == 0) {
            event.setAddress("");
            event.setLatitude(0.0);
            event.setLongitude(0.0);
        }
        return true;//event.getDescription().length() > 0 && event.getName().length() > 0 && event.getAddress() != null && event.getAddress().length() > 0;
    }

    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return sdf.format(new Date());
    }

    public static String getCurrentTime(int postMinutes) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE,postMinutes);
        return getValidText(now.get(Calendar.HOUR_OF_DAY)) + ":" + getValidText(now.get(Calendar.MINUTE));
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
            updateLocationDetails();
        }
        if ((requestCode == InvitationAppConstants.IMAG_LOAD || requestCode == InvitationAppConstants.IMAGE_CAPTURE) && resultCode == RESULT_OK) {
            Bitmap bitmap;
            if (requestCode == InvitationAppConstants.IMAGE_CAPTURE) {
                bitmap = (Bitmap) data.getExtras().get("data");
            } else {
                bitmap = MobileHelper.getBitmapFromCameraData(data, NewEventActivity.this);
            }
            if (bitmap != null) {
                cameraIcon.setImageBitmap(getRoundedCornerBitmap(bitmap));
                eventPictureInfo = MobileHelper.BitMapToString(bitmap);
            }
        }
    }

    private void updateLocationDetails() {
        if (event.getAddress() != null && event.getAddress().length() > 0) {
            eventAddress.setText(event.getAddress());
        } else {
            eventAddress.setText("");
            eventAddress.setHint("Pick Location");
        }
    }

    private static String updateEndTime(String startTime, int upTime){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = formatter.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, upTime);
        String time = getValidText(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + getValidText(calendar.get(Calendar.MINUTE));
        return time;
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