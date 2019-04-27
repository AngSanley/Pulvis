package com.ta.hyah;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ta.hyah.adapter.HistoryAdapter;
import com.ta.hyah.constants.Constants;
import com.ta.hyah.interfaces.API;
import com.ta.hyah.model.HistoryModel;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvHistory;
    TextView tvGreetings;
    AppCompatImageButton btnSettings;
    MaterialButton buttonCheckNow;
    TextView tvName;

    String TAG = getClass().getSimpleName();

    API apiService;

    public static String deviceName;
    public static String deviceMAC;

    String userName;
    String userID;

    SharedPreferences prefs, prefsble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvGreetings = findViewById(R.id.tvGreetings);
        setTvGreetings();

        tvName = findViewById(R.id.tvPaName);
        chekinUserData();

        prefs = getSharedPreferences("loginShared", MODE_PRIVATE);
        userName = prefs.getString("name", "Budi");
        userID = prefs.getString("id", "PSU000111222");
        tvName.setText(userName);

        initBtnCheckNow();

        //RV
        rvHistory = findViewById(R.id.rvHistory);
        Log.d(TAG, "onCreate: "+userID+userName);
        initDataHistory(userID);

        btnSettings = findViewById(R.id.btnSettingsBluetooth);

        FirebaseMessaging.getInstance().subscribeToTopic("AHAY");

        prefsble = getSharedPreferences("bluetoothDevice", MODE_PRIVATE);

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefsble.edit().clear().commit();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void chekinUserData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userName = bundle.getString("user-name");
            userID = bundle.getString("user-id");
            tvName.setText(userName);
            Toast.makeText(getApplicationContext(), userName + userID, Toast.LENGTH_LONG).show();
            Log.d(TAG, "chekinUserData: " + userName + userID);
        }
    }

    //dummy data
    public void initDataHistory(String id) {
        apiService = Constants.getAPIService();
        apiService.dataHistory(id).enqueue(new Callback<List<HistoryModel>>() {
            @Override
            public void onResponse(Call<List<HistoryModel>> call, Response<List<HistoryModel>> response) {
                List<HistoryModel> historyModels = response.body();
                Log.d(TAG, "onResponse: "+response.body().toString());
                HistoryAdapter adapter = new HistoryAdapter(historyModels, MainActivity.this);
                rvHistory.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                rvHistory.smoothScrollToPosition(0);
                rvHistory.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<HistoryModel>> call, Throwable t) {

            }
        });
    }

    public void initBtnCheckNow() {
        buttonCheckNow = findViewById(R.id.btnCheckNow);
        buttonCheckNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dname = prefsble.getString("device-name", null);
                String dMac = prefsble.getString("device-mac", null);
                if (dname != null && dMac != null){
                    Intent intent = new Intent(MainActivity.this, StepBloodPresureActivity.class);
                    intent.putExtra("device_name", dname);
                    intent.putExtra("device_mac", dMac);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void setTvGreetings() {
        Calendar calendar = Calendar.getInstance();
        int TOD = calendar.get(Calendar.HOUR_OF_DAY);

        if (TOD >= 0 && TOD < 12) {
            tvGreetings.setText(getResources().getText(R.string.greetings_morning));
        } else if (TOD >= 12 && TOD < 16) {
            tvGreetings.setText(getResources().getText(R.string.greetings_afternoon));
        } else if (TOD >= 16 && TOD < 21) {
            tvGreetings.setText(getResources().getText(R.string.greetings_evening));
        } else if (TOD >= 21 && TOD < 24) {
            tvGreetings.setText(getResources().getText(R.string.greetings_night));
        }
    }
}