package com.application.wijayantoap.apupre_loved;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {
    private ImageView imgLogo, firebaseLogo;
    private TextView txtShown, txtVersion;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Declaring interface component to a variable
        imgLogo = findViewById(R.id.imgLogo);
        firebaseLogo = findViewById(R.id.firebaseLogo);
        txtShown = findViewById(R.id.txtShown);
        txtVersion = findViewById(R.id.textVersion);
        //Set the animation for the ImageView
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.transition);
        imgLogo.startAnimation(anim);
        firebaseLogo.startAnimation(anim);
        txtShown.startAnimation(anim);
        txtVersion.startAnimation(anim);

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        if (username != null && !username.matches("")) {
            Toast.makeText(this, "Logged in as " + username, Toast.LENGTH_SHORT).show();
        }
        //Set timer for the animation to be executed
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    loadUser();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }
            }
        };
        timer.start();
    }

    public void loadUser() {
        if (username.matches("") || username == null) {
            final Intent intentLogin = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intentLogin);
        } else if (username != null && !username.matches("") && !username.matches("admin")){
            final Intent intentMain = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intentMain);
        } else if (username.matches("admin")) {
            final Intent intentAdmin = new Intent(SplashActivity.this, AdminActivity.class);
            startActivity(intentAdmin);
        }
    }
}
