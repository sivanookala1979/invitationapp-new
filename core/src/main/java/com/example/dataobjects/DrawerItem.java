/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.example.dataobjects;

/**
 * @author adarsh
 * @version 1.0, Aug 15, 2015
 */
public class DrawerItem {

    String ItemName;
    int imageResourceId;

    public DrawerItem(String itemName, int imgResID) {
        super();
        ItemName = itemName;
        imageResourceId = imgResID;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getImgResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
}
