package com.example.dataobjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peekay on 22/3/17.
 */

public class Notifications {

    int pendingNotifications;
    List<Notification> notifications = new ArrayList<Notification>();

    public int getPendingNotifications() {
        return pendingNotifications;
    }

    public void setPendingNotifications(int pendingNotifications) {
        this.pendingNotifications = pendingNotifications;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

}
