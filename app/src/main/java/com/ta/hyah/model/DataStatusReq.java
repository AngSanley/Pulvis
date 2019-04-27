package com.ta.hyah.model;

import com.google.gson.annotations.SerializedName;

public class DataStatusReq {

    @SerializedName("id")
    String id;

    @SerializedName("temperature")
    String temperature;

    @SerializedName("sistole")
    String sistole;

    @SerializedName("diastole")
    String diastole;

    @SerializedName("heart_rate")
    String heart_rate;

    @SerializedName("respiration_rate")
    String respiration_rate;

    public DataStatusReq(String id, String temperature, String sistole, String diastole, String heart_rate, String respiration_rate) {
        this.id = id;
        this.temperature = temperature;
        this.sistole = sistole;
        this.diastole = diastole;
        this.heart_rate = heart_rate;
        this.respiration_rate = respiration_rate;
    }
}
