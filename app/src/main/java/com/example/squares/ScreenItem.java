package com.example.squares;

import android.widget.ImageView;
import android.widget.TextView;


public abstract class ScreenItem {
    // for future development, may include some improvements

    protected ImageView image;

    protected ScreenItem(ImageView image) {
        this.image = image;
    }

    public abstract boolean onClick(TextView scoreText, ImageView nowTapImage); // if the game is over, returns false; else returns true
}
