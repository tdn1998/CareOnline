package com.example.dell_pro.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.dell_pro.MainActivity;
import com.example.dell_pro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthList;

    private TextInputLayout emailf;
    private TextInputLayout passf;

    private long backpressedtime;
    private Toast backtoast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailf=findViewById(R.id.email_enter);
        passf=findViewById(R.id.pass_enter);

        mAuth = FirebaseAuth.getInstance();
        mAuthList = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthList);
    }

    //on pressing back in this activity
    @Override
    public void onBackPressed() {
        if(backpressedtime+2000>System.currentTimeMillis()){
            backtoast.cancel();
        super.onBackPressed();
        return;
        } else{
            backtoast=Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT);
            backtoast.show();
        }
        backpressedtime= System.currentTimeMillis();
    }

    public void create_account_clicked(View view) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void login_clicked(View View){
        String email = Objects.requireNonNull(emailf.getEditText()).getText().toString().trim();
        String pass = Objects.requireNonNull(passf.getEditText()).getText().toString().trim();

        //checking for validation
        if(email.isEmpty()){
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .repeat(0)
                    .playOn(emailf);
            emailf.setError("*Field Can't be Empty");
            emailf.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .repeat(0)
                    .playOn(emailf);
            emailf.setError("*Enter Valid Email");
            emailf.requestFocus();
            return;
        }
        if(pass.isEmpty()){
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .repeat(0)
                    .playOn(passf);
            passf.setError("*Field Can't be Empty");
            passf.requestFocus();
            return;
        }

        if(pass.length()<6){
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .repeat(0)
                    .playOn(passf);
            passf.setError("*Minimum length of password should be 6");
            passf.requestFocus();
            return;
        }

        final ProgressDialog dialog;
        dialog =new ProgressDialog(LoginActivity.this,R.style.AppCompatAlertDialogStyle);
        dialog.setTitle("Please Wait");
        dialog.setMessage("Authenticating...");
        dialog.show();

        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //check for task successful
                if(task.isSuccessful()){
                    //if success
                    dialog.dismiss();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else{
                    //if failed error message
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void forget_clicked(View view) {
        Toast.makeText(this, "Not Applicable", Toast.LENGTH_SHORT).show();
    }
}
