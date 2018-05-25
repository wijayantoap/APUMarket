package com.application.wijayantoap.apupre_loved;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.application.wijayantoap.apupre_loved.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    TextView txtRegister;
    CardView buttonLogin;
    EditText editUsername, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        txtRegister = findViewById(R.id.textRegister);
        buttonLogin = findViewById(R.id.cardViewLogin);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);

        // init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usernameValidate = editUsername.getText().toString();
                String passwordValidate = editPassword.getText().toString();
                if (usernameValidate.matches("") || passwordValidate.matches("")) {
                    Toast.makeText(LoginActivity.this, "Username and Password must not be empty", Toast.LENGTH_SHORT).show();
                } else {

                    if (usernameValidate.matches("admin") && passwordValidate.matches("admin")) {
                        Intent i = new Intent(LoginActivity.this, AdminActivity.class);
                        startActivity(i);
                    } else {

                        final ProgressDialog mDialog = new ProgressDialog(LoginActivity.this);
                        mDialog.setMessage("Please wait");
                        mDialog.show();

                        table_user.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Check if user does not exist in database
                                if (dataSnapshot.child(editUsername.getText().toString()).exists()) {
                                    // Get User information
                                    mDialog.dismiss();
                                    User user = dataSnapshot.child(editUsername.getText().toString()).getValue(User.class);
                                    if (user.getPassword().equals(editPassword.getText().toString())) {
                                        Toast.makeText(LoginActivity.this, "Log in success!", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Log in failed", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    mDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                View sharedView = txtRegister;
                String transitionName = getString(R.string.shared_transaction);
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, sharedView, transitionName);
                startActivity(i, transitionActivityOptions.toBundle());
                //startActivity(i);
            }
        });
    }


}
