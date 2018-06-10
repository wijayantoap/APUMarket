package com.application.wijayantoap.apupre_loved;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.application.wijayantoap.apupre_loved.Model.Activity;
import com.application.wijayantoap.apupre_loved.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

import me.drakeet.materialdialog.MaterialDialog;

public class RegisterActivity extends AppCompatActivity {
    TextView textTnC;
    MaterialDialog mMaterialDialog;
    EditText editEmail, editUsername, editPassword, editPasswordConfirmation;
    CardView cardViewRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        textTnC = findViewById(R.id.textRegister);
        editEmail = findViewById(R.id.editEmail);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        editPasswordConfirmation = findViewById(R.id.editPasswordConfirmation);
        cardViewRegister = findViewById(R.id.cardViewRegister);

        DateFormat df = new SimpleDateFormat("dd MMM yyyy");
        final String date = df.format(Calendar.getInstance().getTime());


        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mUsername = editEmail.getText().toString();
                if (mUsername.matches("")) {
                    editUsername.setText("");
                    editUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sad, 0, 0, 0);
                } else {
                    StringTokenizer tokens = new StringTokenizer(mUsername, "@");
                    String first = tokens.nextToken();// this will contain string before @
                    editUsername.setText(first.toUpperCase());
                    editUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_smile, 0, 0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        final DatabaseReference table_activity = database.getReference("Activity");

        cardViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check whether the data is empty or not
                final String validateEmail = editEmail.getText().toString();
                final String validateUsername = editUsername.getText().toString();
                final String validatePassword = editPassword.getText().toString();
                String validatePasswordConfirmation = editPasswordConfirmation.getText().toString();
                int length = editPassword.getText().length();

                if (validateEmail.matches("") || validateUsername.matches("") || validatePassword.matches("")
                        || validatePasswordConfirmation.matches("")) {
                    Toast.makeText(RegisterActivity.this, "Everything must be filled", Toast.LENGTH_SHORT).show();
                } else {
                    if (length < 5) {
                        Toast.makeText(RegisterActivity.this, "Password must be more than 5 characters", Toast.LENGTH_SHORT).show();
                    } else {

                        if (!validatePasswordConfirmation.equals(validatePassword)) {
                            Toast.makeText(RegisterActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                        } else {
                            if (validateEmail.contains("@mail.apu.edu.my") || validateEmail.contains("@mail.apiit.edu.my")) {
                                final ProgressDialog mDialog = new ProgressDialog(RegisterActivity.this);
                                mDialog.setMessage("Loading.. Please wait");
                                mDialog.show();
                                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        // Check if user already exist
                                        if (dataSnapshot.child(validateUsername).exists()) {
                                            mDialog.dismiss();
                                            Toast.makeText(RegisterActivity.this, "User already exist", Toast.LENGTH_SHORT).show();
                                        } else {
                                            mDialog.dismiss();
                                            User user = new User(validateEmail, validatePassword, 0, "active", 0);
                                            table_user.child(validateUsername).setValue(user);
                                            Toast.makeText(RegisterActivity.this, "Sign up successfully", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(RegisterActivity.this, MainActivity.class);

                                            Activity activity = new Activity(validateUsername, "Registered to APU Market", date);
                                            table_activity.push().setValue(activity);
                                            saveInfo();
                                            startActivity(i);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            } else {
                                Toast.makeText(RegisterActivity.this, "Must register with APU email address", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                mMaterialDialog = new MaterialDialog(RegisterActivity.this)
                        .setTitle("Terms and Conditions")
                        .setMessage(getString(R.string.termsandconditions))
                        .setPositiveButton("AGREE", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();
                            }
                        });

                textTnC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.show();
                    }
                });
            }
        });
    }

    public void saveInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("username", editUsername.getText().toString());
        editor.putString("password", editPassword.getText().toString());
        editor.apply();
    }
}
