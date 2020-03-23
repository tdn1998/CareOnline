package com.example.dell_pro.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.dell_pro.R;
import com.example.dell_pro.authentication.LoginActivity;
import com.example.dell_pro.authentication.NewProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingFragment extends Fragment {

    private MaterialTextView display_name, display_email;
    private CircleImageView view;
    private ImageView verified;
    private FirebaseUser user;
    private ProgressBar progress;

    private RecyclerView mRecyclerView;
    private SettingAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings,container,false);

        display_name = v.findViewById(R.id.setting_username);
        display_email = v.findViewById(R.id.setting_email);
        progress=v.findViewById(R.id.setting_progress);
        view = v.findViewById(R.id.setting_image);
        verified = v.findViewById(R.id.verification);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified()) {
            verified.setVisibility(View.VISIBLE);
        } else {
            verified.setVisibility(View.INVISIBLE);
        }

        final ArrayList<SettingItem> settingList = new ArrayList<>();
        settingList.add(new SettingItem(R.drawable.blankprofile_round, "See Profile", "Change Details of User"));
        settingList.add(new SettingItem(R.drawable.verified, "Verify User", "Check Email for Verification"));
        settingList.add(new SettingItem(R.drawable.mail, "Reset Email", "Change Email of User Account"));
        settingList.add(new SettingItem(R.drawable.key, "Reset Password", "Change Password of User Account"));
        settingList.add(new SettingItem(R.drawable.help_icon, "Help", "FAQs, Privacy Policy"));
        settingList.add(new SettingItem(R.drawable.delete, "Delete Account", "Delete User Account and All its Data"));
        settingList.add(new SettingItem(R.drawable.signout_icon, "Sign Out", "User Sign out and Close all"));

        mRecyclerView = v.findViewById(R.id.recycle_settings);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new SettingAdapter(settingList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        buttons_clicked();
        user_show_profile();
        return v;
    }

    private void buttons_clicked() {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_profile();
            }
        });

        mAdapter.setOnItemClickListener(new SettingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0:
                        update_profile();
                        break;
                    case 1:
                        email_verify();
                        break;
                    case 2:
                        reset_email();
                        break;
                    case 3:
                        reset_password();
                        break;
                    case 4:
                        help();
                        break;
                    case 5:
                        delete_account();
                        break;
                    case 6:
                        user_signout();
                        break;
                }
            }
        });
    }

    private void delete_account() {
    }

    private void help() {
    }

    private void reset_password() {
    }

    private void reset_email() {
    }

    private void user_signout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent1 = new Intent(getContext(), LoginActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent1);
        getActivity().finish();
    }

    private void email_verify() {
        if (user.isEmailVerified()) {
            Toast.makeText(getContext(), "Email is Already Verified", Toast.LENGTH_SHORT).show();
        } else {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(getContext(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                getActivity().finish();
                                Toast.makeText(getContext(), "Verification Email Sent", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void update_profile() {
        Intent intent = new Intent(getContext(),NewProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void user_show_profile() {
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
