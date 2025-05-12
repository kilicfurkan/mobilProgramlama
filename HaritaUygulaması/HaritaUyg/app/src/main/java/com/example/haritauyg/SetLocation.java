package com.example.haritauyg;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.GoogleMap;

public class SetLocation extends AppCompatActivity {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_set_location);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;

        LatLng fenerbahceStadium = new LatLng(40.987001,29.034972);
        mMap.addMarker(new MarkerOptions().position(fenerbahceStadium).title("Fenerbahçe Atatürk Stadyumu"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fenerbahceStadium, 15));
    }
}