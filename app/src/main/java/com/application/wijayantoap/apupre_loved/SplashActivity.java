package com.application.wijayantoap.apupre_loved;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    private ImageView imgLogo;
    private TextView txtShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Declaring interface component to a variable
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        txtShown = (TextView) findViewById(R.id.txtShown);
        //Set the animation for the ImageView
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.transition);
        imgLogo.startAnimation(anim);
        txtShown.startAnimation(anim);

        //Set timer for the animation to be executed
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally{
                    final Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }
            }
        };
        timer.start();
    }
}
