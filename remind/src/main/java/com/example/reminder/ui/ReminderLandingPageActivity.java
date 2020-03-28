package com.example.reminder.ui;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reminder.simplealarms.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public final class ReminderLandingPageActivity extends AppCompatActivity implements SensorEventListener {

    private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;
    private float x, y, z;
    private int count;

    static final int SHAKE_THRESHOLD = 100;
    public static int DATA_X = SensorManager.DATA_X;
    public static int DATA_Y = SensorManager.DATA_Y;
    public static int DATA_Z = SensorManager.DATA_Z;
    public SensorManager sensorManager;
    public Sensor accelerormeterSensor;
    private PowerManager pm;
    private PowerManager.WakeLock wl;
    private Vibrator vibrator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_landing_page);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assert sensorManager != null;
        accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        final FloatingActionButton launchMainActivityBtn = findViewById(R.id.load_main_activity_btn);
        final FloatingActionButton dismiss = findViewById(R.id.dismiss_btn);

        launchMainActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReminderLandingPageActivity.this, RemindActivity.class));
                vibrator.cancel();
                finish();
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.cancel();
                finish();
            }
        });
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        startvibe();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (accelerormeterSensor != null)
            sensorManager.registerListener(this, accelerormeterSensor,
                    SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (sensorManager != null)
            sensorManager.unregisterListener(this);
    }

    public static Intent launchIntent(Context context) {
        final Intent i = new Intent(context, ReminderLandingPageActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);
            if (gabOfTime > 100) {
                lastTime = currentTime;
                x = event.values[SensorManager.DATA_X];
                y = event.values[SensorManager.DATA_Y];
                z = event.values[SensorManager.DATA_Z];
                speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;
                if (speed > SHAKE_THRESHOLD) {
                    lastTime = currentTime;
                    count++;//for shake test
                    if (count == 10) {
                        vibrator.cancel();
                        finish();
                    }
                }
                lastX = event.values[DATA_X];
                lastY = event.values[DATA_Y];
                lastZ = event.values[DATA_Z];
            }
        }
    }

    public void startvibe() {
        vibrator.vibrate(new long[]{100, 1000, 100, 500, 100, 1000}, 0);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
