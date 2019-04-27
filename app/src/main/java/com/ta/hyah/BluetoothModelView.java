package com.ta.hyah;

import android.app.Application;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.harrysoft.androidbluetoothserial.BluetoothManager;
import com.harrysoft.androidbluetoothserial.SimpleBluetoothDeviceInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class BluetoothModelView extends AndroidViewModel {
    String message;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    BluetoothManager bluetoothManager;

    SimpleBluetoothDeviceInterface deviceInterface;
    private MutableLiveData<String> messagesData = new MutableLiveData<>();
    // The connection status that the activity sees
    private MutableLiveData<ConnectionStatus> connectionStatusData = new MutableLiveData<>();
    // The device name that the activity sees
    private MutableLiveData<String> deviceNameData = new MutableLiveData<>();
    // The message in the message box that the activity sees
    private MutableLiveData<String> messageData = new MutableLiveData<>();

    private StringBuilder messages = new StringBuilder();

    // Our configuration
    private String deviceName;
    private String mac;

    // A variable to help us not double-connect
    private boolean connectionAttemptedOrMade = false;
    // A variable to help us not setup twice
    private boolean viewModelSetup = false;

    public BluetoothModelView(@NonNull Application application) {
        super(application);
    }

    public void toast(String messageResource) { Toast.makeText(getApplication(), messageResource, Toast.LENGTH_LONG).show(); }

    public boolean setupViewModel(String deviceName, String mac) {
        // Check we haven't already been called
        if (!viewModelSetup) {
            viewModelSetup = true;

            // Setup our BluetoothManager
            bluetoothManager = BluetoothManager.getInstance();
            if (bluetoothManager == null) {
                // Bluetooth unavailable on this device :( tell the user
                toast("GK ada");
                // Tell the activity there was an error and to close
                return false;
            }

            // Remember the configuration
            this.deviceName = deviceName;
            this.mac = mac;

            // Tell the activity the device name so it can set the title
            deviceNameData.postValue(deviceName);
            // Tell the activity we are disconnected.
            connectionStatusData.postValue(ConnectionStatus.DISCONNECTED);
        }
        // If we got this far, nothing went wrong, so return true
        return true;
    }

    public void connect() {
        // Check we are not already connecting or connected
        if (!connectionAttemptedOrMade) {
            // Connect asynchronously
            compositeDisposable.add(bluetoothManager.openSerialDevice(mac)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(device -> onConnected(device.toSimpleDeviceInterface()), t -> {
                        toast("connection_failed");
                        connectionAttemptedOrMade = false;
                        connectionStatusData.postValue(ConnectionStatus.DISCONNECTED);
                    }));
            // Remember that we made a connection attempt.
            connectionAttemptedOrMade = true;
            // Tell the activity that we are connecting.
            connectionStatusData.postValue(ConnectionStatus.CONNECTING);
        }
    }

    public void disconnect() {
        // Check we were connected
        if (connectionAttemptedOrMade && deviceInterface != null) {
            connectionAttemptedOrMade = false;
            // Use the library to close the connection
            bluetoothManager.closeDevice(deviceInterface);
            // Set it to null so no one tries to use it
            deviceInterface = null;
            // Tell the activity we are disconnected
            connectionStatusData.postValue(ConnectionStatus.DISCONNECTED);
        }
    }

    // Called once the library connects a bluetooth device
    private void onConnected(SimpleBluetoothDeviceInterface deviceInterface) {
        this.deviceInterface = deviceInterface;
        if (this.deviceInterface != null) {
            // We have a device! Tell the activity we are connected.
            connectionStatusData.postValue(ConnectionStatus.CONNECTED);
            // Setup the listeners for the interface
            this.deviceInterface.setListeners(this::onMessageReceived, this::onMessageSent, t -> toast("message_send_error"));
            // Tell the user we are connected.
            toast("connected");
            // Reset the conversation
            messages = new StringBuilder();
            messagesData.postValue(messages.toString());
        } else {
            // deviceInterface was null, so the connection failed
            toast("connection_failed");
            connectionStatusData.postValue(ConnectionStatus.DISCONNECTED);
        }
    }

    private void onMessageReceived(String message) {
        messages.append(deviceName).append(": ").append(message).append('\n');
        messagesData.postValue(messages.toString());
        this.message = message;
    }

    public String getDataMessage(){
        if (message == null) {
            return "no message";
        }
        return message;
    }

    // Adds a sent message to the conversation
    private void onMessageSent(String message) {
        // Add it to the conversation
        messages.append("You").append(": ").append(message).append('\n');
        messagesData.postValue(messages.toString());
        // Reset the message box
        messageData.postValue("");
    }

    public void sendMessage(String message) {
        // Check we have a connected device and the message is not empty, then send the message
        if (deviceInterface != null && !TextUtils.isEmpty(message)) {
            deviceInterface.sendMessage(message);
        }
    }

    @Override
    protected void onCleared() {
        // Dispose any asynchronous operations that are running
        compositeDisposable.dispose();
        // Shutdown bluetooth connections
        bluetoothManager.close();
    }

    public LiveData<String> getMessages() { return messagesData; }

    // Getter method for the activity to use.
    public LiveData<ConnectionStatus> getConnectionStatus() { return connectionStatusData; }

    // Getter method for the activity to use.
    public LiveData<String> getDeviceName() { return deviceNameData; }

    // Getter method for the activity to use.
    public LiveData<String> getMessage() { return messageData; }

    enum ConnectionStatus {
        DISCONNECTED,
        CONNECTING,
        CONNECTED
    }
}
