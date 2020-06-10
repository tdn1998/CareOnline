package com.example.dell_pro;

import android.os.Bundle;
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

public class ReportActivity extends AppCompatActivity {
    private TextView temperature, oxysat, pulserate, Tdate, currTime, bGroup, mobno, gender, dob, pname;
    private FirebaseUser currUser;
    private FirebaseAuth userAuth;
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

        userAuth = FirebaseAuth.getInstance();
        currUser = userAuth.getCurrentUser();
        String uid = currUser.getUid();

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());


        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pname.setText(currUser.getDisplayName());
                bGroup.setText(dataSnapshot.child("Blood Group").getValue().toString());
                oxysat.setText(dataSnapshot.child("pulse_oximeter").child("oxy_sat").getValue().toString() + " %");
                pulserate.setText(dataSnapshot.child("pulse_oximeter").child("pul_rate").getValue().toString() + " Bpm");
                temperature.setText(dataSnapshot.child("temp").getValue().toString() + " *F");
                mobno.setText(dataSnapshot.child("Phone Number").getValue().toString());
                gender.setText(dataSnapshot.child("Gender").getValue().toString());
                Tdate.setText(currentDate.toString());
                currTime.setText(currentTime.toString());
                dob.setText(dataSnapshot.child("Date of Birth").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
