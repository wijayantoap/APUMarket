package com.application.wijayantoap.apupre_loved;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    private HomeFragment fragmentHome;
    private AddItemFragment fragmentAdd;
    private ProfileFragment fragmentProfile;


    public FirebaseRecyclerAdapter adapter;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //transaction.replace(R.id.content, new HomeFragment()).commit();
                    displayFragment(fragmentHome, fragmentAdd, fragmentProfile);
                    return true;
                case R.id.navigation_dashboard:
                    //transaction.replace(R.id.content, new AddItemFragment()).commit();
                    displayFragment(fragmentAdd, fragmentHome, fragmentProfile);
                    return true;
                case R.id.navigation_notifications:
                    //transaction.replace(R.id.content, new ProfileFragment()).commit();
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

        /*
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content, new HomeFragment(), "home").commit();
        fragmentManager.executePendingTransactions();
        */
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

}
