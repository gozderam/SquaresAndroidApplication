package com.example.squares;

import android.widget.ImageView;
import android.widget.TextView;

public class BadSquare extends ScreenItem {

    private static int points = -30;

    public BadSquare(ImageView image) {
        super(image);
    }

    public boolean onClick(TextView scoreText, ImageView nowTapImage) {
        GameActivity.setYourScore(GameActivity.getYourScore() + points);
        String string = "Your score: " + GameActivity.getYourScore();
        scoreText.setText(string);
        return true;
    }
}
