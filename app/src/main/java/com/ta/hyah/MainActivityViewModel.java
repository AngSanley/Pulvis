package com.ta.hyah;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.harrysoft.androidbluetoothserial.BluetoothManager;

import java.util.List;

import io.reactivex.annotations.NonNull;

public class MainActivityViewModel extends AndroidViewModel {

    private BluetoothManager bluetoothManager;
    private MutableLiveData<List<BluetoothDevice>> pairedDeviceList = new MutableLiveData<>();
    private boolean viewModelSetup = false;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    View v;

    public boolean setupViewModel() {
        if (!viewModelSetup) {
            viewModelSetup = true;
            bluetoothManager = BluetoothManager.getInstance();
            if (bluetoothManager == null) {
                Toast.makeText(getApplication(), "Tydak ada bluetooth", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    public void refreshPairedDevices() {
        pairedDeviceList.postValue(bluetoothManager.getPairedDevicesList());
    }

    @Override
    protected void onCleared() {
        bluetoothManager.close();
    }

    public LiveData<List<BluetoothDevice>> getPairedDeviceList() { return pairedDeviceList; }
}
