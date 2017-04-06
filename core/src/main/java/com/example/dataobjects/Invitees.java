package com.example.dataobjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peekay on 5/4/17.
 */

public class Invitees {

    String title;
    int totalInvitees;
    List<Invitee> inviteesList = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalInvitees() {
        return totalInvitees;
    }

    public void setTotalInvitees(int totalInvitees) {
        this.totalInvitees = totalInvitees;
    }

    public List<Invitee> getInviteesList() {
        return inviteesList;
    }

    public void setInviteesList(List<Invitee> inviteesList) {
        this.inviteesList = inviteesList;
    }

}
