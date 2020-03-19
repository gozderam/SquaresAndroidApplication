package com.example.squares;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateBestScoresActivity extends AppCompatActivity {

    public static final String ID_OF_SMALLEST = "idOfTheSmallestScore";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_best_scores);
        hideSystemUI();
        String inscription = "You got " + GameActivity.getYourScore() +
                " points and hence you're among 5 top gamers! Enter your name to save your score on Best Scores List.";
        TextView youWonText = findViewById(R.id.you_won_text);
        youWonText.setText(inscription);
        EditText enterYourName = findViewById(R.id.enter_your_name);
        enterYourName.setImeOptions(EditorInfo.IME_ACTION_DONE); // makes the done button on the keyboard
    }

    public void onUpdateButtonClick(View view) {
        Intent intent = getIntent();
        int idOfSmallest = intent.getIntExtra(ID_OF_SMALLEST, 0);
        try {
            EditText enterYourName = findViewById(R.id.enter_your_name);
            SQLiteOpenHelper squaresDatabaseHelper = new SquaresDatabaseHelper(this);
            SQLiteDatabase database = squaresDatabaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("NAME", String.valueOf(enterYourName.getText()));
            values.put("POINTS", GameActivity.getYourScore());
            database.update("BEST_SCORES", values, "_id = ?",  new String[] {Integer.toString(idOfSmallest)});
            database.close();
        }
        catch (SQLiteException e) {
            Toast.makeText(this, "Database is inaccessible", Toast.LENGTH_SHORT).show();
        }
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
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

}
