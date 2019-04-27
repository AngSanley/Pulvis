package com.ta.hyah.interfaces;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ta.hyah.constants.Constants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationPost {
    @Headers({
            "Content-Type: application/json",
            "Authorization: "+ Constants.AUTHKEY
    })
    @POST("send")
    Call<DataRes> SEND_DATA_CALL(@Body SendData sendData);
}
