package com.example.squares;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Square extends ScreenItem {

    private static int points = 1;

    public Square(ImageView image) {
       super(image);
    }

    public boolean onClick( TextView scoreText, ImageView nowTapImage) {
        if(image.getContentDescription() == nowTapImage.getContentDescription()) {
            GameActivity.setYourScore(GameActivity.getYourScore() + points);
            String string = "Your score: " + GameActivity.getYourScore();
            scoreText.setText(string);
            return true;
        }
        else return false;
    }
}
