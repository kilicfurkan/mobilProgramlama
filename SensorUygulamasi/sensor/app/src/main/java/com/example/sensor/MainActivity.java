package com.example.sensor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button btnAccelerometer = (Button) findViewById(R.id.accelerometer);
    Button btnCompass = (Button) findViewById(R.id.compass);
    Button btnGyroscope = (Button) findViewById(R.id.gyroscope);
    Button btnHumidity = (Button) findViewById(R.id.humidity);

    Button btnLight = (Button) findViewById(R.id.light);

    Button btnMagnometer = (Button) findViewById(R.id.magnometer);

    Button btnPressure = (Button) findViewById(R.id.pressure);

    Button btnProximity = (Button) findViewById(R.id.proximity);

    Button btnThermometer = (Button) findViewById(R.id.thermometer);

    public void sayfayaYonlendir(View view) {
        String butonAdi = ((Button) view).getText().toString();


        Intent intent;

        switch (butonAdi) {
            case "accelerometer":
                intent = new Intent(MainActivity.this, accelerometer.class);
                startActivity(intent);
                break;
            case "compass":
                intent = new Intent(MainActivity.this, compass.class);
                startActivity(intent);
                break;
            case "gyroscope":
                intent = new Intent(MainActivity.this, gyroscope.class);
                startActivity(intent);
                break;
            case "humidity":
                intent = new Intent(MainActivity.this, humidity.class);
                startActivity(intent);
                break;
            case "light":
                intent = new Intent(MainActivity.this, light.class);
                startActivity(intent);
                break;
            case "magnometer":
                intent = new Intent(MainActivity.this, magnometer.class);
                startActivity(intent);
                break;
            case "pressure":
                intent = new Intent(MainActivity.this, pressure.class);
                startActivity(intent);
                break;
            case "proximity":
                intent = new Intent(MainActivity.this, proximity.class);
                startActivity(intent);
                break;
            case "thermometer":
                intent = new Intent(MainActivity.this, thermometer.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}