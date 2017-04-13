package com.cerone.invitation.helpers;

import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InvtAppTimePicker implements TimePickerDialog.OnTimeSetListener {

    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    TextView textView6;
    Calendar calendar;
    boolean startTime;

    public InvtAppTimePicker(TextView textView1, TextView textView2, TextView textView3,
                             TextView textView4, TextView textView5, TextView textView6, Calendar calendar, boolean startTime) {
        this.textView1 = textView1;
        this.textView2 = textView2;
        this.textView3 = textView3;
        this.textView4 = textView4;
        this.textView5 = textView5;
        this.textView6 = textView6;
        this.calendar = calendar;
        this.startTime = startTime;
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        SimpleDateFormat formatter = new SimpleDateFormat("hh mm a");
        String[] time = formatter.format(calendar.getTime()).split(" ");
        Log.d("selected Time", hourOfDay +":"+minute);
        if(startTime) {
            if (textView1 != null && textView2 != null && textView3 != null && calendar != null) {
                textView1.setText(time[0]);
                textView2.setText(time[1]);
                textView3.setText(time[2]);
                calendar.add(Calendar.MINUTE, 60);
                String[] time1 = formatter.format(calendar.getTime()).split(" ");
                textView4.setText(time1[0]);
                textView5.setText(time1[1]);
                textView6.setText(time1[2]);
            }
        }else{
            textView4.setText(time[0]);
            textView5.setText(time[1]);
            textView6.setText(time[2]);
        }
    }

    public void createAndUpdateTime(String time, Context context) {
        setTime(time);
        new TimePickerDialog(context, this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
    }

    private void setTime(String time) {
        Log.d("set Time", time);
        if (time != null && !time.isEmpty()) {
            SimpleDateFormat formatter = new SimpleDateFormat("hh mm a");
            try {
                calendar.setTime(formatter.parse(time));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

}