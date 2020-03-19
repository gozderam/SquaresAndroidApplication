package com.example.squares;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TopLevelActivity extends AppCompatActivity {

    // Array of options
    private static final String[] options = new String[] {"Start", "How to play?", "Best scores", "Exit"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);

        hideSystemUI();

        ListView optionsList = findViewById(R.id.options_list);

        // Setting the adapter to the options_list View
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this, R.layout.options_list_layout, options);
        optionsList.setAdapter(listAdapter);

        // Creating and setting onItemClickListener to the options_list View
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(TopLevelActivity.this, GameActivity.class);
                    startActivity(intent);
                }
                else if(position == 1) {
                    Intent intent = new Intent(TopLevelActivity.this, HowToPlayActivity.class);
                    startActivity(intent);
                }
                else if (position == 2) {
                    Intent intent = new Intent(TopLevelActivity.this, BestScoresActivity.class);
                    startActivity(intent);
                }
                else if (position == 3) {
                    finish();
                    System.exit(0);
                }

            }
        };
        optionsList.setOnItemClickListener(itemClickListener);
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
                // Sticky Immersive mode
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Do not change the size of the content while showing and hiding nav and status bar
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the and nav bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        // Hide the and status bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
