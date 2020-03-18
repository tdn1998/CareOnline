package com.example.dell_pro;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingFragment extends Fragment {

    private TextView display_name;
    private TextView display_email;
    private TextView edit_details;
    private CircleImageView view;
    private FirebaseUser user;
    private ProgressBar progress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings,container,false);

        TextView verify_email = v.findViewById(R.id.verify_email);
        edit_details=v.findViewById(R.id.edit_details);
        display_name = v.findViewById(R.id.setting_username);
        display_email = v.findViewById(R.id.setting_email);
        progress=v.findViewById(R.id.setting_progress);
        view = v.findViewById(R.id.setting_image);

        user = FirebaseAuth.getInstance().getCurrentUser();

        user_showdata();
        //update_data();

        return v;
    }

    /*private void update_data() {
        edit_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),NewProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),NewProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }*/

    private void user_showdata() {
        if (user != null) {
            if (user.getEmail() != null) {
                display_email.setText(user.getEmail());
            }
            if (user.getDisplayName() != null) {
                display_name.setText(user.getDisplayName());
            }
            if (user.getPhotoUrl() != null) {
                progress.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(user.getPhotoUrl().toString())
                        .error(R.drawable.blankprofile_round)
                        .into(view, new Callback() {
                            @Override
                            public void onSuccess() {
                                progress.setVisibility(View.INVISIBLE);
                                YoYo.with(Techniques.FadeIn)
                                        .duration(1000)
                                        .repeat(0)
                                        .playOn(view);
                            }

                            @Override
                            public void onError(Exception e) {
                                progress.setVisibility(View.INVISIBLE);
                            }
                        });
            }
        }
    }
}
