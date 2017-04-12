/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.helpers;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.utills.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * @author CeroneFedora
 * @version 1.0, Aug 7, 2015
 */
public class InvtAppDatePicker implements DatePickerDialog.OnDateSetListener {

    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    TextView textView6;
    String referDate;
    Calendar calendar;
    boolean isStartDate;
    String DATE_FORMAT = "dd MM yyyy";

    public InvtAppDatePicker(TextView textView1, TextView textView2, TextView textView3,
                             TextView textView4, TextView textView5, TextView textView6, Calendar calendar, String referDate, boolean isStartDate) {
        this.isStartDate = isStartDate;
        this.textView1 = textView1;
        this.textView2 = textView2;
        this.textView3 = textView3;
        this.textView4 = textView4;
        this.textView5 = textView5;
        this.textView6 = textView6;
        this.referDate = referDate;
        this.calendar = calendar;

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String myFormat = DATE_FORMAT;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectdData = simpleDateFormat.format(calendar.getTime());
        Log.d("selected date", selectdData);
        Log.d("refer date", referDate);
        String[] date = selectdData.split(" ");
        if(isStartDate) {
            if (StringUtils.isOldDate(selectdData)) {
                textView1.setText(date[0]);
                textView2.setText(date[1]);
                textView3.setText(date[2]);

                if(!StringUtils.validateStartAndEndDates(selectdData, referDate)) {
                    textView4.setText(date[0]);
                    textView5.setText(date[1]);
                    textView6.setText(date[2]);
                }
            }
            else {
                ToastHelper.redToast(textView1.getContext(), "Start date should not be past date.");
            }
        }else{
            if (!StringUtils.validateStartAndEndDates(selectdData, referDate)) {
                textView4.setText(date[0]);
                textView5.setText(date[1]);
                textView6.setText(date[2]);
            } else {
                ToastHelper.redToast(textView1.getContext(), "end date should not be past to start date.");
            }
        }
    }

    public void createAndUpdateDate(View v1,View v2,View v3, String date, Context context) {
        Calendar calendar = Calendar.getInstance();
        setDate(v1, v2, v3, date, calendar);
        new DatePickerDialog(context, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void setDate(View v1,View v2,View v3, String date, Calendar calendar) {
        try {
            if (date != null && !date.isEmpty()) {
                Log.d("date", date);
                SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy", Locale.US);
                calendar.setTime(sdf.parse(date));
                Log.d("new date", sdf.format(new Date()));
//                String[] time = sdf.format(new Date()).split(" ");
//                ((TextView) v1).setText(time[0]);
//                ((TextView) v2).setText(time[1]);
//                ((TextView) v3).setText(time[2]);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}