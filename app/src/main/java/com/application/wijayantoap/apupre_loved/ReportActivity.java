package com.application.wijayantoap.apupre_loved;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.application.wijayantoap.apupre_loved.Model.Report;
import com.application.wijayantoap.apupre_loved.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {

    String emailExtra;
    TextInputEditText textTitle, textEmail, textReport;
    ImageView imgBack, imgSubmit;

    FirebaseDatabase database;
    DatabaseReference table_report;

    String monthName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // init Firebase
        database = FirebaseDatabase.getInstance();
        table_report = database.getReference("Report");

        DateFormat df = new SimpleDateFormat("dd MMM yyyy");
        final String date = df.format(Calendar.getInstance().getTime());

        Toast.makeText(this, "" + date, Toast.LENGTH_SHORT).show();

        textTitle = findViewById(R.id.editTitle);
        textEmail = findViewById(R.id.editEmail);
        textReport = findViewById(R.id.editMessage);
        imgBack = findViewById(R.id.imgBack);
        imgSubmit = findViewById(R.id.imgSubmit);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailVerif = textEmail.getText().toString();
                final String titleVerif = textTitle.getText().toString();
                final String messageVerif = textReport.getText().toString();
                if (emailVerif.matches("") || titleVerif.matches("") || messageVerif.matches("")) {
                    Toast.makeText(ReportActivity.this, "Please fill all the form", Toast.LENGTH_SHORT).show();
                } else {
                    final ProgressDialog mDialog = new ProgressDialog(ReportActivity.this);
                    mDialog.setMessage("Loading.. Please wait");
                    mDialog.show();
                    table_report.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mDialog.dismiss();
                            Report report = new Report(titleVerif, emailVerif, messageVerif, date);
                            table_report.push().setValue(report);
                            Toast.makeText(ReportActivity.this, "Submitted", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    Toast.makeText(ReportActivity.this, "Submitted", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.report_menu, menu);
        return true;
    }
}
