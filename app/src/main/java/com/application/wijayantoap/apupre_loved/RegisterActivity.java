package com.application.wijayantoap.apupre_loved;

import android.app.ProgressDialog;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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


        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mUsername = editEmail.getText().toString();
                if (mUsername.matches("")) {
                    editUsername.setText("");
                    editUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sad, 0,0,0);
                } else {
                    StringTokenizer tokens = new StringTokenizer(mUsername, "@");
                    String first = tokens.nextToken();// this will contain string before @
                    editUsername.setText(first.toUpperCase());
                    editUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_smile, 0,0,0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        cardViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(RegisterActivity.this);
                mDialog.setMessage("Loading like crazy");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Check if user already exist
                        if (dataSnapshot.child(editEmail.getText().toString()).exists()) {
                            mDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "User already exist", Toast.LENGTH_SHORT).show();
                        } else {
                            mDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

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
}
