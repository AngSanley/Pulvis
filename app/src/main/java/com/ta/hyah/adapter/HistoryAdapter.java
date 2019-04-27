package com.ta.hyah.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ta.hyah.DetailPatienActivity;
import com.ta.hyah.R;
import com.ta.hyah.constants.Constants;
import com.ta.hyah.interfaces.API;
import com.ta.hyah.model.DataStatusModel;
import com.ta.hyah.model.DataStatusReq;
import com.ta.hyah.model.HistoryModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<HistoryModel> historyModelVector;
    private Context mContext;

    public HistoryAdapter(List<HistoryModel> historyModelVector, Context mContext) {
        this.historyModelVector = historyModelVector;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryViewHolder(LayoutInflater.from(mContext).inflate(R.layout.card_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.setupHistory(historyModelVector.get(position));
    }

    @Override
    public int getItemCount() {
        return historyModelVector.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView historyDate;
        TextView historyTime;
        TextView historyHearthRate;
        TextView historyRespirationRate;
        TextView historyBodyTemperature;
        TextView historyBloodPresure;

        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            historyDate = itemView.findViewById(R.id.tvHistoryDate);
            historyTime = itemView.findViewById(R.id.tvHistoryTime);
            historyHearthRate = itemView.findViewById(R.id.tvHistoryHeartRate);
            historyRespirationRate = itemView.findViewById(R.id.tvHistoryLungsRate);
            historyBodyTemperature = itemView.findViewById(R.id.tvHistoryThermometer);
            historyBloodPresure = itemView.findViewById(R.id.tvHistoryPresureRate);
            cardView = itemView.findViewById(R.id.cardHistory);
        }

        @SuppressLint("SetTextI18n")
        void setupHistory(HistoryModel historyModel) {
            String blood = historyModel.getSistole() + "/" + historyModel.getDiastole();

            String docktorName = historyModel.getName();
            String spesialis = historyModel.getSpecialist();
            Log.d("huhu", "setupHistory: " + spesialis);
            String doctorPhone = historyModel.getDoctor_phone();
            String emergencyPhone = historyModel.getEmergency_contact();

            historyDate.setText(historyModel.getDate());
            historyTime.setText(historyModel.getTime());
            historyHearthRate.setText(historyModel.getHeart_rate());
            historyRespirationRate.setText(historyModel.getRespiration_rate());
            historyBodyTemperature.setText(historyModel.getTemperature());
            historyBloodPresure.setText(blood);

            SharedPreferences preferences = mContext.getSharedPreferences("loginShared", mContext.MODE_PRIVATE);
            String id = preferences.getString("id", "PSU000111222");

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, DetailPatienActivity.class);

                    API apiService = Constants.getAPIService();
                    apiService.dataStatus(new DataStatusReq(id, historyModel.getTemperature(), historyModel.getSistole(), historyModel.getDiastole(), historyModel.getHeart_rate(), historyModel.getRespiration_rate())).enqueue(new Callback<DataStatusModel>() {
                        @Override
                        public void onResponse(Call<DataStatusModel> call, Response<DataStatusModel> response) {
                            DataStatusModel dataStatusModel = response.body();
                            intent.putExtra("indication", dataStatusModel.getIndication());
                            intent.putExtra("heart_status", dataStatusModel.getHeart_status());
                            intent.putExtra("respiration_status", dataStatusModel.getRespiration_status());
                            intent.putExtra("blood_pressure_status", dataStatusModel.getBlood_pressure_status());
                            intent.putExtra("temperature_status", dataStatusModel.getTemperature_status());
                            intent.putExtra("status", dataStatusModel.getStatus());

                            mContext.startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<DataStatusModel> call, Throwable t) {

                        }
                    });

                    intent.putExtra("date", historyModel.getDate());
                    intent.putExtra("time", historyModel.getTime());
                    intent.putExtra("heart-rate", historyModel.getHeart_rate());
                    intent.putExtra("respi-rate", historyModel.getRespiration_rate());
                    intent.putExtra("body-temp", historyModel.getTemperature());
                    intent.putExtra("blood-press", blood);
                    intent.putExtra("doctor-name", docktorName);
                    intent.putExtra("spesialis", spesialis);
                    intent.putExtra("doctor-phone", doctorPhone);
                    intent.putExtra("emergency", emergencyPhone);



                }
            });
        }
    }
}
