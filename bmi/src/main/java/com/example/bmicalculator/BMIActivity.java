package com.example.bmicalculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

public class BMIActivity extends AppCompatActivity {

    private Toolbar toolbar_bmi;
    private ChipGroup chipGroup;
    private AppCompatSeekBar height_seek, weight_seek;
    private MaterialTextView height_text, weight_text, your_bmi_value, bmi_status, bmi_table;
    private FloatingActionButton cal;
    private int gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        chipGroup = findViewById(R.id.chip_group);
        height_seek = findViewById(R.id.height_seek);
        weight_seek = findViewById(R.id.weight_seek);
        height_text = findViewById(R.id.height_value);
        weight_text = findViewById(R.id.weight_value);
        your_bmi_value = findViewById(R.id.your_bmi_value);
        bmi_status = findViewById(R.id.bmi_status);
        bmi_table = findViewById(R.id.bmi_table);
        cal = findViewById(R.id.cal);

        toolbar_bmi = findViewById(R.id.toolbar_bmi);
        setSupportActionBar(toolbar_bmi);

        height_text.setText(String.valueOf(height_seek.getProgress()));
        weight_text.setText(String.valueOf(weight_seek.getProgress()));
        buttons_clicked();
    }

    private void buttons_clicked() {
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                if (chip != null) {
                    Toast.makeText(getApplicationContext(), "Gender " + chip.getText() + " Selected", Toast.LENGTH_SHORT).show();
                }
                if (chip.getText().equals("Male")) {
                    gender = 1;
                } else if (chip.getText().equals("Female")) {
                    gender = 2;
                } else {
                    gender = 0;
                }
            }
        });

        height_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                height_text.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        weight_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                weight_text.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gender == 0) {
                    Toast.makeText(BMIActivity.this, "Select Gender", Toast.LENGTH_SHORT).show();
                } else {
                    float height_value = height_seek.getProgress();
                    float weight_value = weight_seek.getProgress();
                    float bmi_value = bmi_cal(height_value, weight_value);
                    show_status(bmi_value);
                }
            }
        });

        bmi_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BMIActivity.this, BMITableActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void show_status(float value) {
        if (value >= 18.0 && value <= 25.0) {
            your_bmi_value.setText(String.format("%.2f", value));
            your_bmi_value.setTextColor(getColor(R.color.MEDIUMSEAGREEN));
            bmi_status.setText("You are Normal.");
            bmi_status.setTextColor(getColor(R.color.MEDIUMSEAGREEN));
        } else if (value < 18) {
            your_bmi_value.setText(String.format("%.2f", value));
            your_bmi_value.setTextColor(getColor(R.color.indianred));
            bmi_status.setText("You are Underweight.");
            bmi_status.setTextColor(getColor(R.color.indianred));
        } else if (value > 25 && value <= 30) {
            your_bmi_value.setText(String.format("%.2f", value));
            your_bmi_value.setTextColor(getColor(R.color.indianred));
            bmi_status.setText("You are Overweight.");
            bmi_status.setTextColor(getColor(R.color.indianred));
        } else if (value > 30 && value <= 35) {
            your_bmi_value.setText(String.format("%.2f", value));
            your_bmi_value.setTextColor(getColor(R.color.indianred));
            bmi_status.setText("You are Obese.");
            bmi_status.setTextColor(getColor(R.color.indianred));
        } else if (value > 35 && value <= 40) {
            your_bmi_value.setText(String.format("%.2f", value));
            your_bmi_value.setTextColor(getColor(R.color.indianred));
            bmi_status.setText("You are severely Obese.");
            bmi_status.setTextColor(getColor(R.color.indianred));
        } else {
            your_bmi_value.setText(String.format("%.2f", value));
            your_bmi_value.setTextColor(getColor(R.color.indianred));
            bmi_status.setText("You are Morbidly Obese.");
            bmi_status.setTextColor(getColor(R.color.indianred));
        }
    }

    private float bmi_cal(float height, float weight) {
        float bmi = 0;
        bmi = weight / ((height / 100) * (height / 100));
        return bmi;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bmi_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_help) {
            //Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("What is BMI?")
                    .setMessage("Body mass index, or BMI, is used to determine whether you are in Healthy Weight Range for your Height.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    }).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
