/*
 * (c) Copyright 2001-2016 COMIT AG
 * All Rights Reserved.
 */
package com.example.helper;

import org.json.JSONException;
import org.json.JSONObject;

public interface FromJSONConvertor<T> {

    T fromJSON(JSONObject jsonObject) throws JSONException;
}
