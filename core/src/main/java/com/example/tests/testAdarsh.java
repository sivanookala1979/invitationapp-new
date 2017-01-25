/*
 * (c) Copyright 2001-2016 COMIT AG
 * All Rights Reserved.
 */
package com.example.tests;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author peekay
 * @version 1.0, 02-Feb-2016
 */
public class testAdarsh {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String data = "0.30600351024391836";
        double doubleInfo = Double.parseDouble(data);
        DecimalFormat df = new DecimalFormat("#.00");
        System.out.println(df.format(doubleInfo));
        System.out.println("" + getTrimmeDistance(data));
        formatData("(996) 318-4079");
        formatDate("2016-02-06 10:10:00", 1);
    }

    private static void formatDate(String string, int i) {
        String info = "Not found";
        Date dateTime = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            dateTime = simpleDateFormat.parse(string);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a");
            SimpleDateFormat eventDateFormat = new SimpleDateFormat("E, yyyy MMM dd - HH:mm a");
            System.out.println("MOnth " + dateFormat.format(dateTime));
            System.out.println("MOnth " + timeFormat.format(dateTime));
            System.out.println("Event " + eventDateFormat.format(dateTime));
        } catch (ParseException ex) {
            System.out.println("Exception " + ex);
        }
    }

    private static void formatData(String string) {
        string = string.replaceAll("[^0-9]+", "").trim();
        System.out.println(string);
    }

    public static String getDistance(String data) {
        String distance = "";
        DecimalFormat df = new DecimalFormat("#0.00");
        if (data != null && !data.equals("Not found")) {
            double doubleInfo = Double.parseDouble(data);
            if (doubleInfo > 1) {
                distance = df.format(doubleInfo) + " km";
            }
        }
        return distance;
    }

    public static String getTrimmeDistance(String distance) {
        String result = "Not found";
        DecimalFormat df = new DecimalFormat("#.0");
        df.setMinimumIntegerDigits(1);
        if (distance != null && !distance.equals("Not found")) {
            double doubleInfo = Double.parseDouble(distance);
            result = df.format(doubleInfo) + " km";
        }
        return result;
    }
}
