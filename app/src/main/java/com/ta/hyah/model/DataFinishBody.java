package com.ta.hyah.model;

import com.google.gson.annotations.SerializedName;

public class DataFinishBody {

    String savestatus;
    String time;
    String date;
    String temperature_status;
    String blood_pressure_status;
    String respiration_status;
    String heart_status;
    String status;
    String indication;

    public DataFinishBody(String savestatus, String time, String date, String temperature_status, String blood_pressure_status, String respiration_status, String heart_status, String status, String indication) {
        this.savestatus = savestatus;
        this.time = time;
        this.date = date;
        this.temperature_status = temperature_status;
        this.blood_pressure_status = blood_pressure_status;
        this.respiration_status = respiration_status;
        this.heart_status = heart_status;
        this.status = status;
        this.indication = indication;
    }

    public String getSavestatus() {
        return savestatus;
    }

    public void setSavestatus(String savestatus) {
        this.savestatus = savestatus;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemperature_status() {
        return temperature_status;
    }

    public void setTemperature_status(String temperature_status) {
        this.temperature_status = temperature_status;
    }

    public String getBlood_pressure_status() {
        return blood_pressure_status;
    }

    public void setBlood_pressure_status(String blood_pressure_status) {
        this.blood_pressure_status = blood_pressure_status;
    }

    public String getRespiration_status() {
        return respiration_status;
    }

    public void setRespiration_status(String respiration_status) {
        this.respiration_status = respiration_status;
    }

    public String getHeart_status() {
        return heart_status;
    }

    public void setHeart_status(String heart_status) {
        this.heart_status = heart_status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIndication() {
        return indication;
    }

    public void setIndication(String indication) {
        this.indication = indication;
    }
}
