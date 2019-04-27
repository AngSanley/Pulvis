package com.ta.hyah.model;

import com.google.gson.annotations.SerializedName;

public class DataFinishRes {
    @SerializedName("id")
    String id;

    @SerializedName("temperature")
    String temperature;

    @SerializedName("sistole")
    String sistole;

    @SerializedName("diastole")
    String diastole;

    @SerializedName("respiration_rate")
    String respiration_rate;

    @SerializedName("heart_rate")
    String heart_rate;

    public DataFinishRes(String id, String temperature, String sistole, String diastole, String respiration_rate, String heart_rate) {
        this.id = id;
        this.temperature = temperature;
        this.sistole = sistole;
        this.diastole = diastole;
        this.respiration_rate = respiration_rate;
        this.heart_rate = heart_rate;
    }
}
