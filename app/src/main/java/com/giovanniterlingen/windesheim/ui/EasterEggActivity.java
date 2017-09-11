package com.giovanniterlingen.windesheim.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.giovanniterlingen.windesheim.R;

/**
 * A schedule app for students and teachers of Windesheim
 *
 * @author Giovanni Terlingen
 */
public class EasterEggActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View easterEggView = new EasterEggView(this);
        easterEggView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        setContentView(easterEggView);
    }
}
