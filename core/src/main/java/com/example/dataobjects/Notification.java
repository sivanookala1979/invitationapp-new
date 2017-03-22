package com.example.dataobjects;

/**
 * Created by connectcorners on 12/5/16.
 */
public class Notification {
    int userId;
    int id;
    int orderId;
    int  spaceId;
    String content;
    String categoryName;
    String image;
    String spaceCreationDate;
    boolean isnotified;


    public int getOrderId() { return orderId; }

    public void setOrderId(int orderId) { this.orderId = orderId; }

    public boolean isnotified() { return isnotified; }

    public void setIsnotified(boolean isnotified) { this.isnotified = isnotified; }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public int getSpaceId() { return spaceId; }

    public void setSpaceId(int spaceId) { this.spaceId = spaceId; }

    public String getSpaceCreationDate() { return spaceCreationDate; }

    public void setSpaceCreationDate(String spaceCreationDate) { this.spaceCreationDate = spaceCreationDate; }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
