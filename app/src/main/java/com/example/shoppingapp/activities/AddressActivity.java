package com.example.shoppingapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingapp.R;

// MAP
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

// LOCATION
import com.google.android.gms.location.*;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class AddressActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    Button btnContinue;
    EditText address;

    FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        btnContinue = findViewById(R.id.continueBtn);
        address = findViewById(R.id.address);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        btnContinue.setOnClickListener(v -> {
            Toast.makeText(this, "Address Selected 📍", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, PaymentActivity.class));
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);

            // 🔥 REAL-TIME LOCATION
            LocationRequest request = LocationRequest.create();
            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            request.setInterval(2000);

            fusedLocationClient.requestLocationUpdates(
                    request,
                    new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult result) {

                            if (result == null) return;

                            Location location = result.getLastLocation();

                            if (location != null) {

                                LatLng userLoc = new LatLng(
                                        location.getLatitude(),
                                        location.getLongitude()
                                );

                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLoc, 17));

                                // 🔥 AUTO ADDRESS
                                Geocoder geocoder = new Geocoder(AddressActivity.this, Locale.getDefault());

                                try {
                                    List<Address> addresses = geocoder.getFromLocation(
                                            location.getLatitude(),
                                            location.getLongitude(),
                                            1
                                    );

                                    if (addresses != null && !addresses.isEmpty()) {
                                        address.setText(addresses.get(0).getAddressLine(0));
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    },
                    getMainLooper()
            );

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }

        // 🔥 CLICK → CHANGE ADDRESS
        mMap.setOnMapClickListener(latLng -> {

            mMap.clear();

            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Selected Location"));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            try {
                List<Address> addresses = geocoder.getFromLocation(
                        latLng.latitude,
                        latLng.longitude,
                        1
                );

                if (addresses != null && !addresses.isEmpty()) {
                    address.setText(addresses.get(0).getAddressLine(0));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}