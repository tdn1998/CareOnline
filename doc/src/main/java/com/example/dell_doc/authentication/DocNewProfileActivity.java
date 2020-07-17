package com.example.dell_doc.authentication;

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
import com.example.dell_doc.DatePickerFragment;
import com.example.dell_doc.DocMainActivity;
import com.example.dell_doc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class DocNewProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private CircleImageView mImageview;
    private EditText doc_username, doc_phone_no, doc_mci, doc_special, doc_sup, doc_alma;
    private MaterialTextView date_picker;
    private ProgressDialog dialog;
    private ProgressBar progress;
    private MaterialTextView verified;

    private StorageReference profiledataref;
    private StorageTask task;
    private DatabaseReference mDatabase;
    private FirebaseUser user;

    //extra data
    AppCompatSpinner spinner1, spinner2, spinner3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_new_profile);

        doc_username = findViewById(R.id.doc_user_enter);
        doc_phone_no = findViewById(R.id.doc_user_phone);
        doc_mci = findViewById(R.id.doc_mci);
        doc_special = findViewById(R.id.special);
        doc_sup = findViewById(R.id.super_special);
        doc_alma = findViewById(R.id.alma);

        mImageview = findViewById(R.id.new_image_view);
        progress = findViewById(R.id.update_progress);
        spinner1 = findViewById(R.id.doc_spinner_gender);
        spinner2 = findViewById(R.id.doc_spinner_bldgrp);
        spinner3 = findViewById(R.id.doc_spinner_status);
        date_picker = findViewById(R.id.date_select);
        verified = findViewById(R.id.verified_user);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        assert user != null;
        String uid = user.getUid();
        profiledataref = FirebaseStorage.getInstance().getReference("profilepics/").child("docs/").child(uid);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Doctor").child(uid);

        field_data_setting();
        loaduserdata();
    }

    //Loading previously updated data
    private void loaduserdata() {

        if (user != null) {

            if (user.getDisplayName() != null) {
                Objects.requireNonNull(doc_username).setText(user.getDisplayName());
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
                        if (dataSnapshot.child("phno").exists()) {
                            String phone = dataSnapshot.child("phno").getValue(String.class);
                            doc_phone_no.setText(phone);

                        }

                        if (dataSnapshot.child("gender").exists()) {
                            String gender = dataSnapshot.child("gender").getValue(String.class);
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

                        if (dataSnapshot.child("bloodgrp").exists()) {
                            String blood = dataSnapshot.child("bloodgrp").getValue(String.class);
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

                        if (dataSnapshot.child("status").exists()) {
                            String gender = dataSnapshot.child("status").getValue(String.class);
                            assert gender != null;
                            switch (gender) {
                                case "Active":
                                    spinner3.setSelection(1);
                                    break;
                                case "Inactive":
                                    spinner1.setSelection(2);
                                    break;
                                default:
                                    spinner1.setSelection(0);
                                    break;
                            }
                        }

                        if (dataSnapshot.child("dob").exists()) {
                            String date = dataSnapshot.child("dob").getValue(String.class);
                            date_picker.setText(date);
                        }

                        if (dataSnapshot.child("mci").exists()) {
                            String mci = dataSnapshot.child("mci").getValue(String.class);
                            doc_mci.setText(mci);
                        }

                        if (dataSnapshot.child("special").exists()) {
                            String sp = dataSnapshot.child("special").getValue(String.class);
                            doc_special.setText(sp);
                        }

                        if (dataSnapshot.child("super_special").exists()) {
                            String sup_sp = dataSnapshot.child("super_special").getValue(String.class);
                            doc_sup.setText(sup_sp);
                        }

                        if (dataSnapshot.child("alma_mater").exists()) {
                            String al = dataSnapshot.child("alma_mater").getValue(String.class);
                            doc_alma.setText(al);
                        }

                        if(dataSnapshot.child("is_verified").exists()){
                            String is_verified= dataSnapshot.child("is_verified").getValue(String.class);

                            assert is_verified != null;
                            if (is_verified.equals("verified")) {
                                verified.setVisibility(View.VISIBLE);
                                Toast.makeText(DocNewProfileActivity.this, "Doctor is Verified", Toast.LENGTH_SHORT).show();
                            } else {
                                verified.setVisibility(View.INVISIBLE);
                                Toast.makeText(DocNewProfileActivity.this, "Doctor is not Verified", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    //choosing field data
    private void field_data_setting() {
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Gender")) {
                    //do nothing
                } else {
                    //String gender = parent.getItemAtPosition(position).toString();
                    //Toast.makeText(NewProfileActivity.this, "Gender: " + gender + " Selected", Toast.LENGTH_SHORT).show();
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
                    //String blood_grp = parent.getItemAtPosition(position).toString();
                    //Toast.makeText(NewProfileActivity.this, "Blood Group: " + blood_grp + " Selected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Status")) {
                    //do nothing
                } else {
                    //String blood_grp = parent.getItemAtPosition(position).toString();
                    //Toast.makeText(NewProfileActivity.this, "Blood Group: " + blood_grp + " Selected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
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


    //buttons clicked
    public void cancelupdate_clicked(View view) {
        Intent intent = new Intent(DocNewProfileActivity.this, DocMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void update_profile_clicked(View view) {
        if (task != null && task.isInProgress()) {
            Toast.makeText(this, "Wait Server is Busy", Toast.LENGTH_SHORT).show();
        } else {

            String user_name = Objects.requireNonNull(doc_username).getText().toString().trim();
            String phone = Objects.requireNonNull(doc_phone_no).getText().toString().trim();
            String mci = Objects.requireNonNull(doc_mci).getText().toString().trim();
            String sp = Objects.requireNonNull(doc_special).getText().toString().trim();
            String sup_sp = Objects.requireNonNull(doc_sup).getText().toString().trim();
            String alma = Objects.requireNonNull(doc_alma).getText().toString().trim();
            String dob = date_picker.getText().toString().trim();

            //conditions of fields being checked
            if (user_name.isEmpty()) {
                YoYo.with(Techniques.Shake)
                        .duration(500)
                        .repeat(0)
                        .playOn(doc_username);
                doc_username.setError("Username Required");
                doc_username.requestFocus();
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

            if (spinner3.getSelectedItem().toString().equals("Status")) {
                Toast.makeText(this, "Select Status", Toast.LENGTH_SHORT).show();
                return;
            }

            if (phone.isEmpty()) {
                YoYo.with(Techniques.Shake)
                        .duration(500)
                        .repeat(0)
                        .playOn(doc_phone_no);
                doc_phone_no.setError("Phone Number Required");
                doc_phone_no.requestFocus();
                return;
            }

            if (!Patterns.PHONE.matcher(phone).matches()) {
                YoYo.with(Techniques.Shake)
                        .duration(500)
                        .repeat(0)
                        .playOn(doc_phone_no);
                doc_phone_no.setError("Enter Valid Phone Number");
                doc_phone_no.requestFocus();
                return;
            }

            if (dob.equals("Date of Birth")) {
                Toast.makeText(this, "Enter Valid Date", Toast.LENGTH_SHORT).show();
                return;
            }

            if (mci.isEmpty() || sp.isEmpty() || sup_sp.isEmpty() || alma.isEmpty()) {
                Toast.makeText(this, "Fields cant be Empty", Toast.LENGTH_SHORT).show();
            }

            dialog = new ProgressDialog(DocNewProfileActivity.this, R.style.AppCompatAlertDialogStyle);
            dialog.setTitle("Please Wait");
            dialog.setMessage("Updating...");
            dialog.show();

            profile_photo_update(user_name);
            profile_data_update(spinner1.getSelectedItem().toString(), phone, spinner2.getSelectedItem().toString(), dob,
                    mci, sp, sup_sp, alma, spinner3.getSelectedItem().toString());
        }
    }

    private void profile_data_update(String toString, String phone, String toString1, String dob,
                                     String mci, String sp, String sup_sp, String alma, String toString2) {
        mDatabase.child("gender").setValue(toString);
        mDatabase.child("phno").setValue(phone);
        mDatabase.child("bloodgrp").setValue(toString1);
        mDatabase.child("dob").setValue(dob);

        mDatabase.child("mci").setValue(mci);
        mDatabase.child("special").setValue(sp);
        mDatabase.child("super_special").setValue(sup_sp);
        mDatabase.child("alma_mater").setValue(alma);
        mDatabase.child("status").setValue(toString2);
        mDatabase.child("is_verified").setValue("not_verified");


        mDatabase.child("id").setValue(user.getUid());
        Toast.makeText(this, "Profile Data Updated", Toast.LENGTH_SHORT).show();
    }

    private void profile_photo_update(final String name) {
        final StorageReference profileimgref = profiledataref.child(System.currentTimeMillis()
                + "." + getFileExtension(mImageUri));

        if (user.getPhotoUrl() == null) {
            //do nothing
        } else {
            final StorageReference del_prev_pic = FirebaseStorage.getInstance().getReferenceFromUrl(Objects.requireNonNull(user.getPhotoUrl()).toString());

            del_prev_pic.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(DocNewProfileActivity.this, "Previous Pic Deleted", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(DocNewProfileActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        task = profileimgref.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Toast.makeText(NewProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        get_user_data(profileimgref, name);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });
    }

    //get profile image url and display name after profile updated
    private void get_user_data(StorageReference ref, final String name) {
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (user != null && mImageUri != null) {
                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .setPhotoUri(uri)
                            .build();

                    mDatabase.child("docname").setValue(name);
                    mDatabase.child("imgurl").setValue(uri.toString());
                    //Toast.makeText(NewProfileActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();

                    user.updateProfile(profileUpdate)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        dialog.dismiss();
                                        Toast.makeText(DocNewProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(DocNewProfileActivity.this, DocMainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }

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
}