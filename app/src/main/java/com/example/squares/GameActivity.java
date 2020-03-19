package com.example.squares;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    // this int array of image resource ids is used in changeScreenItem method
    private final static int[] squaresResourceIds = new int[]{
            R.drawable.pic_green_sq,
            R.drawable.pic_blue_sq,
            R.drawable.pic_yellow_sq,
            R.drawable.pic_red_sq
    };

    private final static int[] contentDescriptions = new int[] {
            R.string.pic_green_square,
            R.string.pic_blue_square,
            R.string.pic_yellow_square,
            R.string.pic_red_square
    };

    // TextView with user's score and NowTap
    private TextView scoreText;
    private static int yourScore;
    private ImageView nowTapImage;

    // "Game over" TextView and Button
    private TextView gameOverInfo;
    private Button gameOverButton;

    // Screen Size
    private int screenHeight;
    private int screenWidth;

    // ScreenItem objects (4 different types)
    private ScreenItem itemUp1;
    private ScreenItem itemUp2;
    private ScreenItem itemDown1;
    private ScreenItem itemDown2;
    private ScreenItem itemRight1;
    private ScreenItem itemRight2;
    private ScreenItem itemLeft1;
    private ScreenItem itemLeft2;

    // Positions of squares, used to set positions of ImageViews
    private FloatPoint picUp1Point = new FloatPoint(0.0f, 0.0f);
    private FloatPoint picUp2Point = new FloatPoint(0.0f, 0.0f);
    private FloatPoint picDown1Point = new FloatPoint(0.0f, 0.0f);
    private FloatPoint picDown2Point = new FloatPoint(0.0f, 0.0f);
    private FloatPoint picRight1Point = new FloatPoint(0.0f, 0.0f);
    private FloatPoint picRight2Point = new FloatPoint(0.0f, 0.0f);
    private FloatPoint picLeft1Point = new FloatPoint(0.0f, 0.0f);
    private FloatPoint picLeft2Point = new FloatPoint(0.0f, 0.0f);

    // Handlers
    private Handler handler1 = new Handler();
    private Handler handler2 = new Handler();
    private Handler handler3 = new Handler();
    private Handler handlerNowTapImage = new Handler();
    private Handler handlerSpeed = new Handler();

    // Runnable objects
    private Runnable runnable1;
    private Runnable runnable2;
    private Runnable runnable3;

    // Handlers timeToWaits and numbers of pixels that image move at the time
    private int timeToWait1 = 15;
    private int timeToWait2 = 14;
    private int timeToWait3 = 16;
    private float numberOfPixels1 = 5.0f;
    private float numberOfPixels2 = 6.0f;
    private float numberOfPixels3 = 4.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        hideSystemUI();

        // Initializing text views and NowTapImage, setting yourScore
        scoreText = findViewById(R.id.score_text);
        yourScore = 0;
        String string = "Your score: " + yourScore;
        scoreText.setText(string);
        nowTapImage = findViewById(R.id.now_tap_image);

        // Initializing "Game over" TextView and Button
        gameOverInfo = findViewById(R.id.game_over_info);
        gameOverButton = findViewById(R.id.game_over_button);

        // Initializing ScreenItem objects
        itemUp1 = new Square((ImageView) findViewById(R.id.pic_up1));
        itemUp2 = new Square((ImageView) findViewById(R.id.pic_up2));
        itemDown1 = new Square((ImageView) findViewById(R.id.pic_down1));
        itemDown2 = new Square((ImageView) findViewById(R.id.pic_down2));
        itemRight1 = new Square((ImageView) findViewById(R.id.pic_right1));
        itemRight2 = new Square((ImageView) findViewById(R.id.pic_right2));
        itemLeft1 = new Square((ImageView) findViewById(R.id.pic_left1));
        itemLeft2 = new Square((ImageView) findViewById(R.id.pic_left2));

        // Setting screenHeight and screenWidth
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);
        screenHeight = screenSize.y;
        screenWidth = screenSize.x;

        // Setting default positions for images (outside of the screen)
        itemUp1.image.setX(-50.0f);
        itemUp1.image.setY(-50.0f);
        itemUp2.image.setX(-50.0f);
        itemUp2.image.setY(-50.0f);
        itemDown1.image.setX(-50.0f);
        itemDown1.image.setY(screenHeight + 50.0f);
        itemDown2.image.setX(-50.0f);
        itemDown2.image.setY(screenHeight + 50.0f);
        itemRight1.image.setX(screenWidth + 50.0f);
        itemRight1.image.setY(-50.0f);
        itemRight2.image.setX(screenWidth + 50.0f);
        itemRight2.image.setY(-50.0f);
        itemLeft1.image.setX(-50.0f);
        itemLeft1.image.setY(screenHeight + 50.0f);
        itemLeft2.image.setX(-50.0f);
        itemLeft2.image.setY(screenHeight + 50.0f);

        // Moving
        handlerSpeed.post( new Runnable() {
            @Override
            public void run() {

                if(runnable1!=null) handler1.removeCallbacks(runnable1);
                if(numberOfPixels1 < 30.0f) numberOfPixels1+=2.0f;
                runnable1 = new Runnable() {
                    @Override
                    public void run() {
                        itemUp1 = moveUp(itemUp1, picUp1Point, numberOfPixels1);
                        itemDown1 = moveDown(itemDown1, picDown1Point, numberOfPixels1);
                        itemRight1 = moveRight(itemRight1, picRight1Point, numberOfPixels1);
                        itemLeft1 = moveLeft(itemLeft1, picLeft1Point, numberOfPixels1);
                        handler1.postDelayed(this, timeToWait1);
                    }
                };
                handler1.post(runnable1);

                if(runnable2!=null) handler2.removeCallbacks(runnable2);
                if(numberOfPixels2 < 30.0f) numberOfPixels2+=1.0f;
                runnable2 = new Runnable() {
                    @Override
                    public void run() {
                        itemRight2 = moveRight(itemRight2, picRight2Point, numberOfPixels2);
                        itemLeft2 = moveLeft(itemLeft2, picLeft2Point, numberOfPixels2);
                        handler2.postDelayed(this, timeToWait2);
                    }
                };
                handler2.post(runnable2);

                if(runnable3!=null) handler3.removeCallbacks(runnable3);
                if(numberOfPixels3 < 30.0f) numberOfPixels3+=3.0f;
                runnable3 = new Runnable() {
                    @Override
                    public void run() {
                        itemUp2 = moveUp(itemUp2, picUp2Point, numberOfPixels3);
                        itemDown2 = moveDown(itemDown2, picDown2Point, numberOfPixels3);
                        handler3.postDelayed(this, timeToWait3);
                    }
                };
                handler3.post(runnable3);
               handlerSpeed.postDelayed(this, 10000);
            }
        });

        // Changing NowTapImage
        handlerNowTapImage.post(new Runnable() {
            @Override
            public void run() {
                int index = (int) (Math.random() * squaresResourceIds.length);
                nowTapImage.setImageResource(squaresResourceIds[index]);
                nowTapImage.setContentDescription(getResources().getString(contentDescriptions[index]));
                handlerNowTapImage.postDelayed(this, 15000);
            }
        });
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

    // Getters and Setters
    public static int getYourScore() {
        return yourScore;
    }
    public static void setYourScore(int _yourScore) {
        yourScore = _yourScore;
    }

    // Moving methods
    private ScreenItem moveUp(ScreenItem screenItem, FloatPoint point, float numberOfPixels) {
        point.y -= numberOfPixels;
        ScreenItem newScreenItem = screenItem;
        if (screenItem.image.getY() + screenItem.image.getHeight() < 0) {
            point.x = (float) Math.floor((Math.random() * (screenWidth - screenItem.image.getWidth())) + screenItem.image.getWidth());
            point.y = screenHeight + 100.0f;
            newScreenItem = changeScreenItem(screenItem);
        }
        screenItem.image.setX(point.x);
        screenItem.image.setY(point.y);
        return newScreenItem;
    }

    private ScreenItem moveDown(ScreenItem screenItem, FloatPoint point, float numberOfPixels) {
        point.y += numberOfPixels;
        ScreenItem newScreenItem = screenItem;
        if (screenItem.image.getY() > screenHeight) {
            point.x = (float) Math.floor((Math.random() * (screenWidth - screenItem.image.getWidth())) + screenItem.image.getWidth());
            point.y = -100.0f;
            newScreenItem = changeScreenItem(screenItem);
        }
        screenItem.image.setX(point.x);
        screenItem.image.setY(point.y);
        return newScreenItem;
    }

    private ScreenItem moveRight(ScreenItem screenItem, FloatPoint point, float numberOfPixels) {
        point.x += numberOfPixels;
        ScreenItem newScreenItem = screenItem;
        if (screenItem.image.getX() > screenWidth + screenItem.image.getWidth()) {
            point.y = (float) Math.floor(Math.random() * (screenHeight - screenItem.image.getHeight()));
            point.x = -100.0f;
            newScreenItem = changeScreenItem(screenItem);
        }
        screenItem.image.setX(point.x);
        screenItem.image.setY(point.y);
        return newScreenItem;
    }

    private ScreenItem moveLeft(ScreenItem screenItem, FloatPoint point, float numberOfPixels) {
        point.x -= numberOfPixels;
        ScreenItem newScreenItem = screenItem;
        if (screenItem.image.getX() + screenItem.image.getWidth() < 0) {
            point.y = (float) Math.floor(Math.random() * (screenHeight - screenItem.image.getHeight()));
            point.x = screenWidth + 100.0f;
            newScreenItem = changeScreenItem(screenItem);
        }
        screenItem.image.setX(point.x);
        screenItem.image.setY(point.y);
        return newScreenItem;
    }

    // this method change the image of particular ScreenItem
    public ScreenItem changeScreenItem(ScreenItem toChange) {
        toChange.image.setVisibility(View.VISIBLE);
        int whichObject = (int)(Math.random()*3);
        ScreenItem newScreenItem;
        if(whichObject==0 || whichObject==1) {
            newScreenItem = new Square(toChange.image);
            int indexToChange = (int) (Math.random() * squaresResourceIds.length);
            newScreenItem.image.setImageResource(squaresResourceIds[indexToChange]);
            newScreenItem.image.setContentDescription(getResources().getString(contentDescriptions[indexToChange]));
        }
        else {
            newScreenItem = new BadSquare(toChange.image);
            newScreenItem.image.setImageResource(R.drawable.pic_bad_sq);
            newScreenItem.image.setContentDescription(getResources().getString(R.string.pic_bad_square));
        }
        return newScreenItem;
    }

    // OnClick methods
    public void onUp1Click (View view) {
        if(itemUp1.onClick(scoreText, nowTapImage)) {
            view.setVisibility(View.INVISIBLE);
        }
        else gameOver();
    }
    public void onUp2Click (View view) {
        if(itemUp2.onClick(scoreText, nowTapImage)) {
            view.setVisibility(View.INVISIBLE);
        }
        else gameOver();
    }
    ////////////////////////////////////////////////
    public void onDown1Click (View view) {
        if(itemDown1.onClick(scoreText, nowTapImage)) {
            view.setVisibility(View.INVISIBLE);
        }
        else gameOver();
    }
    public void onDown2Click (View view) {
        if(itemDown2.onClick(scoreText, nowTapImage)) {
            view.setVisibility(View.INVISIBLE);
        }
        else gameOver();
    }
    ////////////////////////////////////////////////
    public void onRight1Click (View view) {
        if(itemRight1.onClick(scoreText, nowTapImage)) {
            view.setVisibility(View.INVISIBLE);
        }
        else gameOver();
    }
    public void onRight2Click (View view) {
        if(itemRight2.onClick(scoreText, nowTapImage)) {
            view.setVisibility(View.INVISIBLE);
        }
        else gameOver();
    }
    ////////////////////////////////////////////////
    public void onLeft1Click (View view) {
        if(itemLeft1.onClick(scoreText, nowTapImage)) {
            view.setVisibility(View.INVISIBLE);
        }
        else gameOver();
    }
    public void onLeft2Click (View view) {
        if(itemLeft2.onClick(scoreText, nowTapImage)) {
            view.setVisibility(View.INVISIBLE);
        }
        else gameOver();
    }

    // Game Over
    public void gameOver() {
        gameOverInfo.setVisibility(View.VISIBLE);
        gameOverButton.setVisibility(View.VISIBLE);
        handler1.removeCallbacksAndMessages(null);
        handler2.removeCallbacksAndMessages(null);
        handler3.removeCallbacksAndMessages(null);
        handlerNowTapImage.removeCallbacksAndMessages(null);
        handlerSpeed.removeCallbacksAndMessages(null);
        itemUp1.image.setEnabled(false);
        itemUp2.image.setEnabled(false);
        itemDown1.image.setEnabled(false);
        itemDown2.image.setEnabled(false);
        itemRight1.image.setEnabled(false);
        itemRight2.image.setEnabled(false);
        itemLeft1.image.setEnabled(false);
        itemLeft2.image.setEnabled(false);
    }
    public void onGameOverButtonClick (View view) {
        try {
            SQLiteOpenHelper squaresDatabaseHelper = new SquaresDatabaseHelper(this);
            SQLiteDatabase database = squaresDatabaseHelper.getReadableDatabase();
            Cursor cursor = database.query("BEST_SCORES", new String[] {"_id", "POINTS"},
                    null, null, null, null, "POINTS ASC");
            if(cursor.moveToFirst() && yourScore > cursor.getInt(1)) {
                int idOfSmallest = cursor.getInt(0);
                cursor.close();
                database.close();
                Intent intent = new Intent(GameActivity.this, UpdateBestScoresActivity.class);
                intent.putExtra(UpdateBestScoresActivity.ID_OF_SMALLEST, idOfSmallest);
                startActivity(intent);
            }
            else {
                cursor.close();
                database.close();
            }
            finish();
        }
        catch (SQLiteException e){
            Toast.makeText(this, "Database is inaccessible", Toast.LENGTH_SHORT).show();
        }
    }
}

