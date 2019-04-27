package com.ta.hyah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.harrysoft.androidbluetoothserial.BluetoothManager;
import com.ta.hyah.fragment.DevicesFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.annotations.NonNull;

public class SettingsActivity extends AppCompatActivity {

    private MainActivityViewModel viewModel;
    String TAG = getClass().getSimpleName();

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbarSettings);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Bluetooth");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        if (!viewModel.setupViewModel()) {
            finish();
            return;
        }

        RecyclerView deviceList = findViewById(R.id.rvBluetooth);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.rvSwipe);

        deviceList.setLayoutManager(new LinearLayoutManager(this));
        DeviceAdapter adapter = new DeviceAdapter();
        deviceList.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.refreshPairedDevices();
            swipeRefreshLayout.setRefreshing(false);
        });
        viewModel.getPairedDeviceList().observe(SettingsActivity.this, adapter::updateList);
        viewModel.refreshPairedDevices();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public void openCommunicationsActivity(String deviceName, String macAddress) {
        Log.d(TAG, "openCommunicationsActivity: ");
        Intent intent = new Intent(this, StepBloodPresureActivity.class);
        intent.putExtra("device_name", deviceName);
        intent.putExtra("device_mac", macAddress);
        initShared(deviceName, macAddress);
        startActivity(intent);
        finish();
    }

    private class DeviceViewHolder extends RecyclerView.ViewHolder {

        private final RelativeLayout layout;
        private final TextView text1;
        private final TextView text2;

        DeviceViewHolder(View view) {
            super(view);
            layout = view.findViewById(R.id.list_item);
            text1 = view.findViewById(R.id.list_item_text1);
            text2 = view.findViewById(R.id.list_item_text2);
        }

        void setupView(BluetoothDevice device) {
            text1.setText(device.getName());
            text2.setText(device.getAddress());
            layout.setOnClickListener(view -> openCommunicationsActivity(device.getName(), device.getAddress()));
        }
    }

    private class DeviceAdapter extends RecyclerView.Adapter<DeviceViewHolder> {

        private List<BluetoothDevice> deviceList = new ArrayList<>();

        @NonNull
        @Override
        public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DeviceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
            holder.setupView(deviceList.get(position));
        }

        @Override
        public int getItemCount() {
            return deviceList.size();
        }

        void updateList(List<BluetoothDevice> deviceList) {
            this.deviceList = deviceList;
            notifyDataSetChanged();
        }
    }

    public void initShared(String deviceName, String deviceMac){
        SharedPreferences.Editor editor = getSharedPreferences("bluetoothDevice", MODE_PRIVATE).edit();
        editor.putString("device-name", deviceName);
        editor.putString("device-mac", deviceMac);
        editor.apply();
    }
}
