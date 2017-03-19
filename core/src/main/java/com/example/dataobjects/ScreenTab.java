package com.example.dataobjects;

/**
 * Created by adarsh on 3/19/17.
 */

public class ScreenTab {
    int index;
    String name;
    int imageResource;

    public ScreenTab(int index, String name, int imageResource) {
        this.index = index;
        this.name = name;
        this.imageResource = imageResource;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }


}
