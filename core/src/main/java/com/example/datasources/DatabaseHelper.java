/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.example.datasources;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.stubs.IDatabaseHelper;

/**
 * @author cerone
 * @version 1.0, Apr 14, 2015
 */
public class DatabaseHelper extends SQLiteOpenHelper implements IDatabaseHelper {

    private static final int VERSION = 5;

    public DatabaseHelper(Context context) {
        super(context, "dihours.db", null, VERSION);
    }

    private static final String CACHE_DATA_SOURCE = createTable(CacheDataSource.CACHE_DATA_TABLE, CacheDataSource.ALL_COLUMNS);
    private static final String DATABASE_CURRENCY_TYPES = createTable(CurrencyTypeDataSource.CURRENCY_TYPE_TABLE,CurrencyTypeDataSource.ALL_COLUMNS);

    public static final String[] ALL_TABLES = {
            CacheDataSource.CACHE_DATA_TABLE,
            CurrencyTypeDataSource.CURRENCY_TYPE_TABLE
    };
    
    public static final BaseDataSource[] ALL_DATASOURCES = {

            new CacheDataSource(null),
            new CurrencyTypeDataSource(null),
    };

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CACHE_DATA_SOURCE);
        database.execSQL(DATABASE_CURRENCY_TYPES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + CacheDataSource.CACHE_DATA_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + CurrencyTypeDataSource.CURRENCY_TYPE_TABLE);
        onCreate(database);
    }

    public static String createTable(String tableName, String... columnNames) {
        String result = "create table if not exists " + tableName + "(";
        for (int index = 0; index < columnNames.length; index++) {
            String columnName = columnNames[index];
            result += columnName + (index != columnNames.length - 1 ? " TEXT, " : " TEXT");
        }
        result += ");";
        return result;
    }

    public static String createCustomTable(String tableName, String query) {
        String result = "create table if not exists " + tableName + "(";
        result += query;
        result += ");";
        return result;
    }

    @Override
    public SQLiteDatabase getWritableDatabase2() {
        return super.getWritableDatabase();
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    public void clearDB() {
        SQLiteDatabase database = getWritableDatabase();
        for (String tableName : ALL_TABLES) {
            database.execSQL("DELETE FROM " + tableName);
        }
        database.close();
    }

    public static BaseDataSource getRelevantDataSource(String tableName, Context context) {
        for (int i = 0; i < ALL_TABLES.length; i++) {
            if (ALL_TABLES[i].equals(tableName)) {
                return ALL_DATASOURCES[i];
            }
        }
        return null;
    }
}
