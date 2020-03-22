package com.example.dell_pro;

import android.app.Dialog;
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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeFragment extends Fragment {

    final int[] imageArray = {R.drawable.blood_donation, R.drawable.clinics,
            R.drawable.doctor_interaction};

    private static final String TAG = "MainActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    private FirebaseUser user;

    CardView test,chat,nearby,remainder,video,bmi_cal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        final ImageView imageView = v.findViewById(R.id.image_slide);

        test = v.findViewById(R.id.test);
        chat = v.findViewById(R.id.chat);
        nearby = v.findViewById(R.id.nearby);
        remainder = v.findViewById(R.id.remainder);
        video = v.findViewById(R.id.video);
        bmi_cal = v.findViewById(R.id.bmi_cal);

        user = FirebaseAuth.getInstance().getCurrentUser();

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
            public void onClick(View v) {
                if (user.isEmailVerified()) {
                    //Intent intent = new Intent(getContext(), HealthActivity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Toast.makeText(getContext(), "Personal Checkup", Toast.LENGTH_SHORT).show();
                    //startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Verify the Email to Use its Services", Toast.LENGTH_SHORT).show();
                }
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            public void onClick(View v) {
                if (user.isEmailVerified()) {
                    //Intent intent = new Intent(getContext(), HealthActivity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Toast.makeText(getContext(), "Medicine Remainder", Toast.LENGTH_SHORT).show();
                    //startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Verify the Email to Use its Services", Toast.LENGTH_SHORT).show();
                }
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            public void onClick(View v) {
                if (user.isEmailVerified()) {
                    //Intent intent = new Intent(getContext(), HealthActivity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Toast.makeText(getContext(), "BMI Calculator", Toast.LENGTH_SHORT).show();
                    //startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Verify the Email to Use its Services", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(isServicesOK()){
            init();
        }

        return v;
    }

    private void init(){
        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(getContext(), "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
