package com.example.dell_pro.authentication;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.DialogFragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.dell_pro.DatePickerFragment;
import com.example.dell_pro.MainActivity;
import com.example.dell_pro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private CircleImageView mImageview;
    private EditText username, phone_no, emerg_phone_no,user_desp;
    private MaterialTextView verified;
    private MaterialTextView date_picker;
    private ProgressDialog dialog;
    private ProgressBar progress;

    private StorageReference profiledataref;
    private StorageTask task;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    //extra data
    AppCompatSpinner spinner1, spinner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);

        mImageview = findViewById(R.id.new_image_view);
        username = findViewById(R.id.user_enter);
        phone_no = findViewById(R.id.user_phone);
        emerg_phone_no = findViewById(R.id.user_emrg_phone);
        user_desp=findViewById(R.id.user_desp);
        progress = findViewById(R.id.update_progress);
        spinner1 = findViewById(R.id.spinner_gender);
        spinner2 = findViewById(R.id.spinner_bldgrp);
        date_picker = findViewById(R.id.date_select);
        verified=findViewById(R.id.verified_user);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        assert user != null;
        String uid = user.getUid();
        profiledataref = FirebaseStorage.getInstance().getReference("profilepics/").child(uid);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(uid);

        if(user.isEmailVerified()){
            verified.setVisibility(View.VISIBLE);
            Toast.makeText(this, "User is Verified", Toast.LENGTH_SHORT).show();
        } else{
            verified.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "User is not Verified", Toast.LENGTH_SHORT).show();
        }

        field_data_setting();
        loaduserdata();
    }

    //Loading previously updated data
    private void loaduserdata() {
        if (user != null) {
            if (user.getDisplayName() != null) {
                Objects.requireNonNull(username).setText(user.getDisplayName());
            }
            if (user.getPhotoUrl() != null) {
                progress.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(user.getPhotoUrl().toString())
                        .error(R.drawable.blankprofile_round)
                        .into(mImageview, new Callback() {
                            @Override
                            public void onSuccess() {
                                progress.setVisibility(View.INVISIBLE);
                                YoYo.with(Techniques.FadeIn)
                                        .duration(1000)
                                        .repeat(0)
                                        .playOn(findViewById(R.id.new_image_view));
                            }

                            @Override
                            public void onError(Exception e) {
                                progress.setVisibility(View.INVISIBLE);
                            }
                        });
            }
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (dataSnapshot.child("Phone Number").exists()) {
                            String phone = dataSnapshot.child("Phone Number").getValue(String.class);
                            phone_no.setText(phone);

                        }
                        if (dataSnapshot.child("Emergency Phone Number").exists()) {
                            String phone = dataSnapshot.child("Emergency Phone Number").getValue(String.class);
                            emerg_phone_no.setText(phone);
                        }
                        if (dataSnapshot.child("Gender").exists()) {
                            String gender = dataSnapshot.child("Gender").getValue(String.class);
                            assert gender != null;
                            switch (gender) {
                                case "Male":
                                    spinner1.setSelection(1);
                                    break;
                                case "Female":
                                    spinner1.setSelection(2);
                                    break;
                                case "Other":
                                    spinner1.setSelection(3);
                                    break;
                                default:
                                    spinner1.setSelection(0);
                                    break;
                            }
                        }
                        if (dataSnapshot.child("Blood Group").exists()) {
                            String blood = dataSnapshot.child("Blood Group").getValue(String.class);
                            assert blood != null;
                            switch (blood) {
                                case "A+ve":
                                    spinner2.setSelection(1);
                                    break;
                                case "A-ve":
                                    spinner2.setSelection(2);
                                    break;
                                case "B+ve":
                                    spinner2.setSelection(3);
                                    break;
                                case "B-ve":
                                    spinner2.setSelection(4);
                                    break;
                                case "O+ve":
                                    spinner2.setSelection(5);
                                    break;
                                case "O-ve":
                                    spinner2.setSelection(6);
                                    break;
                                case "AB+ve":
                                    spinner2.setSelection(7);
                                    break;
                                case "AB-ve":
                                    spinner2.setSelection(8);
                                    break;
                                default:
                                    spinner2.setSelection(0);
                                    break;
                            }
                        }
                        if (dataSnapshot.child("Date of Birth").exists()) {
                            String date = dataSnapshot.child("Date of Birth").getValue(String.class);
                            date_picker.setText(date);
                        }
                        if (dataSnapshot.child("Description").exists()) {
                            String desp = dataSnapshot.child("Description").getValue(String.class);
                            user_desp.setText(desp);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    //buttons clicked
    public void cancelupdate_clicked(View view) {
        Intent intent = new Intent(NewProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void update_profile_clicked(View view) {
        if (task != null && task.isInProgress()) {
            Toast.makeText(this, "Wait Server is Busy", Toast.LENGTH_SHORT).show();
        } else {

            String user_name = Objects.requireNonNull(username).getText().toString().trim();
            String phone = Objects.requireNonNull(phone_no).getText().toString().trim();
            String emerg_phone = Objects.requireNonNull(emerg_phone_no).getText().toString().trim();
            String dob = date_picker.getText().toString().trim();
            String desp = Objects.requireNonNull(user_desp).getText().toString().trim();

            //conditions of fields being checked
            if (user_name.isEmpty()) {
                YoYo.with(Techniques.Shake)
                        .duration(500)
                        .repeat(0)
                        .playOn(username);
                username.setError("Username Required");
                username.requestFocus();
                return;
            }
            if (mImageUri == null) {
                Toast.makeText(this, "Choose Image", Toast.LENGTH_SHORT).show();
                return;
            }
            if (spinner1.getSelectedItem().toString().equals("Gender")) {
                Toast.makeText(this, "Select Gender", Toast.LENGTH_SHORT).show();
                return;
            }
            if (spinner2.getSelectedItem().toString().equals("Blood Group")) {
                Toast.makeText(this, "Select Blood Group", Toast.LENGTH_SHORT).show();
                return;
            }
            if(phone.isEmpty()){
                YoYo.with(Techniques.Shake)
                        .duration(500)
                        .repeat(0)
                        .playOn(phone_no);
                phone_no.setError("Phone Number Required");
                phone_no.requestFocus();
                return;
            }
            if (!Patterns.PHONE.matcher(phone).matches()) {
                YoYo.with(Techniques.Shake)
                        .duration(500)
                        .repeat(0)
                        .playOn(phone_no);
                phone_no.setError("Enter Valid Phone Number");
                phone_no.requestFocus();
                return;
            }
            if(emerg_phone.isEmpty()){
                YoYo.with(Techniques.Shake)
                        .duration(500)
                        .repeat(0)
                        .playOn(emerg_phone_no);
                emerg_phone_no.setError("Emergency Phone Number Required");
                emerg_phone_no.requestFocus();
                return;
            }
            if (!Patterns.PHONE.matcher(emerg_phone).matches()) {
                YoYo.with(Techniques.Shake)
                        .duration(500)
                        .repeat(0)
                        .playOn(emerg_phone_no);
                emerg_phone_no.setError("Enter Valid Emergency Phone Number");
                emerg_phone_no.requestFocus();
                return;
            }
            if (phone.equals(emerg_phone)) {
                Toast.makeText(this, "Phone Number should not match Emergency Phone Number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (dob.equals("Date of Birth")) {
                Toast.makeText(this, "Enter Valid Date", Toast.LENGTH_SHORT).show();
                return;
            }

            dialog = new ProgressDialog(NewProfileActivity.this, R.style.AppCompatAlertDialogStyle);
            dialog.setTitle("Please Wait");
            dialog.setMessage("Updating...");
            dialog.show();

            profile_photo_update(user_name);
            profile_data_update(spinner1.getSelectedItem().toString(), phone, spinner2.getSelectedItem().toString(), emerg_phone, dob,desp);
        }
    }

    //choosing new image
    public void choose_profile_img(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent
                .createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri)
                    .into(mImageview);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    //choosing field data
    private void field_data_setting() {
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Gender")) {
                    //do nothing
                } else {
                    String gender = parent.getItemAtPosition(position).toString();
                    Toast.makeText(NewProfileActivity.this, "Gender: " + gender + " Selected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Blood Group")) {
                    //do nothing
                } else {
                    String blood_grp = parent.getItemAtPosition(position).toString();
                    Toast.makeText(NewProfileActivity.this, "Blood Group: " + blood_grp + " Selected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        date_picker.setOnClickListener(v -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDateString;
        month = month + 1;
        //String currentDateString = DateFormat.getDateInstance().format(c.getTime());
        //String currentDateString = dayOfMonth+"/"+month+"/"+year;
        if (dayOfMonth > 9) {
            if (month > 9) {
                currentDateString = dayOfMonth + "/" + month + "/" + year;
            } else {
                currentDateString = dayOfMonth + "/0" + month + "/" + year;
            }
        } else {
            if (month > 9) {
                currentDateString = "0" + dayOfMonth + "/" + month + "/" + year;
            } else {
                currentDateString = "0" + dayOfMonth + "/0" + month + "/" + year;
            }
        }

        date_picker.setText(currentDateString);
    }

    //updating profile fields
    private void profile_data_update(String gender, String phone, String blood_group, String emerg_phone, String date, String Desp) {
        mDatabase.child("Gender").setValue(gender);
        mDatabase.child("Phone Number").setValue(phone);
        mDatabase.child("Blood Group").setValue(blood_group);
        mDatabase.child("Emergency Phone Number").setValue(emerg_phone);
        mDatabase.child("Date of Birth").setValue(date);
        mDatabase.child("Description").setValue(Desp);
        Toast.makeText(this, "Profile Data Updated", Toast.LENGTH_SHORT).show();

        //set extras
        int oxy_sat=generateRandom(95,100);
        int pul_rate=generateRandom(60,100);
        int temp=generateRandom(97,99);
        mDatabase.child("pulse_oximeter").child("oxy_sat").setValue(oxy_sat);
        mDatabase.child("pulse_oximeter").child("pul_rate").setValue(pul_rate);
        mDatabase.child("temp").setValue(temp);
    }

    private int generateRandom(int a, int b) {
        int ans;
        Random rand = new Random();
        ans = a + rand.nextInt(b - a + 1);
        return ans;
    }

    private void profile_photo_update(final String name) {
        final StorageReference profileimgref = profiledataref.child(System.currentTimeMillis()
                + "." + getFileExtension(mImageUri));

        task = profileimgref.putFile(mImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    //Toast.makeText(NewProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    get_user_data(profileimgref, name);
                })
                .addOnFailureListener(e -> Toast.makeText(NewProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show())
                .addOnProgressListener(taskSnapshot -> {
                    //Toast.makeText(NewProfileActivity.this, "Uploading", Toast.LENGTH_SHORT).show();
                });
    }

    //get profile image url and display name after profile updated
    private void get_user_data(StorageReference ref, final String name) {
        ref.getDownloadUrl().addOnSuccessListener(uri -> {
            if (user != null && mImageUri != null) {
                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .setPhotoUri(uri)
                        .build();

                user.updateProfile(profileUpdate)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                Toast.makeText(NewProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(NewProfileActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        });
            }
        });
    }
}
