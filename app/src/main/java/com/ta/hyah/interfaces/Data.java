package com.ta.hyah.interfaces;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("extra_information")
    @Expose
    private String extraInformation;

    public Data(String extraInformation) {
        this.extraInformation = extraInformation;
    }

    public String getExtraInformation() {
        return extraInformation;
    }

    public void setExtraInformation(String extraInformation) {
        this.extraInformation = extraInformation;
    }
}
