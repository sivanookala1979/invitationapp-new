package com.example.dataobjects;

/**
 * Created by adarsht on 02/02/17.
 */

public class Eventstatistics {

    int acceptCount;
    int rejectCount;
    int checkedInCount;
    int totalInviteesCount;
    String  message;
    boolean valid;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getAcceptCount() {
        return acceptCount;
    }

    public void setAcceptCount(int acceptCount) {
        this.acceptCount = acceptCount;
    }

    public int getRejectCount() {
        return rejectCount;
    }

    public void setRejectCount(int rejectCount) {
        this.rejectCount = rejectCount;
    }

    public int getCheckedInCount() {
        return checkedInCount;
    }

    public void setCheckedInCount(int checkedInCount) {
        this.checkedInCount = checkedInCount;
    }

    public int getTotalInviteesCount() {
        return totalInviteesCount;
    }

    public void setTotalInviteesCount(int totalInviteesCount) {
        this.totalInviteesCount = totalInviteesCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
