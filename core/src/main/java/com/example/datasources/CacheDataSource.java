/*
 * (c) Copyright 2001-2016 COMIT AG
 * All Rights Reserved.
 */
package com.example.datasources;


import android.content.Context;

import com.example.stubs.AndroidStubsFactory;
import com.example.stubs.IContentValues;
import com.example.stubs.ICursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author darling
 * @version 1.0, 23-Feb-2016
 */
public class CacheDataSource extends BaseDataSource {

    public static final String CACHE_DATA_TABLE = "Cache_Data_Table";
    public static final String CACHE_DATA_ID = "id";
    public static final String KEY = "Key";
    public static final String LAST_UPDATED_AT = "Last_Updated_At";
    public static final String[] ALL_COLUMNS = new String[]{
            CACHE_DATA_ID,
            KEY,
            LAST_UPDATED_AT};

    public CacheDataSource(Context context) {
        super(context);
    }

    @Override
    public String[] getAllColumns() {
        return ALL_COLUMNS;
    }

    @Override
    public String getTableName() {
        return CACHE_DATA_TABLE;
    }

    @Override
    public String getUniqueId() {
        return CACHE_DATA_ID;
    }

    @Override
    public BaseDataSource createNew(android.content.Context context) {
        return null;
    }

    public void insert(String key, String date) {
        IContentValues contentValues = AndroidStubsFactory.createContentValues();
        contentValues.put(KEY, key);
        contentValues.put(LAST_UPDATED_AT, date);
        insertEntry(contentValues);
    }

    public boolean isNeedToLoadFromServer(String key, int numberOfDays) throws ParseException {
        boolean result = true;
        ICursor query = database.query(CACHE_DATA_TABLE, ALL_COLUMNS, KEY + " = '" + key + "'" + "order by " + LAST_UPDATED_AT + " DESC limit 1", null, null, null, null);
        if (query.getCount() > 0) {
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            //String dateInString = (String) query.getItemAt(0, this).get(LAST_UPDATED_AT);
            String dateInString = (String) query.getItemAt(0, this).get(LAST_UPDATED_AT);
            //String dateInString = formatter.format(query.getItemAt(0, this).get(LAST_UPDATED_AT));
            Date date1 = formatter.parse(dateInString);
            Date date2 = new Date();
            int diffInDays = (int) ((date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24));
            if (diffInDays <= numberOfDays) {
                return false;
            }
        }
        return result;
    }

}
