package com.cerone.invitation.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cerone.invitation.helpers.InvtAppPreferences;

/**
 * Created by adarsht on 08/02/17.
 */

public class TestService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        final int flagInfo = intent.getExtras().getInt("flag");
        Log.d("TestService","Data "+flagInfo);
    }
    public void CancelAlarm(Context context,int flag) {
        Intent intent = new Intent(context, TestService.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, flag, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
