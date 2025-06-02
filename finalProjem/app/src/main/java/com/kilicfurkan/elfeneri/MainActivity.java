package com.kilicfurkan.elfeneri;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.Manifest;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private CardView pusula;
    private SensorManager sensorYoneticisi;
    private float aci = 0f;
    private float[] yercekimi = new float[3], manyetikAlan = new float[3];
    private boolean fenerAcik = false;
    private CameraManager kameraYonetici;
    private String kameraID;
    private ConstraintLayout anaEkran;

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

        anaEkran = findViewById(R.id.main);
        pusula = findViewById(R.id.pusulaCard);
        ImageButton fenerButonu = findViewById(R.id.fenerButon);
        Button sosButonu = findViewById(R.id.sosButton);
        Button gecis = findViewById(R.id.sayfa2);
        sensorYoneticisi = (SensorManager) getSystemService(SENSOR_SERVICE);
        kameraYonetici = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            kameraID = kameraYonetici.getCameraIdList()[0];
        } catch (CameraAccessException e){
            e.printStackTrace();
        }

        gecis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
        sosButonu.setOnClickListener(new View.OnClickListener(){
            int kisaFlashSuresi = 300;
            int uzunFlashSuresi = 900;
            int beklemeSuresi = 300;
            int tekrarSayisi = 3;
            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    try {
                        sosSinyali(kisaFlashSuresi, tekrarSayisi, beklemeSuresi);
                        sosSinyali(uzunFlashSuresi, tekrarSayisi, beklemeSuresi);
                        sosSinyali(kisaFlashSuresi, tekrarSayisi, beklemeSuresi);
                        anaEkran.setBackgroundColor(Color.BLACK);

                        aramaEkraniAc();
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();

                bilgileriKaydet();
            }
        });

        fenerButonu.setOnClickListener(v -> {
            fenerAcik = !fenerAcik;
            fenerAc(fenerAcik ? true : false, false);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (sensorYoneticisi != null) {
            sensorYoneticisi.registerListener(this, sensorYoneticisi.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
            sensorYoneticisi.registerListener(this, sensorYoneticisi.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorYoneticisi.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            alcakGecirgenFiltre(event.values.clone(), yercekimi);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            alcakGecirgenFiltre(event.values.clone(), manyetikAlan);
        }

        if (yercekimi == null || manyetikAlan == null || yercekimi.length < 3 || manyetikAlan.length < 3) {
            return;
        }

        float[] rotasyon = new float[9];
        float[] egim = new float[9];

        boolean basariliMi = SensorManager.getRotationMatrix(rotasyon, egim, yercekimi, manyetikAlan);

        if (basariliMi) {
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

    private void alcakGecirgenFiltre(float[] girisDegerleri, float[] cikisDegerleri) {
        float agirlik = 0.1f;
        for (int i = 0; i < girisDegerleri.length; i++) {
            cikisDegerleri[i] = cikisDegerleri[i] + agirlik * (girisDegerleri[i] - cikisDegerleri[i]);
        }
    }


    private void fenerAc(boolean acikMi, boolean sos) {
        try {
            if (kameraYonetici != null) {
                kameraYonetici.setTorchMode(kameraID, acikMi);
            }

            if (acikMi) {
                anaEkran.setBackgroundColor(sos ? Color.RED : Color.WHITE);
            } else if (!acikMi) {
                anaEkran.setBackgroundColor(sos ? Color.BLUE : Color.BLACK);
            }

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void bilgileriKaydet() {
        FusedLocationProviderClient konumSaglayici = LocationServices.getFusedLocationProviderClient(this);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        konumSaglayici.getLastLocation().addOnSuccessListener(this, konum -> {
           if(konum != null){
               String enlem = Double.toString(konum.getLatitude());
               String boylam = Double.toString(konum.getLongitude());
               String tarih = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

               new Thread(() -> {
                  try {
                      Class.forName("net.,sourceforge.jtds.jdbc.Driver");

                      String url = "jdbc:jtds:sqlserver://10.0.2.2:1433;databaseName=Pusula;user=furkandeneme;password=211207;";
                      Connection baglanti = DriverManager.getConnection(url);
                      Log.d("DB", "Bağlantı başarılı");
                      String sorgu =  "INSERT INTO Kayit (Enlem, Boylam, TarihSaat) VALUES (?, ?, ?)";
                      PreparedStatement komut = baglanti.prepareStatement(sorgu);
                      komut.setString(1, enlem);
                      komut.setString(2, boylam);
                      komut.setString(3, tarih);

                      baglanti.setAutoCommit(true);
                      komut.executeUpdate();
                      baglanti.close();
                  } catch (Exception e) {
                      Log.e("DBError", "Veritabanı hatası: " + e.getMessage());
                      e.printStackTrace();
                  }
               }).start();
           }
        });
    }

    private void sosSinyali(int acikSure, int tekrar, int beklemeSuresi) throws InterruptedException{
        for(int i = 0; i < tekrar; i++){
            fenerAc(true, true);
            Thread.sleep(acikSure);

            fenerAc(false, true);
            Thread.sleep(beklemeSuresi);
        }
    }

    private void aramaEkraniAc() {
        Intent acilArama = new Intent(Intent.ACTION_DIAL);
        acilArama.setData(Uri.parse("tel:112"));
        startActivity(acilArama);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}