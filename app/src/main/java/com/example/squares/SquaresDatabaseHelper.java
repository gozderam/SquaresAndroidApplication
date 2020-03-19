package com.example.squares;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SquaresDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "squares";
    private static final int DB_VERSION = 1;
    public static final int RECORDS = 5;

    SquaresDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateDatabase(db, oldVersion, newVersion);
    }

    private void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < 1) {
            db.execSQL("CREATE TABLE BEST_SCORES (_id INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, POINTS INTEGER);");
            for (int i =0; i<RECORDS; ++i) {
                ContentValues values = new ContentValues();
                values.put("NAME", " - - - ");
                values.put("POINTS", 0);
                db.insert("BEST_SCORES", null, values);
            }
        }
    }
}
