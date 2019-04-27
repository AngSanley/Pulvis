package com.ta.hyah.notificationsm;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ta.hyah.constants.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OurNotifications {

    Context context;

    private String TAG = getClass().getSimpleName();

    public OurNotifications(Context context) {
        this.context = context;
    }

    public void setupNotification(String toTopic, String extraInfo, String notifTitle, String notifMsg) {
        JSONObject jsParent = new JSONObject();
        try {
            jsParent.put("to", "/topics/" + toTopic);

            JSONObject jsDataIsi = new JSONObject();
            jsDataIsi.put("extra_information", extraInfo);

            JSONObject jsNotifikasiIsi = new JSONObject();
            jsNotifikasiIsi.put("title", notifTitle);
            jsNotifikasiIsi.put("text", notifMsg);

            jsParent.put("data", jsDataIsi);
            jsParent.put("notification", jsNotifikasiIsi);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.URL + "send", jsParent, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: " + response.toString());
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", Constants.AUTHKEY);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}
