package com.ta.hyah.constants;

import com.ta.hyah.BuildConfig;
import com.ta.hyah.interfaces.API;
import com.ta.hyah.retrofit.RetrofitClient;

public class Constants {
    public static final String INTENT_ACTION_DISCONNECT = BuildConfig.APPLICATION_ID + ".Disconnect";
    public static final String NOTIFICATION_CHANNEL = BuildConfig.APPLICATION_ID + ".Channel";
    public static final String INTENT_CLASS_MAIN = BuildConfig.APPLICATION_ID + ".SettingsActivity";
    public static final String AUTHKEY = "key=AAAAVEYCQzE:APA91bGv6dp0DoWqEQeAtSDK22TfPw3W6A9RH_SJU_pswI3hOPfyhu7U3RKkrQTLKTQdZa-Lavf7jSwNGKbimEALidK1mUhW2DSXcZjhEJryFgt8drqFaxN_nu6Zkewk1dSKBPbbLB1p";
    public static final String URL = "https://fcm.googleapis.com/fcm/";
    public static final String URLLOGIN = "http://kentang.binusian.id:3000/";

    public static API getAPIService(){
        return RetrofitClient.getRetrofit(URLLOGIN).create(API.class);
    }

    public static final int NOTIFY_MANAGER_START_FOREGROUND_SERVICE = 1001;

    public Constants(){}
}
