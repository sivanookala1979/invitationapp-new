package com.cerone.invitation.helpers;

import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import java.util.Calendar;

public class InvtAppTimePicker implements TimePickerDialog.OnTimeSetListener {

    EditText editText;
    Calendar calendar;

    //    EditText refeditText;
    //    boolean isStartTime;
    public InvtAppTimePicker(EditText editText, Calendar calendar) {
        this.editText = editText;
        this.calendar = calendar;
    }

    //    public InvtAppTimePicker(EditText editText, Calendar calendar, EditText refeditText, boolean isStartTime) {
    //        this.editText = editText;
    //        this.calendar = calendar;
    //        this.refeditText = refeditText;
    //        this.isStartTime = isStartTime;
    //    }
    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        if (editText != null && calendar != null) {
            editText.setText(getValidText(hourOfDay) + ":" + getValidText(minute));
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
            editText.setText(getValidText(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + getValidText(calendar.get(Calendar.MINUTE)));
        }
    }
}
