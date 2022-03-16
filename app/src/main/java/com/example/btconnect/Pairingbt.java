package com.example.btconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Pairingbt extends AppCompatActivity {
    final String TAG = "SubActivity";
    ArrayAdapter<String> pairingAdapter, scanAdapter;
    ListView listView_pairing, listView_scan;
    ArrayList<String> pairingList, scanList;
    Button bt_cancel, bt_scan;
    BluetoothAdapter myBluetoothAdapter;
    protected static UUID MY_UUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pairingbt);

        bt_cancel = (Button) findViewById(R.id.bt_cancel);
        bt_scan = (Button) findViewById(R.id.bt_scan);
        listView_pairing = (ListView) findViewById(R.id.listview_pairing);
        listView_scan = (ListView) findViewById(R.id.listview_scan);


        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        pairingList = new ArrayList<>();

        scanList = new ArrayList<>();

        MY_UUID = UUID.randomUUID();
        Log.d(TAG, MY_UUID.toString());



        Set<BluetoothDevice> pairedDevices = myBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                pairingList.add(device.getName() + "\n" + device.getAddress());//기기 이름과 맥어드레스를 추가한다.
            }
        }
        pairingAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, pairingList);//커스텀 레이아웃 사용함
        listView_pairing.setAdapter(pairingAdapter);

        listView_pairing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) adapterView.getItemAtPosition(i);
                Toast.makeText(getApplicationContext(), "연결기기: " + selectedItem, Toast.LENGTH_SHORT).show();

            }
        });

        bt_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myBluetoothAdapter.isDiscovering()) {
                    myBluetoothAdapter.cancelDiscovery();
                }
                scanList.clear();
                myBluetoothAdapter.startDiscovery();
            }
        });

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(myReceiver, intentFilter);

        scanAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, scanList);
        listView_scan.setAdapter(scanAdapter);

        //리스트 항목 클릭시
        listView_scan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) adapterView.getItemAtPosition(i);
                Toast.makeText(getApplicationContext(), "연결기기: " + selectedItem, Toast.LENGTH_SHORT).show();
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                scanList.add(device.getName());
                scanAdapter.notifyDataSetChanged();
            }
        }
    };
}