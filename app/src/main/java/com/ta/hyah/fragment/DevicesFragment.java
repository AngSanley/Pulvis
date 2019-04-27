package com.ta.hyah.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.ta.hyah.MainActivity;
import com.ta.hyah.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class DevicesFragment extends ListFragment {

    private Menu menu;
    private final BluetoothAdapter bluetoothAdapter;
    private BroadcastReceiver bleDiscoveryBroadcastReceiver;
    private IntentFilter bleDiscoveryIntentFilter;

    private ArrayList<BluetoothDevice> listItems = new ArrayList<>();
    private ArrayAdapter<BluetoothDevice> listAdapter;

    String TAG = getClass().getSimpleName();

    public DevicesFragment() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bleDiscoveryBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device.getType() != BluetoothDevice.DEVICE_TYPE_CLASSIC) {
                        getActivity().runOnUiThread(() -> updateScan(device));
                    }
                }
                if (intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                    stopScan();
                }
            }
        };
        bleDiscoveryIntentFilter = new IntentFilter();
        bleDiscoveryIntentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        bleDiscoveryIntentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        listAdapter = new ArrayAdapter<BluetoothDevice>(getActivity(), 0, listItems) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                BluetoothDevice device = listItems.get(position);
                if (convertView == null)
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_devices, parent, false);
                TextView text1 = convertView.findViewById(R.id.text1);
                TextView text2 = convertView.findViewById(R.id.text2);
                if (device.getName() == null || device.getName().isEmpty())
                    text1.setText("<unnamed>");
                else
                    text1.setText(device.getName());

                text2.setText(device.getAddress());
                return convertView;
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(null);
        View header = getActivity().getLayoutInflater().inflate(R.layout.device_list_header, null, false);
        getListView().addHeaderView(header, null, false);
        setEmptyText("Initializing..");
        ((TextView) getListView().getEmptyView()).setTextSize(18);
        setListAdapter(listAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_devices, menu);
        this.menu = menu;
        if (Objects.requireNonNull(getActivity()).getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH))
            menu.findItem(R.id.bt_settings).setEnabled(true);
        if (bluetoothAdapter == null || !getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
            menu.findItem(R.id.ble_scan).setEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(bleDiscoveryBroadcastReceiver, bleDiscoveryIntentFilter);
        if (bluetoothAdapter == null || !getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
            setEmptyText("<Bluetooth LE no suported>");
        else if (!bluetoothAdapter.isEnabled())
            setEmptyText("<Bluetooth is disabled, click SCAN to find LE Device>");
        else
            setEmptyText("<use SCAN to refresh devices>");
    }

    @Override
    public void onPause() {
        super.onPause();
        stopScan();
        getActivity().unregisterReceiver(bleDiscoveryBroadcastReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.ble_scan:
                startScan();
                return true;
            case R.id.ble_scan_stop:
                stopScan();
                return true;
            case R.id.bt_settings:
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivity(intent);
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    private void startScan(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Location permission required");
                builder.setMessage("This app does not use location information, but scanning for Bluetooth LE devices requires this permission");
                builder.setPositiveButton(android.R.string.ok,
                        (dialog, which) -> requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0));
                builder.show();
                return;
            }
        }
        listItems.clear();
        listAdapter.notifyDataSetChanged();
        setEmptyText("<scanning>");
        menu.findItem(R.id.ble_scan).setVisible(false);
        menu.findItem(R.id.ble_scan_stop).setVisible(true);
        bluetoothAdapter.startDiscovery();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            new Handler(Looper.getMainLooper()).postDelayed(this::startScan, 1);
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Location permission denied");
            builder.setMessage("As location permission has not been granted, this app cannot scan for Bluetooth LE devices.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.show();
        }
    }

    private void updateScan(BluetoothDevice device) {
        if (listItems.indexOf(device) < 0) {
            listItems.add(device);
            Collections.sort(listItems, DevicesFragment::compareTo);
            listAdapter.notifyDataSetChanged();
        }
    }

    private void stopScan(){
        setEmptyText("<No bluetooth devices>");
        if (menu != null){
            menu.findItem(R.id.ble_scan).setVisible(true);
            menu.findItem(R.id.ble_scan_stop).setVisible(false);
        }
        bluetoothAdapter.cancelDiscovery();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        stopScan();
        BluetoothDevice device = listItems.get(position-1);
        Bundle args = new Bundle();
        args.putString("device", device.getAddress());

        Intent mainIntent = new Intent(getContext(), MainActivity.class);
        mainIntent.putExtras(args);
        startActivity(mainIntent);
        Log.d(TAG, "onListItemClick: "+args);
        /*Fragment fragment = new TerminalFragment();
        fragment.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.fragmentBluetooth, fragment, "terminal").addToBackStack(null).commit();*/
    }

    /**
     * sort by name, then address. sort named devices first
     */
    static int compareTo(BluetoothDevice a, BluetoothDevice b) {
        boolean aValid = a.getName()!=null && !a.getName().isEmpty();
        boolean bValid = b.getName()!=null && !b.getName().isEmpty();
        if(aValid && bValid) {
            int ret = a.getName().compareTo(b.getName());
            if (ret != 0) return ret;
            return a.getAddress().compareTo(b.getAddress());
        }
        if(aValid) return -1;
        if(bValid) return +1;
        return a.getAddress().compareTo(b.getAddress());
    }
}
