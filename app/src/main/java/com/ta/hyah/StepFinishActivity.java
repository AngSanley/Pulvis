package com.ta.hyah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.ta.hyah.constants.Constants;
import com.ta.hyah.interfaces.API;
import com.ta.hyah.model.DataFinishBody;
import com.ta.hyah.model.DataFinishRes;
import com.ta.hyah.notificationsm.OurNotifications;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ta.hyah.StepBloodPresureActivity.heartResult;
import static com.ta.hyah.StepBloodPresureActivity.systole;
import static com.ta.hyah.StepBloodPresureActivity.diastole;
import static com.ta.hyah.StepBodyTempretureActivity.bodyTempreture;
import static com.ta.hyah.StepRespirationRateActivity.respiRate;

public class StepFinishActivity extends AppCompatActivity {

    MaterialButton btnFinishStep;
    MaterialButton btnRetryStep;
    API apiService;
    String TAG = getClass().getSimpleName();
    SharedPreferences preferences;
    OurNotifications notifications;
    String userID;

    TextView tvHR, tvBT, tvRT, tvBP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_finish);
        initView();
        btnFinishTrigger();

        preferences = getSharedPreferences("loginShared", MODE_PRIVATE);
        userID = preferences.getString("id", "PSU000111222");

        notifications = new OurNotifications(this);
    }

    public void initView(){
        btnFinishStep = findViewById(R.id.btnFinishStep);
        btnRetryStep = findViewById(R.id.btnRetryStep);

        tvHR = findViewById(R.id.tvHeartRateF);
        tvBT = findViewById(R.id.tvBodyTempF);
        tvRT = findViewById(R.id.tvRespirationRateF);
        tvBP = findViewById(R.id.tvBloodPresureF);

        tvHR.setText(heartResult);
        tvBT.setText(bodyTempreture);
        tvRT.setText(respiRate);
        tvBP.setText(systole+"/"+diastole);

    }

    public void btnFinishTrigger(){
        btnFinishStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putStuffDataSensor(userID, bodyTempreture, systole, diastole, respiRate, heartResult);
                Intent intent = new Intent(StepFinishActivity.this, MainActivity.class);
                notifications.setupNotification("DR000111", "this is notification", "Alert", "Check your Patient!");
                startActivity(intent);
                finish();
            }
        });

        btnRetryStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StepFinishActivity.this, StepBloodPresureActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void putStuffDataSensor(String id, String temperature, String sistole, String diastole, String respiration_rate, String heart_rate){
        apiService = Constants.getAPIService();
        apiService.dataFinish(new DataFinishRes(id, temperature, sistole, diastole, respiration_rate, heart_rate)).enqueue(new Callback<DataFinishBody>() {
            @Override
            public void onResponse(Call<DataFinishBody> call, Response<DataFinishBody> response) {
                DataFinishBody dataFinishBody = response.body();

            }

            @Override
            public void onFailure(Call<DataFinishBody> call, Throwable t) {

            }
        });
    }
}
