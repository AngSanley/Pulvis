package com.ta.hyah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ta.hyah.constants.Constants;
import com.ta.hyah.constants.DataSensorConstans;
import com.ta.hyah.model.DataBloodPresure;
import com.ta.hyah.socket.Pressure;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class StepBloodPresureActivity extends AppCompatActivity {

    String deviceName;
    String deviceMAC;

    public static String systole;
    public static String diastole;
    public static String heartResult;

    Pressure pressure;

    String TAG = getClass().getSimpleName();

    BluetoothModelView viewModel;

    RingProgressBar ringProgressBar;
    Button buttonS, btnConnect, btnNext;

    int progress = 0;

    TextView textViewResult, textViewSatuan, textViewResultH, textViewSatuanH;

    LinearLayout linearLayoutBlood, linearLayoutHeart;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_blood_presure);
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
                //btnNext.setEnabled(true);
            }
        });
    }

    public void initView() {
        buttonS = findViewById(R.id.btnStartBlood);
        ringProgressBar = findViewById(R.id.progress_bar_blood);
        textViewResult = findViewById(R.id.tvResultBlood);
        textViewSatuan = findViewById(R.id.tvSatuanBlood);
        textViewResultH = findViewById(R.id.tvResultHeart);
        textViewSatuanH = findViewById(R.id.tvSatuanHeart);
        btnConnect = findViewById(R.id.btnConBlood);
        btnNext = findViewById(R.id.btnNextStepBlood);
        //btnNext.setEnabled(false);

        linearLayoutBlood = findViewById(R.id.linearBlood);
        linearLayoutHeart = findViewById(R.id.linearHeart);

        linearLayoutBlood.setVisibility(View.INVISIBLE);
        linearLayoutHeart.setVisibility(View.INVISIBLE);
        textViewSatuan.setText(getResources().getString(R.string.mmhg));
        textViewSatuanH.setText(getResources().getString(R.string.bpm));
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
                viewModel.sendMessage("pressure,1#");
                buttonS.setEnabled(false);
                buttonS.setVisibility(View.GONE);
                @SuppressLint("HandlerLeak") Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 0) {
                            if (progress < 85) {
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
                                Thread.sleep(500);
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

        viewModel.getConnectionStatus().observe(this, this::onConnectionStatus);
        viewModel.getDeviceName().observe(this, name -> setTitle(getString(R.string.device_name_format, name)));
        viewModel.getMessages().observe(this, message -> {

            if (TextUtils.isEmpty(message)) {
                message = "NO message";
            }
            Log.d(TAG, viewModel.getDataMessage());
            String msg = viewModel.getDataMessage();
            if (!msg.equals("no message")) {
                if (msg.equals("ahay")) {
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
                                    Thread.sleep(50);
                                    handler.sendEmptyMessage(0);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                    ArrayList<String> geee = new ArrayList<>();
                    for (int i = 0; i < DataSensorConstans.arrayListBloodPresures.size(); i++) {
                        geee.add(DataSensorConstans.arrayListBloodPresures.get(i).getsBloodPresure());
                    }
                    pressure = new Pressure(geee);
                    textViewResult.setText(pressure.getPressure());
                    textViewResultH.setText(pressure.getHeartRate());

                    linearLayoutBlood.setVisibility(View.VISIBLE);
                    linearLayoutHeart.setVisibility(View.VISIBLE);

                    systole = String.valueOf(pressure.systole);
                    diastole = String.valueOf(pressure.diastole);
                    heartResult = String.valueOf(pressure.getHeartRate());

                    Log.d(TAG, "bluetoothArea: " + systole + "/" + diastole);

                    return;
                }
                Pattern p = Pattern.compile("^[-+]?[0-9]*\\.?[0-9]+$");
                Matcher m = p.matcher(msg);
                if (m.find()) {
                    DataSensorConstans.arrayListBloodPresures.add(new DataBloodPresure(msg));
                }
            }
        });
        viewModel.getMessage().observe(this, message -> {
            // Only update the message if the ViewModel is trying to reset it
            if (TextUtils.isEmpty(message)) {

            }
        });

        deviceName = getIntent().getStringExtra("device_name");
        deviceMAC = getIntent().getStringExtra("device_mac");

        Log.d(TAG, "bluetoothArea: " + deviceName + deviceMAC);

        btnNext.setOnClickListener(View -> openBodyTempt(deviceName, deviceMAC));
    }

    public void openBodyTempt(String deviceName, String macAddress) {
        Intent intent = new Intent(this, StepBodyTempretureActivity.class);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.disconnect();
        Log.d(TAG, "onDestroy: " + deviceName + " " + deviceMAC);
    }

    /*@Override
    public void onBackPressed() {

    }*/
}
