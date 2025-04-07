package com.example.plakauygulamas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    String[][] sehirlerVePlakalar = {
            {"Adana", "1"},
            {"Adıyaman", "2"},
            {"Afyonkarahisar", "3"},
            {"Ağrı", "4"},
            {"Amasya", "5"},
            {"Ankara", "6"},
            {"Antalya", "7"},
            {"Artvin", "8"},
            {"Aydın", "9"},
            {"Balıkesir", "10"},
            {"Bilecik", "11"},
            {"Bingöl", "12"},
            {"Bitlis", "13"},
            {"Bolu", "14"},
            {"Burdur", "15"},
            {"Bursa", "16"},
            {"Çanakkale", "17"},
            {"Çankırı", "18"},
            {"Çorum", "19"},
            {"Denizli", "20"},
            {"Diyarbakır", "21"},
            {"Edirne", "22"},
            {"Elazığ", "23"},
            {"Erzincan", "24"},
            {"Erzurum", "25"},
            {"Eskişehir", "26"},
            {"Gaziantep", "27"},
            {"Giresun", "28"},
            {"Gümüşhane", "29"},
            {"Hakkari", "30"},
            {"Hatay", "31"},
            {"Iğdır", "32"},
            {"Isparta", "33"},
            {"Istanbul", "34"},
            {"İzmir", "35"},
            {"Kars", "36"},
            {"Kastamonu", "37"},
            {"Kayseri", "38"},
            {"Kırıkkale", "39"},
            {"Kırklareli", "40"},
            {"Kırşehir", "41"},
            {"Kocaeli", "41"},
            {"Konya", "42"},
            {"Kütahya", "43"},
            {"Malatya", "44"},
            {"Manisa", "45"},
            {"Kahramanmaraş", "46"},
            {"Mardin", "47"},
            {"Muğla", "48"},
            {"Muş", "49"},
            {"Nevşehir", "50"},
            {"Niğde", "51"},
            {"Ordu", "52"},
            {"Rize", "53"},
            {"Sakarya", "54"},
            {"Samsun", "55"},
            {"Siirt", "56"},
            {"Sinop", "57"},
            {"Sivas", "58"},
            {"Tekirdağ", "59"},
            {"Tokat", "60"},
            {"Trabzon", "61"},
            {"Tunceli", "62"},
            {"Şanlıurfa", "63"},
            {"Uşak", "64"},
            {"Van", "65"},
            {"Yozgat", "66"},
            {"Zonguldak", "67"},
            {"Aksaray", "68"},
            {"Bayburt", "69"},
            {"Karaman", "70"},
            {"Kırıkkale", "71"},
            {"Batman", "72"},
            {"Şırnak", "73"},
            {"Bartın", "74"},
            {"Ardahan", "75"},
            {"Iğdır", "76"},
            {"Yalova", "77"},
            {"Karabük", "78"},
            {"Kilis", "79"},
            {"Osmaniye", "80"},
            {"Düzce", "81"}
    };

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

        Intent intent = new Intent(MainActivity.this, ikinciActivity.class);
        Button btnbasla = (Button) findViewById(R.id.basla);
        Button btnonayla = (Button) findViewById(R.id.onayla);
        TextView txt = (TextView) findViewById(R.id.textView);
        SeekBar sbsehir = (SeekBar) findViewById(R.id.seekBar2);
        EditText ett= (EditText) findViewById(R.id.editTextText);

        sbsehir.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = progress + 1;
                txt.setText(String.valueOf(value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnonayla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                int secilenSehir = sbsehir.getProgress() + 1;

                if(sehirlerVePlakalar[secilenSehir][1] == ett.getText().toString())
                {
                    startActivity(intent);
                }
            }
        });

    }
}