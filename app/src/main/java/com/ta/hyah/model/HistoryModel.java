package com.ta.hyah.model;

public class HistoryModel {
    private String name;
    private String specialist;
    private String doctor_phone;
    private String emergency_contact;
    private String date;
    private String time;
    private String temperature;
    private String sistole;
    private String diastole;
    private String respiration_rate;
    private String heart_rate;

    public HistoryModel(String name, String specialist, String doctor_phone, String emergency_contact, String date, String time, String temperature, String sistole, String diastole, String respiration_rate, String heart_rate) {
        this.name = name;
        this.specialist = specialist;
        this.doctor_phone = doctor_phone;
        this.emergency_contact = emergency_contact;
        this.date = date;
        this.time = time;
        this.temperature = temperature;
        this.sistole = sistole;
        this.diastole = diastole;
        this.respiration_rate = respiration_rate;
        this.heart_rate = heart_rate;
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

    public String getDoctor_phone() {
        return doctor_phone;
    }

    public void setDoctor_phone(String doctor_phone) {
        this.doctor_phone = doctor_phone;
    }

    public String getEmergency_contact() {
        return emergency_contact;
    }

    public void setEmergency_contact(String emergency_contact) {
        this.emergency_contact = emergency_contact;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
