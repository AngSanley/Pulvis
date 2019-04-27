package com.ta.hyah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.ta.hyah.constants.Constants;
import com.ta.hyah.interfaces.API;
import com.ta.hyah.model.PasienModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText text;
    MaterialButton materialButton;
    API apiService;
    String TAG = getClass().getSimpleName();
    SharedPreferences sharedPreferences;
    String userName;
    String userID;
    String userStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        text = findViewById(R.id.ikutDongNanti);
        materialButton = findViewById(R.id.btnSubmit);

        sharedPreferences = this.getSharedPreferences("loginShared", MODE_PRIVATE);

        if (!sharedPreferences.getString("id", "invalid").equals("invalid")){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sa = text.getText().toString();
                doLogin(sa);
                //volleyLogin(sa);
            }
        });
    }

    public void doLogin(String idLogin) {
        apiService = Constants.getAPIService();
        sendDataLogin(idLogin);
    }

    public void sendDataLogin(String id) {
        apiService.goLogin(id).enqueue(new Callback<PasienModel>() {
            @Override
            public void onResponse(Call<PasienModel> call, Response<PasienModel> response) {
                PasienModel pasienModel = response.body();
                userName = pasienModel.getName();
                userID = pasienModel.getId();
                userStatus = pasienModel.getLoginStatus();
                if (userStatus.equals("success")) {
                    Toast.makeText(getApplicationContext(), "mantul username: "+userName+" id "+userID, Toast.LENGTH_LONG).show();
                    initShared(id, userName, userStatus);
                    Bundle bundle = new Bundle();
                    bundle.putString("user-name", userName);
                    bundle.putString("user-id", userID);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<PasienModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t);
            }
        });
    }

    public void initShared(String idUser, String nameUser, String status){
        SharedPreferences.Editor editor = getSharedPreferences("loginShared", MODE_PRIVATE).edit();
        editor.putString("id", idUser);
        editor.putString("name", nameUser);
        editor.putString("loginstatus", status);
        editor.apply();
    }
}