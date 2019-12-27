package com.m.openthedoorapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserNotificationResponse {

    @SerializedName("Notfication")
    private List<NotficationItem> notfication;
    @SerializedName("status")
    private boolean status;

    public List<NotficationItem> getNotfication() {
        return notfication;
    }

    public void setNotfication(List<NotficationItem> notfication) {
        this.notfication = notfication;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return
                "UserNotificationResponse{" +
                        "notfication = '" + notfication + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}