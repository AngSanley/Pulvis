package com.ta.hyah.interfaces;

import com.ta.hyah.model.DataFinishRes;
import com.ta.hyah.model.DataFinishBody;
import com.ta.hyah.model.DataStatusModel;
import com.ta.hyah.model.DataStatusReq;
import com.ta.hyah.model.HistoryModel;
import com.ta.hyah.model.PasienModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface API {
    @FormUrlEncoded
    @POST("patientLogin")
    public Call<PasienModel> goLogin(@Field("id") String id);

    @FormUrlEncoded
    @POST("getMyRecord")
    public Call<List<HistoryModel>> dataHistory(@Field("id") String id);

    @POST("checkRecord")
    public Call<DataStatusModel> dataStatus(@Body DataStatusReq dataStatusReq);

    @POST("saveRecord")
    public Call<DataFinishBody> dataFinish(@Body DataFinishRes dataFinishRes);
}