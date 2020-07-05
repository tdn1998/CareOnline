package com.example.dell_pro;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.dell_pro.authentication.LoginActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

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

        request_app_permissions();
    }

    private void request_app_permissions() {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                            Toast.makeText(StartActivity.this, "All Permissions Granted", Toast.LENGTH_SHORT).show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 2000);
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                            Toast.makeText(StartActivity.this, "Some Permissions are Permanently Denied", Toast.LENGTH_SHORT).show();
                            showSettingsDialog();
                        } else if (report.getDeniedPermissionResponses().size() != 0) {
                            Toast.makeText(StartActivity.this, "Some Permissions are Denied", Toast.LENGTH_SHORT).show();
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    public void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This App needs Permission to use its Features. You can grant them in App Settings.");
        builder.setPositiveButton("Goto Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        builder.show();
    }

    public void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
        finish();
    }
}
