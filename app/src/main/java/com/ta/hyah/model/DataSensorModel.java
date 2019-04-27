package com.ta.hyah.model;

import com.google.gson.annotations.SerializedName;

public class DataSensorModel {
    @SerializedName("id")
    private String id;

    @SerializedName("temperatur")
    private String temperature;

    @SerializedName("sistole")
    private String sistole;

    @SerializedName("diastole")
    private String diastole;

    @SerializedName("respiration_rate")
    private String respiration_rate;

    @SerializedName("heart_rate")
    private String heart_rate;

    public DataSensorModel(String id, String temperature, String sistole, String diastole, String respiration_rate, String heart_rate) {
        this.id = id;
        this.temperature = temperature;
        this.sistole = sistole;
        this.diastole = diastole;
        this.respiration_rate = respiration_rate;
        this.heart_rate = heart_rate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getSistole() {
        return sistole;
    }

    public void setSistole(String sistole) {
        this.sistole = sistole;
    }

    public String getDiastole() {
        return diastole;
    }

    public void setDiastole(String diastole) {
        this.diastole = diastole;
    }

    public String getRespiration_rate() {
        return respiration_rate;
    }

    public void setRespiration_rate(String respiration_rate) {
        this.respiration_rate = respiration_rate;
    }

    public String getHeart_rate() {
        return heart_rate;
    }

    public void setHeart_rate(String heart_rate) {
        this.heart_rate = heart_rate;
    }
}
