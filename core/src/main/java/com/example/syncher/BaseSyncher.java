package com.example.syncher;


import com.example.helper.DefaultExceptionHandler;
import com.example.helper.ExceptionHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseSyncher {

    public static ExceptionHandler exceptionHandler = new DefaultExceptionHandler();
    //public static String BASE_URL = "http://192.168.10.11:4000/";
    public static String BASE_URL = "http://invtapp.cerone-software.com/";
    public static String accessToken;

    public void handleException(Exception exception) {
        exception.printStackTrace();
        exceptionHandler.handleException(exception);
    }

    public String getJsonData(JSONObject jsonObject, String key) throws JSONException {
        if (!jsonObject.isNull(key)) {
            return jsonObject.getString(key);
        }
        return null;
    }

    public static ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public static void setExceptionHandler(ExceptionHandler exceptionHandler) {
        BaseSyncher.exceptionHandler = exceptionHandler;
    }

    public static String getBASE_URL() {
        return BASE_URL;
    }

    public static void setBASE_URL(String bASE_URL) {
        BASE_URL = bASE_URL;
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        BaseSyncher.accessToken = accessToken;
    }

    public static void testSetup() {

    }
}