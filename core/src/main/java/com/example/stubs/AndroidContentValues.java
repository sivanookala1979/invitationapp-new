package com.example.stubs;

import android.content.ContentValues;

/**
 * Created by connectcorners on 7/7/16.
 */
public class AndroidContentValues implements IContentValues{


    ContentValues contentValues = null;

    public AndroidContentValues() {
        super();
        contentValues = new ContentValues();
    }

    public ContentValues getContentValues() {
        return contentValues;
    }

    public void setContentValues(ContentValues contentValues) {
        this.contentValues = contentValues;
    }

    @Override
    public void put(String key, String values) {
        contentValues.put(key, values);
        setContentValues(contentValues);
    }
}

