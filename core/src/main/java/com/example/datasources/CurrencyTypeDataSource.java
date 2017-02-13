package com.example.datasources;


import android.content.Context;

import com.example.dataobjects.CurrencyTypes;
import com.example.stubs.AndroidStubsFactory;
import com.example.stubs.IContentValues;
import com.example.stubs.ICursor;

import java.util.ArrayList;
import java.util.List;

public class CurrencyTypeDataSource extends BaseDataSource {

    public static final String CURRENCY_TYPE_TABLE = "currencies_table";
    public static final String CURRENCY_TYPE_ID = "id";
    public static final String CURRENCY_TYPE = "title";
    public static final String DESCRIPTION = "description";
    public static final String CURRENCY_SYMBOL = "currency_symbol";
    public static final String COUNTRY_CODE = "country_code";
    public static final String MOBILE_NUMBER_COUNT = "mobile_number_count";
    public static final String SYS_COUNTRY_CODE = "sys_country_code";
    public static final String COUNTRY_NAME = "country_name";
    public static final String[] ALL_COLUMNS = new String[]{
            CURRENCY_TYPE_ID,
            CURRENCY_TYPE,
            DESCRIPTION,
            CURRENCY_SYMBOL,
            COUNTRY_CODE,
            MOBILE_NUMBER_COUNT,
            SYS_COUNTRY_CODE,
            COUNTRY_NAME};

    public CurrencyTypeDataSource(Context context) {
        super(context);
    }

    @Override
    public String[] getAllColumns() {
        // TODO Auto-generated method stub
        return ALL_COLUMNS;
    }

    @Override
    public String getTableName() {
        // TODO Auto-generated method stub
        return CURRENCY_TYPE_TABLE;
    }

    @Override
    public String getUniqueId() {
        // TODO Auto-generated method stub
        return CURRENCY_TYPE_ID;
    }

    public void insert(String currencyType, String description, String currencySymbol, String countryCode, String mobileNumberCount, String sysCountryCode, String countryName) {
        IContentValues contentValues = AndroidStubsFactory.createContentValues();
        contentValues.put(CURRENCY_TYPE, currencyType);
        contentValues.put(DESCRIPTION, description);
        contentValues.put(CURRENCY_SYMBOL, currencySymbol);
        contentValues.put(COUNTRY_CODE, countryCode);
        contentValues.put(MOBILE_NUMBER_COUNT, mobileNumberCount);
        contentValues.put(SYS_COUNTRY_CODE, sysCountryCode);
        contentValues.put(COUNTRY_NAME, countryName);
        insertEntry(contentValues);
    }

    public List<CurrencyTypes> getAllCurrencyTypes() {
        List<CurrencyTypes> result = new ArrayList<CurrencyTypes>();
        ICursor query = getAllEntries();
        for (int i = 0; i < query.getCount(); i++) {
            CurrencyTypes currencyType = new CurrencyTypes();
            currencyType.setCurrencyCode((String) query.getItemAt(i, this).get(CURRENCY_TYPE));
            currencyType.setDescription((String) query.getItemAt(i, this).get(DESCRIPTION));
            currencyType.setCountryCode((String) query.getItemAt(i, this).get(COUNTRY_CODE));
            currencyType.setCurrencySymbol((String) query.getItemAt(i, this).get(CURRENCY_SYMBOL));
            currencyType.setMobileNumberCount((String) query.getItemAt(i, this).get(MOBILE_NUMBER_COUNT));
            currencyType.setSysCountryCode((String) query.getItemAt(i, this).get(SYS_COUNTRY_CODE));
            currencyType.setCountryName((String) query.getItemAt(i, this).get(COUNTRY_NAME));
            result.add(currencyType);
        }
        return result;
    }

    public CurrencyTypes getCurrency(String countryCode) {
        CurrencyTypes currencyType = new CurrencyTypes();
        ICursor query = database.query(CURRENCY_TYPE_TABLE, ALL_COLUMNS, COUNTRY_CODE + " = '" + countryCode + "'", null, null, null, null);
        if (query.getCount() > 0) {
            currencyType.setCurrencyCode((String) query.getItemAt(0, this).get(CURRENCY_TYPE));
            currencyType.setDescription((String) query.getItemAt(0, this).get(DESCRIPTION));
            currencyType.setCountryCode((String) query.getItemAt(0, this).get(COUNTRY_CODE));
            currencyType.setCurrencySymbol((String) query.getItemAt(0, this).get(CURRENCY_SYMBOL));
            currencyType.setMobileNumberCount((String) query.getItemAt(0, this).get(MOBILE_NUMBER_COUNT));
            currencyType.setSysCountryCode((String) query.getItemAt(0, this).get(SYS_COUNTRY_CODE));
            currencyType.setCountryName((String) query.getItemAt(0, this).get(COUNTRY_NAME));
        }
        return currencyType;
    }

    @Override
    public BaseDataSource createNew(Context context) {
        // TODO Auto-generated method stub
        return new CurrencyTypeDataSource(context);
    }
}
