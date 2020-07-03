package com.example.dell_pro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ReportActivity extends AppCompatActivity {
    private TextView temperature, oxysat, pulserate, Tdate, currTime, bGroup, mobno, gender, dob, pname;
    private FirebaseUser currUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        temperature = findViewById(R.id.temperature);
        oxysat = findViewById(R.id.oxysat);
        pulserate = findViewById(R.id.pulserate);
        Tdate = findViewById(R.id.Tdate);
        currTime = findViewById(R.id.currTime);
        bGroup = findViewById(R.id.bgroup);
        mobno = findViewById(R.id.mobno);
        gender = findViewById(R.id.gender);
        dob = findViewById(R.id.dob);
        pname = findViewById(R.id.pname);

        FirebaseAuth userAuth = FirebaseAuth.getInstance();
        currUser = userAuth.getCurrentUser();
        assert currUser != null;
        String uid = currUser.getUid();

        final String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        final String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());


        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pname.setText(currUser.getDisplayName());
                bGroup.setText(Objects.requireNonNull(dataSnapshot.child("bloodgrp").getValue()).toString());
                oxysat.setText(Objects.requireNonNull(dataSnapshot.child("pulse_oximeter").child("oxy_sat").getValue()).toString() + " %");
                pulserate.setText(Objects.requireNonNull(dataSnapshot.child("pulse_oximeter").child("pul_rate").getValue()).toString() + " Bpm");
                temperature.setText(Objects.requireNonNull(dataSnapshot.child("temp").getValue()).toString() + " *F");
                mobno.setText(Objects.requireNonNull(dataSnapshot.child("phno").getValue()).toString());
                gender.setText(Objects.requireNonNull(dataSnapshot.child("gender").getValue()).toString());
                Tdate.setText(currentDate);
                currTime.setText(currentTime);
                dob.setText(Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button ecgbutton = findViewById(R.id.ecgbutton);

        ecgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReportActivity.this, EcgGraph.class));
            }
        });

    }
}
