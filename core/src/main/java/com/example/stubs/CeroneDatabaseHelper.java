/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.example.stubs;

import android.database.sqlite.SQLiteDatabase;

/**
 * @author suzuki
 * @version 1.0, 16-Jul-2015
 */
public class CeroneDatabaseHelper implements IDatabaseHelper {

    @Override
    public void close() {
        // TODO Auto-generated method stub
    }

    @Override
    public SQLiteDatabase getWritableDatabase2() {
        throw new RuntimeException();
    }
}
