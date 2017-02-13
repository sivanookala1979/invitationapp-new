package com.example.syncher;

import android.content.Context;

import com.example.dataobjects.CurrencyTypes;
import com.example.datasources.CacheDataSource;
import com.example.datasources.CurrencyTypeDataSource;
import com.example.utills.HTTPUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by peekay on 9/2/17.
 */

public class CurrencyTypesSyncher extends BaseSyncher {

    Context context;
    CurrencyTypeDataSource currencyTypeDataSource;
    CacheDataSource cacheDataSource;
    public static final String CURRENCY_TYPE_KEY = "currency_type_120";
    public static final int NUMBER_OF_DAYS = 5;

    public CurrencyTypesSyncher(Context context) {
        super();
        this.context = context;
        currencyTypeDataSource = new CurrencyTypeDataSource(context);
        cacheDataSource = new CacheDataSource(context);
    }

    public List<CurrencyTypes> getAllCurrencyTypes() {
        List<CurrencyTypes> result = new ArrayList<CurrencyTypes>();
        try {
            cacheDataSource.setUpdataSource();
            if (cacheDataSource.isNeedToLoadFromServer(CURRENCY_TYPE_KEY, NUMBER_OF_DAYS)) {
                String currencyTypes = HTTPUtils.getDataFromServer("http://invtapp.cerone-software.com/currencies.json", "GET", true);
                JSONObject responseJSON = new JSONObject(currencyTypes);
                JSONArray jsonArray = responseJSON.getJSONArray("currencies");
                for (int index = 0; index < jsonArray.length(); index++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(index);
                    CurrencyTypes currencies = new CurrencyTypes();
                    if(isNotNull(jsonObject, "title"))
                        currencies.setCurrencyCode(jsonObject.getString("title"));
                    if(isNotNull(jsonObject, "description"))
                        currencies.setDescription(jsonObject.getString("description"));
                    if(isNotNull(jsonObject, "active"))
                        currencies.setActive(jsonObject.getBoolean("active"));
                    if(isNotNull(jsonObject, "country_code"))
                        currencies.setCountryCode(jsonObject.getString("country_code"));
                    if(isNotNull(jsonObject, "currency_symbol"))
                        currencies.setCurrencySymbol(jsonObject.getString("currency_symbol"));
                    if (isNotNull(jsonObject, "mobile_number_count"))
                        currencies.setMobileNumberCount(jsonObject.getInt("mobile_number_count") + "");
                    if(isNotNull(jsonObject, "sys_country_code"))
                        currencies.setSysCountryCode(jsonObject.getString("sys_country_code"));
                    if(isNotNull(jsonObject, "country_name"))
                        currencies.setCountryName(jsonObject.getString("country_name"));
                    result.add(currencies);
                }
                storeCurrencyTypes(result);
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 0);
                cacheDataSource.insert(CURRENCY_TYPE_KEY, cal.getTime().toString());
            } else {
                currencyTypeDataSource.setUpdataSource();
                result = currencyTypeDataSource.getAllCurrencyTypes();
                currencyTypeDataSource.close();
            }

        } catch (Exception e) {
            handleException(e);
        }
        return result;
    }
    private void storeCurrencyTypes(List<CurrencyTypes> result) {
        // TODO Auto-generated method stub
        try {
            currencyTypeDataSource.setUpdataSource();
            currencyTypeDataSource.clearAll();
            for (int i = 0; i < result.size(); i++) {
                currencyTypeDataSource.insert(result.get(i).getCurrencyCode(), result.get(i).getDescription(), result.get(i).getCurrencySymbol(), result.get(i).getCountryCode(), result.get(i).getMobileNumberCount(), result.get(i).getSysCountryCode(), result.get(i).getCountryName());
            }
        } catch (Exception e) {
            handleException(e);
        } finally {
            currencyTypeDataSource.close();
        }
    }
}
