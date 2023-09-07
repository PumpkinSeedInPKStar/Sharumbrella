
package com.nanumy.sharumbrella;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationRequest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

// public class ReturnMap extends AppCompatActivity implements OnMapReadyCallback{
public class ReturnMap extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private boolean isPermissionGranted = false;
    private boolean isNearConvenienceStore = false;
    private Marker convenienceStoreMarker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_map);
        /*
        // Initialize FusedLocationProviderClient and FusedLocationSource
        fusedLocationClient = new FusedLocationProviderClient(this);
        locationSource = new FusedLocationSource(this, 1001);

        // Initialize location request parameters
        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.QUALITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(5000);

        // Check and request location permissions
        checkLocationPermission();

        // Initialize location callback
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (isNearConvenienceStore) {
                    // Check if the user is near the convenience store
                    checkProximityToConvenienceStore(locationResult.getLastLocation());
                }
            }
        };
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Initialize the OK button
        Button okButton = findViewById(R.id.button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNearConvenienceStore) {
                    // Execute the return process
                    performReturn();
                } else {
                    // Display a message indicating that the user is not at the return location
                    Toast.makeText(ReturnMap.this, "아직 반납소가 아닙니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        */
    }

    /*
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        // Initialize NaverMap
        this.naverMap = naverMap;

        // Check if permission is granted and enable location tracking
        if (isPermissionGranted) {
            naverMap.setLocationSource(locationSource);
            naverMap.getUiSettings().setLocationButtonEnabled(true);
            startLocationUpdates();
        }
    }
    */

    /*
    private void checkLocationPermission() {
        // Check if fine location and coarse location permissions are granted
        boolean fineLocationGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        boolean coarseLocationGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        // Request location permissions if not granted
        if (!fineLocationGranted && !coarseLocationGranted) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    1001
            );
        } else {
            // Permissions already granted
            isPermissionGranted = true;
            naverMap.setLocationSource(locationSource);
            naverMap.getUiSettings().setLocationButtonEnabled(true);
            startLocationUpdates();
        }
    }*/
    /*
    private void startLocationUpdates() {
        if (isPermissionGranted) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void checkProximityToConvenienceStore(Location currentLocation) {
        // Calculate distance between current location and convenience store (Example: 50 meters)
//        double convenienceStoreLatitude;
        LatLng convenienceStoreLocation = new LatLng(convenienceStoreLatitude, convenienceStoreLongitude);
        double distanceInMeters = currentLocation.distanceTo(new Location("ConvenienceStoreLocation") {
            {
                setLatitude(convenienceStoreLocation.latitude);
                setLongitude(convenienceStoreLocation.longitude);
            }
        });

        if (distanceInMeters <= 50) {
            // User is within 50 meters of the convenience store
            isNearConvenienceStore = true;
            convenienceStoreMarker.setIconTintColor(getResources().getColor(R.color.red));
        }
    }
    */
    private void performReturn() {
        // Check if the umbrella ID exists in the Firebase Realtime Database
        // If it exists, update the rentalStatus to "X"
        // Display a message indicating that the return is complete
        // Return to the MainActivity
    }
}

/*
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.nanumy.sharumbrella.R;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.util.FusedLocationSource;

public class ReturnMap extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private NaverMap naverMap;
    private FusedLocationSource locationSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_map);

        // 위치 권한 확인 및 요청
        if (checkLocationPermission()) {
            initMap();
        }
    }

    private boolean checkLocationPermission() {
        boolean fineLocationGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        boolean coarseLocationGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        if (!fineLocationGranted || !coarseLocationGranted) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE
            );
            return false;
        } else {
            return true;
        }
    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initMap();
            } else {
                Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;

        // 사용자의 위치 트래킹 활성화
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        // 지도 초기 위치 설정 (예시 위치: 서울역)
        CameraPosition cameraPosition = new CameraPosition(
                new LatLng(37.5666102, 126.9783881),
                16.0
        );
        naverMap.setCameraPosition(cameraPosition);
    }
}
*/