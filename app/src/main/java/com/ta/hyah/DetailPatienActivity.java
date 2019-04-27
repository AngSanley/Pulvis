package com.ta.hyah;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.internal.Util;

public class DetailPatienActivity extends AppCompatActivity {

    AppCompatImageButton aCIBtnBack, aCIBtnCall, aCIBtnMsg;
    String[] personCall = {"Docter number", "Emergency number"};

    TextView tvDocter, tvSpesialis, tvDate, tvTime, tvHeart, tvBodyT, tvRespi, tvBloodP;
    TextView tvStatusHeart, tvStatusBody, tvStatusRespi, tvStatusBlood, tvIndication;

    String date, time, heartRate, respiRate, tempR, bloodPress, doctorName, spesialis, doctorPhone, emergency;

    String indication, heartStatus, respiStatus, bloodStatus, temprStatus, status;

    String TAG = getClass().getSimpleName();

    public void chekinData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            date = bundle.getString("date");
            time = bundle.getString("time");
            heartRate = bundle.getString("heart-rate");
            respiRate = bundle.getString("respi-rate");
            tempR = bundle.getString("body-temp");
            bloodPress = bundle.getString("blood-press");
            doctorName = bundle.getString("doctor-name");
            spesialis = bundle.getString("spesialis");
            doctorPhone = bundle.getString("doctor-phone");
            emergency = bundle.getString("emergency");

            indication = bundle.getString("indication");
            heartStatus = bundle.getString("heart_status");
            respiStatus = bundle.getString("respiration_status");
            bloodStatus = bundle.getString("blood_pressure_status");
            temprStatus = bundle.getString("temperature_status");
            status = bundle.getString("status");
        }
    }

    public void setupData() {
        tvDocter.setText(doctorName);
        tvSpesialis.setText(spesialis);
        tvDate.setText(date);
        tvTime.setText(time);
        tvHeart.setText(heartRate);
        tvBodyT.setText(tempR + getResources().getString(R.string.celcius));
        tvRespi.setText(respiRate);
        tvBloodP.setText(bloodPress);


        tvIndication.setText(indication);
        tvStatusHeart.setText(heartStatus);
        tvStatusBlood.setText(bloodStatus);
        tvStatusBody.setText(temprStatus);
        tvStatusRespi.setText(respiStatus);

        if (tvIndication.getText().toString().isEmpty()) {
            tvIndication.setVisibility(View.GONE);
        }

        if (tvStatusHeart.getText().toString().equals("abnormal")){
            tvStatusHeart.setTextColor(getColor(R.color.colorRed));
        }

        if (tvStatusBlood.getText().toString().equals("abnormal")){
            tvStatusBlood.setTextColor(getColor(R.color.colorRed));
        }

        if (tvStatusBody.getText().toString().equals("abnormal")){
            tvStatusBody.setTextColor(getColor(R.color.colorRed));
        }

        if (tvStatusRespi.getText().toString().equals("abnormal")){
            tvStatusRespi.setTextColor(getColor(R.color.colorRed));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_patien);
        initView();
        clickView();
        chekinData();
        setupData();

        Log.d(TAG, "onCreate: " + spesialis);
    }

    public void initView() {
        aCIBtnBack = findViewById(R.id.btnBack);
        aCIBtnCall = findViewById(R.id.btnCallD);
        aCIBtnMsg = findViewById(R.id.btnMessageD);

        tvDocter = findViewById(R.id.tvDoctorName);
        tvSpesialis = findViewById(R.id.tvSpesialis);
        tvDate = findViewById(R.id.tvDetailHistoryDate);
        tvTime = findViewById(R.id.tvDetailHistoryTime);
        tvHeart = findViewById(R.id.tvHeartRate);
        tvBodyT = findViewById(R.id.tvBodyTemp);
        tvRespi = findViewById(R.id.tvRespirationRate);
        tvBloodP = findViewById(R.id.tvBloodPresure);

        tvIndication = findViewById(R.id.tvStatusDiagnosa);
        tvStatusHeart = findViewById(R.id.tvStatusHeartRate);
        tvStatusBody = findViewById(R.id.tvStatusBody);
        tvStatusRespi = findViewById(R.id.tvStatusRespirationRate);
        tvStatusBlood = findViewById(R.id.tvStatusblood_presure);
    }

    public void clickView() {
        aCIBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        aCIBtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogList("Telepon", personCall);
            }
        });

        String message = "Tset wa ne";
        String nWAnumber = "+6287776613548";
        aCIBtnMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoWA(nWAnumber, message);
            }
        });
    }

    public void gotoWA(String nWAnumber, String message) {
        boolean checkinWA = checkWA("com.whatsapp");
        if (checkinWA) {
            Uri uri = Uri.parse("smsto:" + nWAnumber);
            Intent msgM = new Intent(Intent.ACTION_SENDTO, uri)
                    .setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"))
                    .putExtra(Intent.EXTRA_TEXT, message)
                    .setPackage("com.whatsapp");
            startActivity(msgM);

            /*try {
                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "");
                sendIntent.putExtra("jid", nWAnumber + "@s.whatsapp.net");
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);
            } catch (Exception e) {
                Toast.makeText(this, "Error/n" + e.toString(), Toast.LENGTH_SHORT).show();
            }*/

        } else {
            Toast.makeText(getApplicationContext(), "NO WA", Toast.LENGTH_LONG).show();
        }
    }

    public boolean checkWA(String uri) {
        PackageManager packageManager = getPackageManager();
        boolean app_installed = false;
        try {
            packageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return app_installed;
    }

    public void customDialogList(String title, String[] listItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setItems(listItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                String phoneNumberDoctor = doctorPhone;
                                intentCall(phoneNumberDoctor);
                                break;
                            case 1:
                                String phoneNumberClosed = emergency;
                                intentCall(phoneNumberClosed);
                                break;
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void intentCall(String phoneNumber) {
        Intent callM = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
        startActivity(callM);
    }

}
