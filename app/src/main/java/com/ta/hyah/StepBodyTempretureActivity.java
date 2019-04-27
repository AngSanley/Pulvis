package com.ta.hyah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ta.hyah.constants.DataSensorConstans;
import com.ta.hyah.model.DataBloodPresure;
import com.ta.hyah.model.DataBodyTemprature;
import com.ta.hyah.socket.Pressure;

import java.util.ArrayList;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class StepBodyTempretureActivity extends AppCompatActivity {

    String deviceName;
    String deviceMAC;

    Button btnNext, btnConnect, buttonS;
    String TAG = getClass().getSimpleName();
    BluetoothModelView viewModel;

    RingProgressBar ringProgressBar;

    public static String bodyTempreture;

    TextView textViewResult, textViewSatuan;

    String msgL;

    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_body_tempreture);
        initView();
        bluetoothArea();

        ringHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    btnConnect.callOnClick();
                }
            }
        }).start();

        ringProgressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {
            @Override
            public void progressToComplete() {
                Toast.makeText(getApplicationContext(), "Complete", Toast.LENGTH_LONG).show();
                textViewResult.setVisibility(View.VISIBLE);
                textViewResult.setText(msgL + getResources().getString(R.string.celcius));
            }
        });
    }

    public void initView() {
        btnNext = findViewById(R.id.btnNextStepTempr);
        btnConnect = findViewById(R.id.btnConTempr);
        buttonS = findViewById(R.id.btnStartTempr);
        ringProgressBar = findViewById(R.id.progress_bar_tempr);
        textViewResult = findViewById(R.id.tvResultTempr);
        textViewSatuan = findViewById(R.id.tvSatuanTempr);

        textViewResult.setVisibility(View.GONE);
    }

    public void ringHandler() {
        @SuppressLint("HandlerLeak") Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    if (progress < 100) {
                        progress++;
                        ringProgressBar.setProgress(progress);
                    }
                }
            }
        };

        buttonS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.sendMessage("temperature,1#");
                buttonS.setEnabled(false);
                buttonS.setVisibility(View.GONE);
                @SuppressLint("HandlerLeak") Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 0) {
                            if (progress < 100) {
                                progress++;
                                ringProgressBar.setProgress(progress);
                            }
                        }
                    }
                };

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 100; i++) {
                            try {
                                Thread.sleep(100);
                                handler.sendEmptyMessage(0);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });
    }

    public void bluetoothArea() {
        viewModel = ViewModelProviders.of(this).get(BluetoothModelView.class);

        if (!viewModel.setupViewModel(getIntent().getStringExtra("device_name"), getIntent().getStringExtra("device_mac"))) {
            finish();
            return;
        }

        viewModel.getMessage().observe(this, message -> {
            if (TextUtils.isEmpty(message)) {
                Log.d(TAG, "onCreate: messageView Upd: " + message);
            }
        });
        viewModel.getConnectionStatus().observe(this, this::onConnectionStatus);
        viewModel.getDeviceName().observe(this, name -> setTitle(getString(R.string.device_name_format, name)));
        viewModel.getMessages().observe(this, message -> {
            if (TextUtils.isEmpty(message)) {
                message = "No message";
            }
            String msg = viewModel.getDataMessage();
            Log.d(TAG, "onCreate: messageView: " + msg);
            DataSensorConstans.arrayListBodyTemprature.add(new DataBodyTemprature(msg));
            msgL = msg;
            bodyTempreture = msg;
        });

        deviceName = getIntent().getStringExtra("device_name");
        deviceMAC = getIntent().getStringExtra("device_mac");

        Log.d(TAG, "bluetoothArea: " + deviceName + deviceMAC);

        btnNext.setOnClickListener(View -> openRespiRate(deviceName, deviceMAC));
    }

    public void openRespiRate(String deviceName, String macAddress) {
        Intent intent = new Intent(this, StepRespirationRateActivity.class);
        intent.putExtra("device_name", deviceName);
        intent.putExtra("device_mac", macAddress);
        startActivity(intent);
        viewModel.disconnect();
    }

    private void onConnectionStatus(BluetoothModelView.ConnectionStatus connectionStatus) {
        switch (connectionStatus) {
            case CONNECTED:
                Log.d(TAG, "onConnectionStatus: Connected");
                Toast.makeText(getApplicationContext(), "connected", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onConnectionStatus: Connected " + deviceName + " " + deviceMAC);
                buttonS.setEnabled(true);
                btnConnect.setEnabled(true);
                btnConnect.setText("Disconnect");
                btnConnect.setOnClickListener(v -> viewModel.disconnect());
                break;

            case CONNECTING:
                Log.d(TAG, "onConnectionStatus: Connecting");
                Toast.makeText(getApplicationContext(), "Connecting", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onConnectionStatus: Connecting " + deviceName + " " + deviceMAC);
                btnConnect.setEnabled(false);
                btnConnect.setText("Connect");
                break;

            case DISCONNECTED:
                Log.d(TAG, "onConnectionStatus: Disconnected");
                Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onConnectionStatus: Disconnected " + deviceName + " " + deviceMAC);
                buttonS.setEnabled(false);
                btnConnect.setEnabled(true);
                btnConnect.setText("Connect");
                btnConnect.setOnClickListener(v -> viewModel.connect());
                break;
        }
    }

   /* @Override
    public void onBackPressed() {

    }*/
}
