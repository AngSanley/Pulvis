package com.ta.hyah.model;

public class DataSistolDiastol {
    String sistol;
    String diastol;

    public DataSistolDiastol(String sistol, String diastol) {
        this.sistol = sistol;
        this.diastol = diastol;
    }

    public String getSistol() {
        return sistol;
    }

    public void setSistol(String sistol) {
        this.sistol = sistol;
    }

    public String getDiastol() {
        return diastol;
    }

    public void setDiastol(String diastol) {
        this.diastol = diastol;
    }
}
