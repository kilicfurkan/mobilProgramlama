package com.kilicfurkan.elfeneri;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    private ListView listeView;
    private ArrayList<String> kayitlarListesi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        listeView = findViewById(R.id.listeView);
        kayitlarListesi = new ArrayList<>();

        new Thread(() -> {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                String url = "jdbc:jtds:sqlserver://10.0.2.2:1433;databaseName=Pusula;user=furkandeneme;password=211207;";
                Connection baglanti = DriverManager.getConnection(url);
                Log.d("DB", "Bağlantı başarılı (listeleme)");

                String sorgu = "SELECT Enlem, Boylam, TarihSaat FROM Kayit ORDER BY TarihSaat DESC";
                Statement statement = baglanti.createStatement();
                ResultSet resultSet = statement.executeQuery(sorgu);

                while (resultSet.next()) {
                    String enlem = resultSet.getString("Enlem");
                    String boylam = resultSet.getString("Boylam");
                    String tarih = resultSet.getString("TarihSaat");

                    String satir = "Enlem: " + enlem + "\nBoylam: " + boylam + "\nTarih: " + tarih;
                    kayitlarListesi.add(satir);
                }

                baglanti.close();

                runOnUiThread(() -> {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, kayitlarListesi);
                    listeView.setAdapter(adapter);
                });

            } catch (Exception e) {
                Log.e("DBError", "Veritabanı hatası: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
}
