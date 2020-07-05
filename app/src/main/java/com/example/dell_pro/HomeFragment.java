package com.example.dell_pro;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.bmicalculator.BMIActivity;
import com.example.covid_chatbot.CoronaActivity;
import com.example.nearbymaps.MapsActivity;
import com.example.reminder.ui.RemindActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.Random;

public class HomeFragment extends Fragment {

    private final int[] imageArray = {R.drawable.blood_donation, R.drawable.clinics,
            R.drawable.covid_banner,
            R.drawable.corona,
            R.drawable.doctor_interaction};

    private static final String TAG = "MainActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    private FirebaseUser user;
    private DatabaseReference mDatabase;

    private CardView nearby;
    int count = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        final ImageView imageView = v.findViewById(R.id.image_slide);
        CardView test = v.findViewById(R.id.test);
        CardView chat = v.findViewById(R.id.chat);
        nearby = v.findViewById(R.id.nearby);
        CardView remainder = v.findViewById(R.id.remainder);
        CardView video = v.findViewById(R.id.video);
        CardView bmi_cal = v.findViewById(R.id.bmi_cal);
        CardView reports = v.findViewById(R.id.reports);
        CardView chat_bot = v.findViewById(R.id.chatbot);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        assert user != null;
        String uid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            int i = 0;

            public void run() {
                YoYo.with(Techniques.FadeInRight)
                        .duration(2000)
                        .repeat(0)
                        .playOn(imageView);
                imageView.setImageResource(imageArray[i]);
                i++;
                if (i > imageArray.length - 1) {
                    i = 0;
                }
                handler.postDelayed(this, 5000);
            }
        };

        handler.postDelayed(runnable, 5000);

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.isEmailVerified()) {
                    //Intent intent = new Intent(getContext(), HealthActivity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    count++;
                    int oxy_sat = generateRandom(95, 100);
                    int pul_rate = generateRandom(60, 100);
                    int temp = generateRandom(97, 99);
                    mDatabase.child("pulse_oximeter").child("oxy_sat").setValue(oxy_sat);
                    mDatabase.child("pulse_oximeter").child("pul_rate").setValue(pul_rate);
                    mDatabase.child("temp").setValue(temp);

                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    builder.setTitle("Go To Medical Report?")
                            .setMessage("Your test is Complete...Goto Medical Report to check results (This is Just a Simulation...!!!)")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    goto_report();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //do nothing
                                }
                            }).show();

                    //Toast.makeText(getContext(), "Your test is complete...Goto reports to check results(This is just a simulation!!!) ", Toast.LENGTH_LONG).show();
                    //startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Verify the Email to Use its Services", Toast.LENGTH_SHORT).show();
                }
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.isEmailVerified()) {
                    //Intent intent = new Intent(getContext(), HealthActivity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Toast.makeText(getContext(), "Chat With Doctor", Toast.LENGTH_SHORT).show();
                    //startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Verify the Email to Use its Services", Toast.LENGTH_SHORT).show();
                }
            }
        });

        remainder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.isEmailVerified()) {
                    Intent intent = new Intent(getContext(), RemindActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Toast.makeText(getContext(), "Medicine Remainder", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Verify the Email to Use its Services", Toast.LENGTH_SHORT).show();
                }
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.isEmailVerified()) {
                    //Intent intent = new Intent(getContext(), HealthActivity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Toast.makeText(getContext(), "How to Use?", Toast.LENGTH_SHORT).show();
                    //startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Verify the Email to Use its Services", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bmi_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.isEmailVerified()) {
                    Intent intent = new Intent(getContext(), BMIActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Toast.makeText(getContext(), "BMI Calculator", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Verify the Email to Use its Services", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.isEmailVerified()) {
                    goto_report();
                } else {
                    Toast.makeText(getContext(), "Verify the Email to Use its Services", Toast.LENGTH_SHORT).show();
                }
            }
        });

        chat_bot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.isEmailVerified()) {
                    Intent intent = new Intent(getContext(), CoronaActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Toast.makeText(getContext(), "COVID ChatBot", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Verify the Email to Use its Services", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (isServicesOK()) {
            init();
        }

        return v;
    }

    private void goto_report() {
        if (count == 0)
            Toast.makeText(getContext(), "Click on personal check up to start test", Toast.LENGTH_SHORT).show();
        else if (user.getDisplayName() != null) {
            Intent intent = new Intent(getContext(), ReportActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Toast.makeText(getContext(), "View Report", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            count = 0;
        } else
            Toast.makeText(getContext(), "Complete your profile first!! Go to settings->See Profile", Toast.LENGTH_SHORT).show();
    }

    private void init() {
        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.isEmailVerified()) {
                    Intent intent = new Intent(getContext(), MapsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //Toast.makeText(getContext(), "Health Centres", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Verify the Email to Use its Services", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(getContext(), "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private int generateRandom(int a, int b) {
        int ans;
        Random rand = new Random();
        ans = a + rand.nextInt(b - a + 1);
        return ans;
    }
}
