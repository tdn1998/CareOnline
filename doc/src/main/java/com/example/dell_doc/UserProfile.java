package com.example.dell_doc;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class UserProfile extends AppCompatActivity {

    private TextView name, phone, blood, dob, gender, desp;
    private ImageView image;

    private DatabaseReference reference;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("Profile");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        final String id = getIntent().getStringExtra("id");

        name = findViewById(R.id.user_profile_name);
        phone = findViewById(R.id.user_phone);
        blood = findViewById(R.id.user_blood);
        dob = findViewById(R.id.user_dob);
        gender = findViewById(R.id.user_gender);
        desp = findViewById(R.id.user_desp);
        image = findViewById(R.id.user_profile_image);

        //status.setText(id);

        assert id != null;
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
        user = FirebaseAuth.getInstance().getCurrentUser();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String dis_name = Objects.requireNonNull(dataSnapshot.child("username").getValue()).toString();
                String phone_no = Objects.requireNonNull(dataSnapshot.child("phno").getValue()).toString();
                String imgurl = Objects.requireNonNull(dataSnapshot.child("imgurl").getValue()).toString();
                String blood_grp = Objects.requireNonNull(dataSnapshot.child("bloodgrp").getValue()).toString();
                String date = Objects.requireNonNull(dataSnapshot.child("dob").getValue()).toString();
                String gend = Objects.requireNonNull(dataSnapshot.child("gender").getValue()).toString();
                String despt = Objects.requireNonNull(dataSnapshot.child("desp").getValue()).toString();

                name.setText(dis_name);
                phone.append(phone_no);
                blood.append(blood_grp);
                dob.append(date);
                gender.append(gend);
                desp.append(despt);

                Picasso.get().load(imgurl).into(image);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
