package com.ta.hyah.constants;

import com.ta.hyah.model.DataBloodPresure;
import com.ta.hyah.model.DataBodyTemprature;
import com.ta.hyah.model.DataHeartRate;
import com.ta.hyah.model.DataRespirationRate;
import com.ta.hyah.model.DataSensorModel;

import java.util.ArrayList;

public class DataSensorConstans {
    public static String valueHeartRate = "";
    public static String valueBodyTemprature = "";
    public static String valueRespirationRate = "";
    public static String valueBloodPresure = "";

    public static ArrayList<DataSensorModel> modelArrayList = new ArrayList<>();
    public static ArrayList<DataBloodPresure> arrayListBloodPresures = new ArrayList<>();
    public static ArrayList<DataBodyTemprature> arrayListBodyTemprature = new ArrayList<>();
    public static ArrayList<DataHeartRate> arrayListHeartRate = new ArrayList<>();
    public static ArrayList<DataRespirationRate> arrayListRespirationRate = new ArrayList<>();
}
