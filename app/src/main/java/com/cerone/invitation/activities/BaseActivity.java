package com.cerone.invitation.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.adapter.CurrencyCountryNameAdapter;
import com.cerone.invitation.adapter.ImageDialogAdapter;
import com.cerone.invitation.helpers.FontTypes;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.service.MyService;
import com.cerone.invitation.service.NotificationService;
import com.example.dataobjects.CurrencyTypes;
import com.example.dataobjects.Event;
import com.example.dataobjects.Invitation;
import com.example.dataobjects.ScreenTab;
import com.example.dataobjects.ServerResponse;
import com.example.dataobjects.ServiceInformation;
import com.example.syncher.BaseSyncher;
import com.example.syncher.EventSyncher;
import com.example.syncher.InvitationSyncher;
import com.example.utills.InvitationAppConstants;
import com.example.utills.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    public String locationPermission = "";
    public Event eventDetails;
    public static final String LOCATION = "LOCATION";
    public static final String DISTANCE = "DISTANCE";
    public static final String NOTHING = "NOTHING";

    public static final int GOOGLE_MAPS_REQUEST = 1;
    InvtAppAsyncTask invtAppAsyncTask;
    List<Event> events = new ArrayList<Event>();
    String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    List<ScreenTab> screenTabs = new ArrayList<ScreenTab>();
    List<LinearLayout> tabViews = new ArrayList<LinearLayout>();


    public List<ScreenTab> getScreenTabs(int index) {
        List<ScreenTab> screenTabs = new ArrayList<ScreenTab>();
        if (index == 0) {
            screenTabs.add(new ScreenTab(1, "Private", R.drawable.private_icon));
            screenTabs.add(new ScreenTab(0, "Public", R.drawable.public_icon));
        } else {
            screenTabs.add(new ScreenTab(2, "Event", R.drawable.event));
            screenTabs.add(new ScreenTab(3, "Chat", R.drawable.event));
        }
        return screenTabs;
    }

    public void checkUserPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissionsList = new ArrayList<String>();
            for (String permission : PERMISSIONS) {
                if (!hasPermissions(this, permission)) {
                    permissionsList.add(permission);
                }
            }
            if (permissionsList.size() > 0) {
                ActivityCompat.requestPermissions(this, (String[]) permissionsList.toArray(new String[permissionsList.size()]), 99);
            }
        }
    }

    public static boolean hasPermissions(Context context, String permission) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permission != null) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission", "" + permission + " false");
                return false;
            }
        }
        Log.d("Permission", "status true");
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setFontType(int... viewId) {
        FontTypes fontType = new FontTypes(getApplicationContext());
        for (int element : viewId) {
            TextView textview = (TextView) findViewById(element);
            fontType.setTypeface(textview);
        }
    }

    public void addToolbarView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        setSupportActionBar(toolbar);
        setFontType(R.id.toolbar_title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });
    }

    public void closeActivity() {
        finish();
    }

    public void executingTask(InvtAppAsyncTask invtAppAsyncTask) {
        this.invtAppAsyncTask = invtAppAsyncTask;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (invtAppAsyncTask != null) {
            invtAppAsyncTask.cancel(true);
        }
    }

    public void visibleLayout(View... view) {
        for (View widget : view) {
            widget.setVisibility(View.VISIBLE);
        }
    }

    public void setLoginDetails(ServerResponse serverResponse) {
        InvtAppPreferences.setLoggedIn(true);
        InvtAppPreferences.setOwnerId(serverResponse.getId());
        BaseSyncher.setAccessToken(serverResponse.getToken());
        InvtAppPreferences.setAccessToken(serverResponse.getToken());
    }

    public String getCountryZipCode() {
        String CountryID = "";
        String CountryZipCode = "";//"91";
        //                TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //                CountryID = manager.getSimCountryIso().toUpperCase();
        //                String[] rl = this.getResources().getStringArray(R.array.CountryCodes);
        //                for (String element : rl) {
        //                    String[] g = element.split(",");
        //                    if (g[1].trim().equals(CountryID.trim())) {
        //                        CountryZipCode = g[0];
        //                        break;
        //                    }
        //                }
        return CountryZipCode;
    }

    int getCountryIndex(String country, List<CurrencyTypes> currencyTypes) {
        int result = 0;

        for (int i = 0; i < currencyTypes.size(); i++) {

            if (country.equalsIgnoreCase(currencyTypes.get(i).getSysCountryCode())) {
                result = i;
                break;
            }

        }
        return result;
    }

    public void closePreviousServices() {
        List<ServiceInformation> serviceDetailsList = InvtAppPreferences.getServiceDetails();
        for (int i = 0; i < serviceDetailsList.size(); i++) {
            MyService service = new MyService();
            service.CancelAlarm(getApplicationContext(), i);
            NotificationService notificationService = new NotificationService();
            notificationService.CancelAlarm(getApplicationContext(), i);
        }
        InvtAppPreferences.setServiceDetails(new ArrayList<ServiceInformation>());
    }
    public void activateService() {
        closePreviousServices();
        Log.d("Data", "Service started at " + InvtAppPreferences.getAccessToken() + " " + InvtAppPreferences.isLoggedIn());
        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                InvitationSyncher invitationSyncher = new InvitationSyncher();
                List<Invitation> myInvitations = invitationSyncher.getMyInvitations();
                if (myInvitations.size() > 0) {
                    EventSyncher eventSyncher = new EventSyncher();
                    String eventIds = StringUtils.getAcceptedEventIds(myInvitations);
                    if (eventIds.length() > 0) {
                        events = eventSyncher.getMyEventsByIds(eventIds);
                    }
                    events = StringUtils.getFutureEvents(events);
                    StringUtils.sortEventsBadsedonStartDate(events);
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if (events.size() > 0) {
                    List<ServiceInformation> servicesList = new ArrayList<ServiceInformation>();
                    for (Event event : events) {
                        //Event event = events.get(0);
                        Log.d("Data Event", "Name " + event.getName() + "Event start date " + event.getStartDateTime() + " end date " + event.getEndDateTime());
                        ServiceInformation information = new ServiceInformation();
                        information.setCheckInNotificationServiceStartTime(StringUtils.getNewDate(event.getStartDateTime(), -10));
                        information.setServiceStartTime(StringUtils.getNewDate(event.getStartDateTime(), -30));
                        information.setServieEndTime(StringUtils.getNewDate(event.getStartDateTime(), 15));
                        information.setEventStartTime(event.getStartDateTime());
                        information.setEnventEndTime(event.getEndDateTime());
                        information.setEvent(true);
                        information.setEventInfo(event);
                        information.printServiceInformation();
                        servicesList.add(information);
                    }
                    InvtAppPreferences.setServiceDetails(servicesList);
                    startService();
                } else {
                    Log.d("Data", "No future events found");
                }
            };
        }.execute();
    }

    private void startService() {
        List<ServiceInformation> serviceDetailsList = InvtAppPreferences.getServiceDetails();
        int tag = 0;
        for (ServiceInformation serviceDetails : serviceDetailsList) {
            if (serviceDetails.isEvent()) {
                Log.d("Data", "Service started at " + StringUtils.getCurrentDate());
                Intent myIntent = new Intent(this, MyService.class);
                myIntent.setAction(serviceDetails.getServiceStartTime());
                myIntent.putExtra("flag", tag);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, tag, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Calendar cal = Calendar.getInstance();
                cal.setTime(StringUtils.StringToDate(serviceDetails.getServiceStartTime()));
                alarmManager.cancel(pendingIntent);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 60000 * 2, pendingIntent);
            }
            tag++;
        }
    }

    public void doExit() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.setNegativeButton("No", null);
        alertDialog.setMessage("Do you want to exit?");
        alertDialog.show();
    }

    public void pictureDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.login_image_dialog);
        ListView listView = (ListView) dialog.findViewById(R.id.dialogListView);
        List<String> galleryList = new ArrayList<String>();
        galleryList.add("Take Photo");
        galleryList.add("Choose From Gallery");
        final ImageDialogAdapter dialogAdapter = new ImageDialogAdapter(this, R.layout.gallery_title, galleryList);
        listView.setAdapter(dialogAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                String strName = dialogAdapter.getItem(position);
                if (strName.contains("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, InvitationAppConstants.IMAGE_CAPTURE);
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, InvitationAppConstants.IMAG_LOAD);
                }
                dialog.cancel();
            }
        });
        dialog.show();
    }

    void getCountrycodeDialog(final Button countryCodeButton,
                              final EditText mobileNumber, Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.country_code_filter_layout);
        FontTypes fontTypes = new FontTypes(getApplicationContext());

        ListView listView = (ListView) dialog.findViewById(R.id.dialogListView);
        EditText searchText = (EditText) dialog
                .findViewById(R.id.search_edittext);
        fontTypes.setTypeface(dialog.findViewById(R.id.heading_text),
                searchText);
        final CurrencyCountryNameAdapter dialogAdapter = new CurrencyCountryNameAdapter(
                context, R.layout.gallery_layout, InvtAppPreferences.getCurrencyCountryCodeDetails());
        listView.setAdapter(dialogAdapter);
        searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                dialogAdapter.getFilter().filter(s.toString());
                dialogAdapter.notifyDataSetChanged();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long id) {
                CurrencyTypes item = dialogAdapter.getItem(position);
                countryCodeButton.setText(item.getCountryCode());
                InputFilter[] FilterArray = new InputFilter[1];
                FilterArray[0] = new InputFilter.LengthFilter(Integer
                        .parseInt(item.getMobileNumberCount()));
                mobileNumber.setFilters(FilterArray);
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public boolean validateInputDetails(TextView... view) {
        boolean status = true;
        for (TextView textView : view) {
            if (textView == null || textView.getText().toString().isEmpty()) {
                status = false;
                textView.setFocusable(true);
                break;
            }
        }
        return status;
    }

    public void setSnackBarValidation(String validation) {

        Snackbar snackbar = Snackbar.make(findViewById(R.id.main_layout), validation, Snackbar.LENGTH_LONG);
        //Changing action button text color
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.BLACK);
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        FontTypes fontTypes = new FontTypes(getApplicationContext());
        fontTypes.setTypeface(sbView.findViewById(android.support.design.R.id.snackbar_text));
        snackbar.show();
    }


    @Override
    public void onClick(View v) {

    }

    public static Bitmap getRoundedCornerBitmap(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        int borderColor = ColorUtils.setAlphaComponent(Color.WHITE, 0xFF);
        int borderRadius = 0;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);

        Paint paintBg = new Paint();
        paintBg.setColor(borderColor);
        paintBg.setAntiAlias(true);

        canvas.drawCircle(r, r, r, paintBg);
        canvas.drawCircle(r, r, r - borderRadius, paint);
        squaredBitmap.recycle();

        return bitmap;
    }

    public void showLocationPermissionDialog() {
        final Dialog dialog = new Dialog(getApplicationContext());
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
}