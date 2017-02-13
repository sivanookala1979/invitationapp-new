/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.example.datasources;


import android.content.Context;

import com.example.stubs.AndroidSQLiteDatabase;
import com.example.stubs.AndroidStubsFactory;
import com.example.stubs.CeroneSQLiteDatabase;
import com.example.stubs.IContentValues;
import com.example.stubs.ICursor;
import com.example.stubs.IDatabaseHelper;
import com.example.stubs.ISQLiteDatabase;


/**
 * @author cerone
 * @version 1.0, Apr 14, 2015
 */
public abstract class BaseDataSource {

	public ISQLiteDatabase database;
	private final IDatabaseHelper dbHelper;

	public BaseDataSource(Context context) {
		dbHelper = AndroidStubsFactory.createDatabaseHelper(context);
	}

	public long insertEntry(IContentValues newValues) {
		String tableName = getTableName();
		if (tableName != null) {
			return database.insert(tableName, null, newValues);
		}
		return -1;
	}

	public int updateEntry(IContentValues newValues, String key, String value) {
		String tableName = getTableName();
		if (tableName != null) {
			return database.update(tableName, newValues, key + "=?",
					new String[] { value });
		}
		return -1;
	}

	public ICursor getAllEntries() {
		String tableName = getTableName();
		String[] allColumns = getAllColumns();
		if (allColumns != null && tableName != null) {
			return database.query(tableName, allColumns, null, null, null,
					null, null);
		}
		return null;
	}

	public void clearAll() {
		String tableName = getTableName();
		if (tableName != null) {
			database.delete(tableName, null, null);
		}
	}

	public void open() {
		if (AndroidStubsFactory.IS_TEST) {
			database = new CeroneSQLiteDatabase();
		} else {
			database = new AndroidSQLiteDatabase(
					dbHelper.getWritableDatabase2());
		}
	}

	public void close() {
		dbHelper.close();
	}

	public abstract String[] getAllColumns();

	public abstract String getTableName();

	public abstract String getUniqueId();

	public abstract BaseDataSource createNew(Context context);

	public final void createIfNotPresent() {
		database.create(getTableName(), getAllColumns());
	}

	public void setUpdataSource() {
		open();
		createIfNotPresent();
	}
}
