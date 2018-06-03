package com.application.wijayantoap.apupre_loved;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

public class MainActivity extends AppCompatActivity {

    private HomeFragment fragmentHome;
    private AddItemFragment fragmentAdd;
    private ProfileFragment fragmentProfile;

    boolean doubleBackToExitPressedOnce = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    displayFragment(fragmentHome, fragmentAdd, fragmentProfile);
                    return true;
                case R.id.navigation_dashboard:
                    displayFragment(fragmentAdd, fragmentHome, fragmentProfile);
                    return true;
                case R.id.navigation_notifications:
                    displayFragment(fragmentProfile, fragmentHome, fragmentAdd);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            fragmentHome = HomeFragment.newInstance("foo", "foo1");
            fragmentAdd = AddItemFragment.newInstance("bar", "bar1");
            fragmentProfile = ProfileFragment.newInstance("baz", "baz2");
        }

        displayFragment(fragmentHome, fragmentAdd, fragmentProfile);
    }

    protected void displayFragment(Fragment show, Fragment hide1, Fragment hide2) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (show.isAdded()) {
            ft.show(show);
        } else {
            ft.add(R.id.content, show, "home");
        }
        if (hide1.isAdded()) {

            ft.hide(hide1);
        }
        if (hide2.isAdded()) {

            ft.hide(hide2);
        }
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
