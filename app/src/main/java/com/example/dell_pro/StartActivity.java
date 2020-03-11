package com.example.dell_pro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.dell_pro.authentication.LoginActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        YoYo.with(Techniques.FadeInUp)
                .duration(2000)
                .repeat(0)
                .playOn(findViewById(R.id.app_icon));

        YoYo.with(Techniques.FadeInRight)
                .duration(2000)
                .repeat(0)
                .playOn(findViewById(R.id.start_text));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }
}
