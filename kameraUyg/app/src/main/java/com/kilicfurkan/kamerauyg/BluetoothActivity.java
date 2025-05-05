package com.kilicfurkan.kamerauyg;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothActivity extends AppCompatActivity {

    private static final int REQUEST_BLUETOOTH_PERMISSION = 1;
    private BluetoothAdapter adapter;

    private Set<BluetoothDevice> pairedDevices;

    ListView lv;

    Button bluetoothAc;
    Button bluetoothKapat;
    Button listele;
    Button gorunurOl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bluetooth);

        bluetoothAc = findViewById(R.id.btnAc);
        bluetoothKapat = findViewById(R.id.btnKapat);
        listele = findViewById(R.id.btnListele);
        gorunurOl = findViewById(R.id.btnGorun);
        lv = (ListView) findViewById(R.id.listView);

        adapter = BluetoothAdapter.getDefaultAdapter();

        if (adapter == null) {
            finish();
        } else {
            checkAndRequestPermissions();
        }

    }

    private void initializeBluetooth() {
        bluetoothAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                on(v);
            }
        });

        bluetoothKapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                off(v);
            }
        });

        listele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list(v);
            }
        });

        gorunurOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeVisible(v);
            }
        });
    }

    public void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                    REQUEST_BLUETOOTH_PERMISSION);
        } else {
            Toast.makeText(this, "Bluetooth izni zaten verilmiş", Toast.LENGTH_SHORT).show();
        }
    }

    public void on(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            if (!adapter.isEnabled()) {
                Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnOn, 0);
                Toast.makeText(getApplicationContext(), "BLUETOOTH AÇILDI", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "BLUETOOTH AÇILDI", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Bluetooth açma izni verilmedi.", Toast.LENGTH_SHORT).show();
            checkAndRequestPermissions();
        }
    }

    public void off(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            if (adapter.isEnabled()) {
                adapter.disable();
                Toast.makeText(getApplicationContext(), "Bluetooth Kapatıldı", Toast.LENGTH_LONG);
            } else {
                Toast.makeText(this, "Bluetooth kapatma izni verilmedi", Toast.LENGTH_SHORT).show();
                checkAndRequestPermissions();
            }
        }
    }

    public void list(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            pairedDevices = adapter.getBondedDevices();
            ArrayList<String> list = new ArrayList<>();

            for (BluetoothDevice bt : pairedDevices) {
                list.add(bt.getName() + "\n" + bt.getAddress());
            }

            if (list.size() > 0) {
                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
                lv.setAdapter(adapter1);
            } else {
                Toast.makeText(getApplicationContext(), "Eşleştirilmiş cihaz yok", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Bluetooth listesini görüntülemek için izin verilmedi", Toast.LENGTH_SHORT).show();
            checkAndRequestPermissions();
        }
    }

    public void makeVisible(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED) {
            Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            startActivityForResult(getVisible, 0);
        } else {
            Toast.makeText(this, "Cihazı görünür yapmak için izin verilmedi", Toast.LENGTH_SHORT);
            checkAndRequestPermissions();
        }
    }

    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeBluetooth();
            } else {
                Toast.makeText(this, "İzin verilmedi", Toast.LENGTH_SHORT).show();
            }
        }
    }
}