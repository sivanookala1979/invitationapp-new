/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.example.stubs;

import android.content.Context;

import com.example.datasources.DatabaseHelper;

/**
 * @author suzuki
 * @version 1.0, 16-Jul-2015
 */
public class AndroidStubsFactory {

    public static boolean IS_TEST = false;

    public static IDatabaseHelper createDatabaseHelper(Context context) {
        IDatabaseHelper result = null;
        if (IS_TEST) {
            result = new CeroneDatabaseHelper();
        } else {
            result = (IDatabaseHelper) new DatabaseHelper(context);
        }
        return result;
    }

    public static IContentValues createContentValues() {
        IContentValues result = null;
        if (IS_TEST) {
            result = new CeroneContentValues();
        } else {
            result = new AndroidContentValues();
        }
        return result;
    }
}
