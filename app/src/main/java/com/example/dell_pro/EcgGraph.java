package com.example.dell_pro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class EcgGraph extends AppCompatActivity {

    DatabaseReference reference;

    OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecg_graph);

        FirebaseAuth userAuth = FirebaseAuth.getInstance();
        FirebaseUser currUser = userAuth.getCurrentUser();
        assert currUser != null;
        String uid = currUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        //updating ecg data to firebase
        double[] newarr = {0.1601024, 0.60382927, 0.72430853, 0.13842815, -0.24229489,
                -0.03224487, 0.07757149, -0.00624149, -0.01258075, 0.00333573,
                0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5,
                0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5,
                0.1601024, 0.60382927, 0.72430853, 0.13842815, -0.24229489,
                -0.03224487, 0.07757149, -0.00624149, -0.01258075, 0.00333573,
                0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5,
                0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5,
                0.1601024, 0.60382927, 0.72430853, 0.13842815, -0.24229489,
                -0.03224487, 0.07757149, -0.00624149, -0.01258075, 0.00333573,
                0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5,
                0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5,
                0.1601024, 0.60382927, 0.72430853, 0.13842815, -0.24229489,
                -0.03224487, 0.07757149, -0.00624149, -0.01258075, 0.00333573,
                0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5,
                0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5,
                0.1601024, 0.60382927, 0.72430853, 0.13842815, -0.24229489,
                -0.03224487, 0.07757149, -0.00624149, -0.01258075, 0.00333573,
                0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5,
                0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5,
                0.1601024, 0.60382927, 0.72430853, 0.13842815, -0.24229489,
                -0.03224487, 0.07757149, -0.00624149, -0.01258075, 0.00333573,
                0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5,
                0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5,
                0.1601024, 0.60382927, 0.72430853, 0.13842815, -0.24229489,
                -0.03224487, 0.07757149, -0.00624149, -0.01258075, 0.00333573,
                0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5,
                0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5,
                0.1601024, 0.60382927, 0.72430853, 0.13842815, -0.24229489,
                -0.03224487, 0.07757149, -0.00624149, -0.01258075, 0.00333573,
                0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5,
                0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5,
                0.1601024, 0.60382927, 0.72430853, 0.13842815, -0.24229489,
                -0.03224487, 0.07757149, -0.00624149, -0.01258075, 0.00333573,
                0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5,
                0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5,
                0.1601024, 0.60382927, 0.72430853, 0.13842815, -0.24229489,
                -0.03224487, 0.07757149, -0.00624149, -0.01258075, 0.00333573,
                0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5,
                0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5, 0.1e-5};

        for (int i = 0; i < 200; i++)
            reference.child("ECG").child(Integer.toString(i)).setValue(newarr[i]);


        double y, x;
        final double[] arr;
        arr = new double[200];
        final GraphView graph = findViewById(R.id.graph);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

                for (int i = 0; i < 200; i++) {
                    arr[i] = (double) dataSnapshot.child("ECG").child(Integer.toString(i)).getValue();
                    Log.d("value of x is ", String.valueOf(arr[i]));
                    series.appendData(new DataPoint(i, arr[i]), false, 1000);
                }

                graph.addSeries(series);
                series.setColor(Color.MAGENTA);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

  /*      series = new LineGraphSeries<DataPoint>();
        for(int i =0; i<300; i++) {
            x = x + 1;
            y = arr[i]*100;
            series.appendData(new DataPoint(x, y), true, 1000);

        }*/
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-0.3);
        graph.getViewport().setMaxY(0.8);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(100);

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        graph.setTitle("Real time ECG Graph(The graph is just indicative and has been simulated in python due to unavailability of sensors)");
        //graph.setTitleTextSize(45);
        gridLabel.setHorizontalAxisTitle("Sample Number");
        gridLabel.setHorizontalAxisTitleTextSize(50);
        gridLabel.setVerticalAxisTitle("Amplitude (in mV)");
        gridLabel.setVerticalAxisTitleTextSize(50);

        //graph.takeSnapshotAndShare(this,"example","GraphView");
        //final Bitmap bitmap = graph.takeSnapshot();

        /*final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save Image to Mobile")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Bitmap bitmap = graph.takeSnapshot();
                        saveImage(bitmap);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do nothing
                    }
                });

        builder.show();*/
    }

    /*private void saveImage(Bitmap bitmap) {
        File filepath = Environment.getExternalStorageDirectory();
        File dir = new File(filepath.getAbsolutePath() + "/Care/");

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, System.currentTimeMillis() + ".png");

        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
        try {
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
