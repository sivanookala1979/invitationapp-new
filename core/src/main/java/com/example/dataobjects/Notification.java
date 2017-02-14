package com.example.dataobjects;

/**
 * Created by connectcorners on 12/5/16.
 */
public class Notification {


    int userId;
    int spaceId;
    String content;
    String categoryName;
    String spaceCreationDate;
    String fromAddress;
    String toAddress;
    String image;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(int spaceId) {
        this.spaceId = spaceId;
    }

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

    public String getSpaceCreationDate() {
        return spaceCreationDate;
    }

    public void setSpaceCreationDate(String spaceCreationDate) {
        this.spaceCreationDate = spaceCreationDate;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
