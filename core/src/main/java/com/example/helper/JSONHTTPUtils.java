/*
 * (c) Copyright 2001-2016 COMIT AG
 * All Rights Reserved.
 */
package com.example.helper;



import com.example.dataobjects.SaveResult;
import com.example.syncher.BaseSyncher;
import com.example.utills.HTTPUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.syncher.BaseSyncher.BASE_URL;

public class JSONHTTPUtils<T> extends BaseSyncher {

    public List<T> list(String url, FromJSONConvertor<T> jsonConvertor) {
        List<T> result = new ArrayList<T>();
        try {
            String response = HTTPUtils.getDataFromServer(BASE_URL + url, "GET");
            JSONArray messagesArray = new JSONArray(response);
            for (int i = 0; i < messagesArray.length(); i++) {
                JSONObject jsonObject = messagesArray.getJSONObject(i);
                result.add(jsonConvertor.fromJSON(jsonObject));
            }
            System.out.println(response);
        } catch (Exception ex) {
            handleException(ex);
        }
        return result;
    }

    public T dataObject(String url, FromJSONConvertor<T> jsonConvertor) {
        T result = null;
        try {
            String response = HTTPUtils.getDataFromServer(BaseSyncher.BASE_URL + url, "GET");
            JSONObject jsonObject = new JSONObject(response);
            result = jsonConvertor.fromJSON(jsonObject);
        } catch (Exception ex) {
            handleException(ex);
        }
        return result;
    }

    public SaveResult simpleGet(String urlData) {
        SaveResult result = new SaveResult();
        try {
            HTTPUtils.getDataFromServer(BaseSyncher.BASE_URL + urlData, "GET");
            result.setSuccess(true);
        } catch (Exception ex) {
            handleException(ex);
        }
        return result;
    }
}
