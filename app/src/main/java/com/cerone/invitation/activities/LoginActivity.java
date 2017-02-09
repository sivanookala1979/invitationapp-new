package com.cerone.invitation.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.service.MyService;
import com.cerone.invitation.service.TestService;
import com.example.syncher.BaseSyncher;
import com.example.utills.StringUtils;

import java.util.Calendar;


public class LoginActivity extends BaseActivity implements OnClickListener {

    Button loginButton, registerButton;
    boolean alreadyLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        InvtAppPreferences.setPref(getApplicationContext());
        alreadyLoggedIn = InvtAppPreferences.isLoggedIn();
        InvtAppPreferences.setServiceRefresh(false);
        if (alreadyLoggedIn) {
            BaseSyncher.setAccessToken(InvtAppPreferences.getAccessToken());
            System.out.println("Existing access Token " + BaseSyncher.getAccessToken());
            Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
            startActivity(intent);
            finish();
        }
        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }
    private void testService(String dateInfo,int tag) {
        Intent myIntent = new Intent(this, TestService.class);
        myIntent.setAction(dateInfo);
        myIntent.putExtra("flag", tag);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, tag, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar cal = Calendar.getInstance();
        cal.setTime(StringUtils.StringToDate(dateInfo));
        alarmManager.cancel(pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 60000 * 2, pendingIntent);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.loginButton :
                intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
                break;
            case R.id.registerButton :
                intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
                break;
            default :
                break;
        }
    }

    @Override
    public void onBackPressed() {
        doExit();
    }
}
