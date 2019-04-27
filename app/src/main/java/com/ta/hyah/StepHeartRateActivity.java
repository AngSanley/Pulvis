package com.ta.hyah;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class StepHeartRateActivity extends AppCompatActivity {

    RingProgressBar ringProgressBar;
    Button button;

    int progress = 0;
    int progressRetry = 0;

    TextView textViewResult, textViewSatuan;

    String deviceName;
    String deviceMAC;

    String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_heart_rate);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            deviceName = bundle.getString("device_name");
            deviceMAC = bundle.getString("device_mac");
            Log.d(TAG, "onCreate: " + deviceName + deviceMAC);
        }

        ringProgressBar = findViewById(R.id.progress_bar_2);
        ringProgressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {
            @Override
            public void progressToComplete() {
                textViewResult.setVisibility(View.VISIBLE);
                textViewSatuan.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Complete", Toast.LENGTH_LONG).show();
            }
        });

        textViewResult = findViewById(R.id.tvResult);
        textViewSatuan = findViewById(R.id.tvSatuan);

        textViewResult.setVisibility(View.GONE);
        textViewSatuan.setVisibility(View.GONE);

        @SuppressLint("HandlerLeak") Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0){
                    if (progress<100){
                        progress++;
                        ringProgressBar.setProgress(progress);
                    }
                }
            }
        };

        @SuppressLint("HandlerLeak") Handler handlerRetry = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0){
                    if (progressRetry<100){
                        progressRetry++;
                        ringProgressBar.setProgress(progressRetry);
                    }
                    if (progressRetry == 100){
                        progressRetry = 0;
                        if (progressRetry<100){
                            progressRetry++;
                            ringProgressBar.setProgress(progressRetry);
                        }

                    }
                }
            }
        };

        button = findViewById(R.id.btnStart);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.GONE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 100; i++) {
                            try{
                                Thread.sleep(100);
                                handler.sendEmptyMessage(0);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });
    }
}
