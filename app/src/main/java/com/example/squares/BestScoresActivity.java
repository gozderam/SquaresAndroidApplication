package com.example.squares;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;

public class BestScoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_scores);

        hideSystemUI();

        TextView[][] txtArray = {{findViewById(R.id.txt_0_0), findViewById(R.id.txt_0_1), findViewById(R.id.txt_0_2)},
                {findViewById(R.id.txt_1_0), findViewById(R.id.txt_1_1), findViewById(R.id.txt_1_2)},
                {findViewById(R.id.txt_2_0), findViewById(R.id.txt_2_1), findViewById(R.id.txt_2_2)},
                {findViewById(R.id.txt_3_0), findViewById(R.id.txt_3_1), findViewById(R.id.txt_3_2)},
                {findViewById(R.id.txt_4_0), findViewById(R.id.txt_4_1), findViewById(R.id.txt_4_2)},
                {findViewById(R.id.txt_5_0), findViewById(R.id.txt_5_1), findViewById(R.id.txt_5_2)}};
        try {
            SQLiteOpenHelper squaresDatabaseHelper = new SquaresDatabaseHelper(this);
            SQLiteDatabase database = squaresDatabaseHelper.getReadableDatabase();
            Cursor cursor = database.query("BEST_SCORES", new String[] {"NAME", "POINTS"},
                    null, null, null, null, "POINTS DESC", null);

            if(cursor.moveToFirst()) {
                String number = "1.";
                txtArray[1][0].setText(number);
                txtArray[1][1].setText(cursor.getString(0));
                txtArray[1][2].setText(String.valueOf(cursor.getInt(1)));
            }

            for(int i = 2; i<=SquaresDatabaseHelper.RECORDS; ++i) {
                if(cursor.moveToNext()) {
                    String number = i + ".";
                    txtArray[i][0].setText(number);
                    txtArray[i][1].setText(cursor.getString(0));
                    txtArray[i][2].setText(String.valueOf(cursor.getInt(1)));
                }
            }

            cursor.close();
            database.close();
        }
        catch (SQLiteException e) {
            txtArray[1][0].setText("- - -");
        }
    }

    public void onOkClick(View view) {
        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
