package com.example.haftason;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    Button Kaydet, Goster, Sil, Guncelle;
    EditText ad, soyad, yas, sehir;
    TextView Bilgiler;
    private veritabani v1;

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

        v1 = new veritabani(this);
        Kaydet = findViewById(R.id.buttonEkle);
        Goster = findViewById(R.id.buttonGoster);
        Sil = findViewById(R.id.buttonSil);
        Guncelle = findViewById(R.id.buttonGuncelle);
        ad = findViewById(R.id.ad);
        soyad = findViewById(R.id.soyad);
        yas = findViewById(R.id.yas);
        sehir = findViewById(R.id.sehir);
        Bilgiler = findViewById(R.id.textView);

        Kaydet.setOnClickListener(view -> {
            Log.d(TAG,"Kayıt ekle butonuna basıldı");
            KayitEkle(ad.getText().toString(), soyad.getText().toString(), yas.getText().toString(), sehir.getText().toString());
        });

        Goster.setOnClickListener(view -> {
            Log.d(TAG, "Kayıt sil butonuna basıldı");
            KayıtGoster(KayitGetir());
        });

        Sil.setOnClickListener(view -> {
            Log.d(TAG, "Kayıt sil butonuna basıldı");
            KayitSil(ad.getText().toString());
        });

        Guncelle.setOnClickListener(view -> {
            Log.d(TAG, "Kayıt güncelle butonuna basıldı");
            KayıtGuncelle(ad.getText().toString(), soyad.getText().toString(), yas.getText().toString(), sehir.getText().toString());
        });
    }

    private  String[] sutunlar = {"ad", "soyad", "yas", "sehir"};

    private Cursor KayitGetir() {
        SQLiteDatabase db = v1.getWritableDatabase();
        return db.query("OgrenciBilgi", sutunlar, null, null, null, null, null);
    }

    private void KayıtGoster(Cursor goster) {
        StringBuilder builder = new StringBuilder();

        try {
            while (goster.moveToNext()) {
                int ColumnIndexAd = goster.getColumnIndexOrThrow("ad");
                int ColumnIndexSoyad = goster.getColumnIndexOrThrow("soyad");
                int ColumnIndexYas = goster.getColumnIndexOrThrow("yas");
                int ColumnIndexSehir = goster.getColumnIndexOrThrow("sehir");

                String add = goster.getString(ColumnIndexAd);
                String soyadd = goster.getString(ColumnIndexSoyad);
                String yass = goster.getString(ColumnIndexYas);
                String sehirr = goster.getString(ColumnIndexSehir);
                builder.append("Ad: ").append(add).append("\n");
                builder.append("Soyad: ").append(soyadd).append("\n");
                builder.append("Yas: ").append(yass).append("\n");
                builder.append("Sehir: ").append(sehirr).append("\n");
                builder.append("-----------------------").append("\n");
            }
            Bilgiler.setText(builder.toString());
        } catch (Exception e) {
            Log.e(TAG, "Kayıt gösterilirken hata oluştu: " + e.getMessage());
        } finally {
            goster.close();
        }
    }

    private void KayitEkle(String adi, String soyadi, String yasi, String sehri) {
        try {
            SQLiteDatabase db = v1.getWritableDatabase();
            ContentValues veriler = new ContentValues();
            veriler.put("ad", adi);
            veriler.put("soyad", soyadi);
            veriler.put("yas", Integer.parseInt(yasi));
            veriler.put("sehir", sehri);
            db.insertOrThrow("OgrenciBilgi", null, veriler);
            db.close();
            Log.d(TAG, "Kayıt başarıyla eklendi");
        } catch (Exception e) {
            Log.e(TAG, "Kayıt eklenirken hata oluştu: " + e.getMessage());
        }
    }

    private void KayitSil(String adi){
        try {
            SQLiteDatabase db = v1.getWritableDatabase();
            int rows = db.delete("OgrenciBilgi", "ad=?", new String[]{adi});
            db.close();
            Log.d(TAG, rows + "kayıt silindi");
        } catch (Exception e) {
            Log.e(TAG, "Kayıt silinirken hata oluştu: " + e.getMessage());
        }
    }

    private void KayıtGuncelle(String adi, String soyadi, String yasi, String sehri) {
        try {
            SQLiteDatabase db = v1.getWritableDatabase();
            ContentValues cvGuncelle = new ContentValues();
            cvGuncelle.put("soyad", soyadi);
            cvGuncelle.put("yas", Integer.parseInt(yasi));
            cvGuncelle.put("sehir", sehri);
            int rows = db.update("OgrenciBilgi", cvGuncelle, "ad=?", new String[]{adi});
            db.close();
            Log.d(TAG, rows + "kayıt güncellendi");
        } catch (Exception e) {
            Log.e(TAG, "Kayıt güncellenirken hata oluştu" + e.getMessage());
        }
    }
}