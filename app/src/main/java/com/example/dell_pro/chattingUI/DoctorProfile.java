package com.example.dell_pro.chattingUI;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.dell_pro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class DoctorProfile extends AppCompatActivity {

    private TextView name, status,ph_no,special,sup_special;
    private ImageView image;

    private DatabaseReference reference;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("Profile");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        final String id = getIntent().getStringExtra("id");

        status = findViewById(R.id.doc_profile_status);
        name = findViewById(R.id.doc_profile_name);
        image = findViewById(R.id.doc_profile_image);
        ph_no = findViewById(R.id.phno);
        special = findViewById(R.id.special);
        sup_special = findViewById(R.id.sup_special);

        //status.setText(id);

        assert id != null;
        reference = FirebaseDatabase.getInstance().getReference().child("Doctor").child(id);
        user = FirebaseAuth.getInstance().getCurrentUser();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String dis_name = Objects.requireNonNull(dataSnapshot.child("docname").getValue()).toString();
                String dis_status = Objects.requireNonNull(dataSnapshot.child("status").getValue()).toString();
                String imgurl = Objects.requireNonNull(dataSnapshot.child("imgurl").getValue()).toString();

                String ph = Objects.requireNonNull(dataSnapshot.child("phno").getValue()).toString();
                String sp = Objects.requireNonNull(dataSnapshot.child("special").getValue()).toString();
                String susp = Objects.requireNonNull(dataSnapshot.child("super_special").getValue()).toString();

                name.setText(dis_name);
                status.setText(dis_status);
                ph_no.append(ph);
                special.append(sp);
                sup_special.append(susp);

                Picasso.get().load(imgurl).into(image);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}