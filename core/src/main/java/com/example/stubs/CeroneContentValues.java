/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.example.stubs;

import java.util.HashMap;

/**
 * @author suzuki
 * @version 1.0, 16-Jul-2015
 */
public class CeroneContentValues implements IContentValues {

    HashMap<String, Object> values = new HashMap<String, Object>();

    public Object get(String key) {
        return values.get(key);
    }

    @Override
    public void put(String key, String value) {
        values.put(key, value);
    }
}
