package com.ta.hyah.model;

public class DataStatusModel {
    private String temperature_status;
    private String blood_pressure_status;
    private String respiration_status;
    private String heart_status;
    private String status;
    private String indication;
    private String name;
    private String specialist;

    public DataStatusModel(String temperature_status, String blood_pressure_status, String respiration_status, String heart_status, String status, String indication, String name, String specialist) {
        this.temperature_status = temperature_status;
        this.blood_pressure_status = blood_pressure_status;
        this.respiration_status = respiration_status;
        this.heart_status = heart_status;
        this.status = status;
        this.indication = indication;
        this.name = name;
        this.specialist = specialist;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }
}
