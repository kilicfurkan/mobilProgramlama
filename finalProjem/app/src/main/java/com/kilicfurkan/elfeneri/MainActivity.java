package com.kilicfurkan.elfeneri;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private CardView pusula;
    private SensorManager sensorler;
    private float aci = 0f;

    private float[] yercekimi = new float[3];
    private float[] manyetikAlan = new float[3];
    private boolean fenerAcik = false;
    private CameraManager kameraYonetici;
    private String kameraID;

    private ConstraintLayout anaEkran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button sos = (Button) findViewById(R.id.sosButton);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        anaEkran = findViewById(R.id.main);

        pusula = findViewById(R.id.pusulaCard);

        sensorler = (SensorManager) getSystemService(SENSOR_SERVICE);

        ImageButton fenerButonu = findViewById(R.id.fenerButon);

        kameraYonetici = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            kameraID = kameraYonetici.getCameraIdList()[0]; // Cihazın arka kamerasını al
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sosSinyali();
            }
        });

        fenerButonu.setOnClickListener(v -> {
            fenerAcik = !fenerAcik;

            if(fenerAcik)
            {
                fenerAc(true);
            }
            else
            {
                fenerAc(false);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

        if(sensorler != null) {
            sensorler.registerListener(this, sensorler.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
            sensorler.registerListener(this, sensorler.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorler.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            lowPassFilter(event.values.clone(), yercekimi);
        }
        else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            lowPassFilter(event.values.clone(), manyetikAlan);
        }

        if (yercekimi == null || manyetikAlan == null || yercekimi.length < 3 || manyetikAlan.length < 3) {
            return;
        }

        float[] rotasyon = new float[9];
        float[] egim = new float[9];

        boolean basariliMi = SensorManager.getRotationMatrix(rotasyon, egim, yercekimi, manyetikAlan);

        if(basariliMi){
            float[] yon = new float[3];
            SensorManager.getOrientation(rotasyon, yon);
            float aciRadyan = yon[0];
            float aciDerece = (float) Math.toDegrees(aciRadyan);
            aciDerece = (aciDerece + 360) % 360;

            RotateAnimation donusAnimasyonu = new RotateAnimation(
                    aci, -aciDerece,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            donusAnimasyonu.setDuration(500);
            donusAnimasyonu.setFillAfter(true);
            pusula.startAnimation(donusAnimasyonu);

            aci = -aciDerece;
        }
    }

    private void lowPassFilter(float[] input, float[] output) {
        float alpha = 0.1f; // 0 ile 1 arasında bir değer, küçük olursa daha yavaş tepki verir.
        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + alpha * (input[i] - output[i]);
        }
    }

    private void fenerAc(boolean acikMi) {
        try {
            if (kameraYonetici != null) {
                kameraYonetici.setTorchMode(kameraID, acikMi);
            }

            if(acikMi){
                anaEkran.setBackgroundColor(Color.WHITE);
            }
            else{
                anaEkran.setBackgroundColor(Color.BLACK);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void sosSinyali() {
        new Thread(() -> {
            try {
                for (int i = 0; i < 3; i++) {
                    fenerAc(true);
                    Thread.sleep(300);
                    fenerAc(false);
                    Thread.sleep(300);
                }

                for (int i = 0; i < 3; i++) {
                    fenerAc(true);
                    Thread.sleep(900);
                    fenerAc(false);
                    Thread.sleep(300);
                }

                for (int i = 0; i < 3; i++) {
                    fenerAc(true);
                    Thread.sleep(300);
                    fenerAc(false);
                    Thread.sleep(300);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }



}