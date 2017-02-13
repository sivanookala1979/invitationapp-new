package com.example.dataobjects;

public class User {

    String userName;
    String phoneNumber;
    boolean selected;
    boolean admin;
    boolean phoneContact;
    String status;
    boolean profileGiven;
    boolean appLogin;
    String image;
    String errorMessage;
    String gender;

    public boolean isAppLogin() {
        return appLogin;
    }

    public void setAppLogin(boolean appLogin) {
        this.appLogin = appLogin;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isProfileGiven() {
        return profileGiven;
    }

    public void setProfileGiven(boolean profileGiven) {
        this.profileGiven = profileGiven;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isPhoneContact() {
        return phoneContact;
    }

    public void setPhoneContact(boolean phoneContact) {
        this.phoneContact = phoneContact;
    }
}