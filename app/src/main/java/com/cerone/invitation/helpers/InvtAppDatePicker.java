/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.helpers;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.utills.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;



/**
 * @author CeroneFedora
 * @version 1.0, Aug 7, 2015
 */
public class InvtAppDatePicker implements DatePickerDialog.OnDateSetListener {

    EditText editText;
    String DATE_FORMAT = "yyyy-MM-dd ";
    Calendar calendar;
    EditText refEditText;
    boolean isStartDate;

    public InvtAppDatePicker(EditText editText, Calendar calendar) {
        this.editText = editText;
        this.calendar = calendar;
    }

    public InvtAppDatePicker(EditText editText, Calendar calendar, EditText refEditText, boolean isStartDate) {
        this.isStartDate = isStartDate;
        this.refEditText = refEditText;
        this.editText = editText;
        this.calendar = calendar;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String myFormat = DATE_FORMAT;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        if (editText != null && calendar != null) {
            String selectdData = simpleDateFormat.format(calendar.getTime());
            if (refEditText == null)
                editText.setText(selectdData);
            else {
                String refDate = refEditText.getText().toString();
                if (isStartDate) {
                    if (StringUtils.isOldDate(selectdData)) {
                        editText.setText(selectdData);
                        if (!StringUtils.validateStartAndEndDates(selectdData, refDate)) {
                            refEditText.setText(selectdData);
                        }
                    } else {
                        ToastHelper.redToast(editText.getContext(), "Start date should not be past date.");
                    }
                } else {
                    if (StringUtils.validateStartAndEndDates(refDate, selectdData)) {
                        editText.setText(selectdData);
                    }
                }
            }
        }
    }

    public void createAndUpdateDate(View v, String date, Context context) {
        Calendar calendar = Calendar.getInstance();
        setDate(v, date, calendar);
        new DatePickerDialog(context, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void setDate(View v, String date, Calendar calendar) {
        try {
            if (date != null && !date.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                calendar.setTime(sdf.parse(date.substring(0, 10)));
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                ((EditText) v).setText(sdf1.format(calendar.getTime()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
