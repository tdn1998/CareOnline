package com.example.dell_pro;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.dell_pro.authentication.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements PanicFragment.PanicListner{

    private BottomNavigationView bot_nav;

    private DrawerLayout nav_drawer;
    private NavigationView nav_view;

    private long backpressedtime;
    private Toast backtoast;

    private CircleImageView draw_img;
    private TextView draw_user;
    private TextView draw_email;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("Features");
        setSupportActionBar(toolbar);

        bot_nav=findViewById(R.id.bottom_navigation);

        nav_drawer=findViewById(R.id.drawer_layout);
        nav_view=findViewById(R.id.drawer_view);

        View header=nav_view.getHeaderView(0);

        user = FirebaseAuth.getInstance().getCurrentUser();

        draw_img=header.findViewById(R.id.drawer_user_pic);
        draw_user=header.findViewById(R.id.drawer_user_name);
        draw_email=header.findViewById(R.id.drawer_user_email);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, nav_drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        nav_drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            nav_view.setCheckedItem(R.id.drawer_home);
            bot_nav.setSelectedItemId(R.id.bottom_home);
        }

        user_loaddata();
        open_fragments_bottom_nav();
        open_fragments_drawer_nav();
    }

    @Override
    public void onButtonClicked(String text) {
        if(text.equals("1")){
            Toast.makeText(this, "Ambulance", Toast.LENGTH_SHORT).show();
            bot_nav.setSelectedItemId(R.id.bottom_dummy);
        }
        if(text.equals("2")){
            Toast.makeText(this, "Fire Fighter", Toast.LENGTH_SHORT).show();
            bot_nav.setSelectedItemId(R.id.bottom_dummy);
        }
        if(text.equals("3")){
            Toast.makeText(this, "Police", Toast.LENGTH_SHORT).show();
            bot_nav.setSelectedItemId(R.id.bottom_dummy);
        }
    }

    private void user_loaddata() {
        if (user != null) {
            if (user.getEmail() != null) {
                draw_email.setText(user.getEmail());
            }
            if (user.getDisplayName() != null) {
                draw_user.setText(user.getDisplayName());
            }
            if (user.getPhotoUrl() != null) {
                Picasso.get()
                        .load(user.getPhotoUrl().toString())
                        .error(R.drawable.blankprofile_round)
                        .into(draw_img);
            }
        }
    }

    //on clicking on nav drawer button
    private void open_fragments_drawer_nav() {
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId()){
                    case R.id.drawer_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new HomeFragment()).commit();
                        bot_nav.setSelectedItemId(R.id.bottom_home);
                        setTitle("Features");
                        break;
                    case R.id.drawer_notification:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new NotificationFragment()).commit();
                        bot_nav.setSelectedItemId(R.id.bottom_notification);
                        setTitle("Notification");
                        break;
                    case R.id.drawer_setting:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new SettingFragment()).commit();
                        bot_nav.setSelectedItemId(R.id.bottom_settings);
                        setTitle("Settings");
                        break;
                    case R.id.drawer_signout:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.drawer_contact:
                    case R.id.drawer_help:
                    case R.id.drawer_terms:
                    case R.id.drawer_feedback:
                    case R.id.drawer_about:
                        Toast.makeText(MainActivity.this, "Not Applicable Now", Toast.LENGTH_SHORT).show();
                        break;
                }
                nav_drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    //on clicking buttom nav drawer
    private void open_fragments_bottom_nav() {
        bot_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.bottom_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new HomeFragment()).commit();
                        nav_view.setCheckedItem(R.id.drawer_home);
                        setTitle("Features");
                        break;
                    case R.id.bottom_notification:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new NotificationFragment()).commit();
                        nav_view.setCheckedItem(R.id.drawer_notification);
                        setTitle("Notification");
                        break;
                    case R.id.bottom_settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new SettingFragment()).commit();
                        nav_view.setCheckedItem(R.id.drawer_setting);
                        setTitle("Settings");
                        break;
                    case R.id.bottom_panic:
                        panic_button_clicked();
                        nav_view.setCheckedItem(R.id.drawer_dummy);
                        break;
                }
                return true;
            }
        });
    }

    private void panic_button_clicked() {
        PanicFragment panic = new PanicFragment();
        panic.show(getSupportFragmentManager(),"panic options");
    }
}
