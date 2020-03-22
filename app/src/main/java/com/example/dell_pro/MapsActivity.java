package com.example.dell_pro;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    boolean mLocationPermissionGranted = false;
    private static final float DEFAULT_ZOOM = 15f;
    private GoogleMap mMap;
    private Location current_location;
    FusedLocationProviderClient mFusedLocationProviderClient;

    Chip hosp,clin,store,park,gym;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        hosp=findViewById(R.id.hosp);
        clin=findViewById(R.id.clin);
        store=findViewById(R.id.store);
        park=findViewById(R.id.park);
        gym=findViewById(R.id.gym);

        mLocationPermissionGranted = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        chip_select();
        check_for_location_enabled();
    }

    private void chip_select() {

        hosp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat=current_location.getLatitude();
                double lon=current_location.getLongitude();
                final String latlng= lat +","+ lon;
                Uri location = Uri.parse("geo:"+latlng+"?q=Hospital");
                gotomap(location);
            }
        });

        clin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat=current_location.getLatitude();
                double lon=current_location.getLongitude();
                final String latlng= lat +","+ lon;
                Uri location = Uri.parse("geo:"+latlng+"?q=Clinic");
                gotomap(location);
            }
        });

        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat=current_location.getLatitude();
                double lon=current_location.getLongitude();
                final String latlng= lat +","+ lon;
                Uri location = Uri.parse("geo:"+latlng+"?q=Medical+Store");
                gotomap(location);
            }
        });

        park.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat=current_location.getLatitude();
                double lon=current_location.getLongitude();
                final String latlng= lat +","+ lon;
                Uri location = Uri.parse("geo:"+latlng+"?q=parks");
                gotomap(location);
            }
        });

        gym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat=current_location.getLatitude();
                double lon=current_location.getLongitude();
                final String latlng= lat +","+ lon;
                Uri location = Uri.parse("geo:"+latlng+"?q=gyms");
                gotomap(location);
            }
        });
    }

    private void gotomap(Uri location) {
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
        boolean isIntentSafe = activities.size() > 0;
        if (isIntentSafe) {
            Toast.makeText(this, "You will be Redirected to another Site.", Toast.LENGTH_SHORT).show();
            startActivity(mapIntent);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mLocationPermissionGranted == true) {
            getDeviceLocation();

            if (mLocationPermissionGranted == false) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            //mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
        Toast.makeText(this, "Click To Search For Places", Toast.LENGTH_SHORT).show();
    }

    private void getDeviceLocation() {

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionGranted == true) {

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            current_location = (Location) task.getResult();
                            moveCamera(new LatLng(current_location.getLatitude(),current_location.getLongitude()),DEFAULT_ZOOM);
                        } else {
                            Toast.makeText(MapsActivity.this, "Unable to get Current Location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException ignored) {

        }
    }

    private void moveCamera(LatLng latLng,float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void initMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        assert mapFragment != null;
        mapFragment.getMapAsync(MapsActivity.this);
    }

    //Permission and Checking for Location Setting
    private void check_for_location_enabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false, network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {

        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {

        }

        if (!gps_enabled && !network_enabled) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("GPS Network not Enabled.")
                    .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            finish();
                        }
                    });
            dialog.show();
        } else {
            initMap();
        }
    }
}
