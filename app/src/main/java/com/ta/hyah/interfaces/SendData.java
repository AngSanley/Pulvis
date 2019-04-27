package com.ta.hyah.interfaces;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendData {

    @SerializedName("to")
    @Expose
    private String to = "/topics/";
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("notification")
    @Expose
    private Notification notification;

    public SendData(String to, Data data, Notification notification) {
        this.to += to;
        this.data = data;
        this.notification = notification;
    }

    public SendData(Data data, Notification notification) {
        this.data = data;
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
