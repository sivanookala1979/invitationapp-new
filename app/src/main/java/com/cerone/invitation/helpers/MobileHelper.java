/*
 * (c) Copyright 2001-2016 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.MediaStore.MediaColumns;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Random;

import com.example.dataobjects.*;


/**
 * @author Adarsh.T
 * @version 1.0, Feb 23, 2016
 */
public class MobileHelper {

    public static boolean isOnline(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            //ToastHelper.redToast(context, "Please check your Internet connection!");
            return false;
        }
        return true;
    }

    public static ArrayList<User> getAllPhoneContacts(Context context) {
        Log.d("Contacts", "Try to Getting all Contacts");
        ArrayList<User> allContacts = new ArrayList<User>();
        ArrayList<String> allMobileNumbers = new ArrayList<String>();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(uri, new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                BaseColumns._ID}, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            int phoneContactID = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
            User phoneContactInfo = new User();
            phoneContactInfo.setUserName(contactName.trim());
            phoneContactInfo.setPhoneNumber(contactNumber.replaceAll("[^0-9]+", "").trim());
            Log.d("Contacts", "Name " + contactName + " Contact number " + contactNumber);
            if (phoneContactInfo.getPhoneNumber().length() >= 10 && !phoneContactInfo.getUserName().isEmpty()) {
                if (!allMobileNumbers.contains(phoneContactInfo.getPhoneNumber())) {
                    allMobileNumbers.add(phoneContactInfo.getPhoneNumber());
                    allContacts.add(phoneContactInfo);
                }
            }
            cursor.moveToNext();
        }
        cursor.close();
        cursor = null;
        Log.d("END", "Got all Contacts" + allContacts.size());
        return allContacts;
    }

    public static User getUserInforMationByIntent(Intent data, Activity context) {
        User user = new User();
        Uri contactData = data.getData();
        Cursor c = context.managedQuery(contactData, null, null, null, null);
        if (c.moveToFirst()) {
            String id = c.getString(c.getColumnIndexOrThrow(BaseColumns._ID));
            String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            if (hasPhone.equalsIgnoreCase("1")) {
                Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                phones.moveToFirst();
                String mobileNumber = phones.getString(phones.getColumnIndex("data1"));
                mobileNumber = mobileNumber.replaceAll("[^0-9]+", "");
                user.setPhoneNumber(mobileNumber);
            }
        }
        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        user.setUserName(name);
        if (user.getPhoneNumber() == null) {
            user = null;
        } else
            ToastHelper.redToast(context.getApplicationContext(), "Invalid contact");
        return user;
    }

    public static int getContactIDFromNumber(String contactNumber, Context context) {
        contactNumber = Uri.encode(contactNumber);
        int phoneContactID = new Random().nextInt();
        Cursor contactLookupCursor = context.getContentResolver().query(Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(contactNumber)), new String[]{
                PhoneLookup.DISPLAY_NAME,
                BaseColumns._ID}, null, null, null);
        while (contactLookupCursor.moveToNext()) {
            phoneContactID = contactLookupCursor.getInt(contactLookupCursor.getColumnIndexOrThrow(BaseColumns._ID));
        }
        contactLookupCursor.close();
        return phoneContactID;
    }

    public static Bitmap getBitmapFromCameraData(Intent data, Context context) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaColumns.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        Options option = new Options();
        option.inJustDecodeBounds = true;
        int sample = 16;
        BitmapFactory.decodeFile(picturePath, option);
        if (option.outHeight > 150 || option.outWidth > 150) {
            if (option.outHeight > option.outWidth) {
                sample = option.outHeight / 150;
            } else {
                sample = option.outWidth / 150;
            }
        }
        option.inSampleSize = sample;
        option.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(picturePath, option);
    }

    public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        System.out.println(" JPEG Bitmap size : " + temp.length() + " JPEG Byte array length :" + b.length);
        return temp;
    }

    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivity.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean hasNetwork(final Context context, final Context activityContext, String action, final Class onCancelActivityClass) {
        if (!isNetworkAvailable(context)) {
            AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(activityContext);
            myAlertDialog.setTitle("Your mobile internet status");
            myAlertDialog.setMessage("You need an Internet connection to " + action + ". Do you want to turn on it?");
            myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    if (onCancelActivityClass != null) {
                        activityContext.startActivity(new Intent(context, onCancelActivityClass));
                    }
                }
            });
            myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    activityContext.startActivity(intent);
                }
            });
            myAlertDialog.show();
            return isNetworkAvailable(context);
        }
        return true;
    }
}
