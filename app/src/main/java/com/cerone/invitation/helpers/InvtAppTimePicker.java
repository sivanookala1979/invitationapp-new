package com.cerone.invitation.helpers;

import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import java.util.Calendar;

public class InvtAppTimePicker implements TimePickerDialog.OnTimeSetListener {

    TextView textView1;
    TextView textView2;
    TextView textView3;
    Calendar calendar;

    public InvtAppTimePicker(TextView textView1, TextView textView2, TextView textView3, Calendar calendar) {
        this.textView1 = textView1;
        this.textView2 = textView2;
        this.textView3 = textView3;
        this.calendar = calendar;
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        if (textView1 != null && textView2 != null && textView3 != null && calendar != null) {
            textView1.setText(getValidText(calendar.get(Calendar.HOUR_OF_DAY)));
            textView2.setText(getValidText(calendar.get(Calendar.MINUTE)));
            textView3.setText(getMeridiem(calendar.get(Calendar.AM_PM)));
        }
    }

    private String getValidText(int number) {
        return (number >= 10) ? number + "" : "0" + number;
    }

    public void createAndUpdateTime(String time, Context context) {
        setTime(time);
        new TimePickerDialog(context, this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    private void setTime(String time) {
        Log.d("Time", time);
        if (time != null && !time.isEmpty()) {
            String timeInfo[] = time.split(":");
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeInfo[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(timeInfo[1]));
            textView1.setText(getValidText(calendar.get(Calendar.HOUR_OF_DAY)));
            textView2.setText(getValidText(calendar.get(Calendar.MINUTE)));
            textView3.setText(getMeridiem(calendar.get(Calendar.AM_PM)));
        }
    }

    private static String getMeridiem(int hourOfDay) {
        return (hourOfDay < 12) ? "AM" : "PM";
    }
}
