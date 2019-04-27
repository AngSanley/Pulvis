package com.ta.hyah.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PasienModel {

    @SerializedName("loginstatus")
    @Expose
    String loginStatus;

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("name")
    @Expose
    String name;

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
