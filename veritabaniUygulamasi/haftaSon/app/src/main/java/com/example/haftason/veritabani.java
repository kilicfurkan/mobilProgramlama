package com.example.haftason;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class veritabani extends SQLiteOpenHelper {
    private static final String VERITABANI_ADI = "Ogrenciler.db";
    private static final int SURUM = 1;

    public veritabani(Context c) {
        super(c, VERITABANI_ADI, null, SURUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String sql = "CREATE TABLE IF NOT EXISTS OgrenciBilgi(" +
                "ad TEXT NOT NULL, " +
                "soyad TEXT NOT NULL, " +
                "yas INTEGER NOT NULL, " +
                "sehir TEXT NOT NULL)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int eskiSurum, int yeniSurum) {
        db.execSQL("DROP TABLE IF EXISTS OgrenciBilgi");
        onCreate(db);
    }
}
