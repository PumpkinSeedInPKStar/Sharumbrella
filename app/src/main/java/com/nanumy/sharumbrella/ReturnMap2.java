package com.nanumy.sharumbrella;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class ReturnMap2 extends AppCompatActivity implements OnMapReadyCallback {

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private NaverMap naverMap;
    private Marker nearestConvenienceStoreMarker;
    private Button okButton;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private static final String apiKey= "dsajemc7rb";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_map);

        // Retrofit 설정
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://openapi.naver.com") // 네이버 API 엔드포인트
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Retrofit으로 API 서비스 생성
        NaverMapApiService apiService = retrofit.create(NaverMapApiService.class);
        // API 호출
        Call<MapResponse> call = apiService.getMapData("편의점", apiKey);


        // 지도를 초기화하고 띄우기
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        // 위치 액세스 권한 확인 및 요청
        checkLocationPermission();

        // OK 버튼 초기화
        okButton = findViewById(R.id.button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                performReturn();
                // 이전 페이지에서 전달된 우산 ID (예: U001)
                String umbrellaId = getIntent().getStringExtra("umbrellaId");
                // 파이어베이스 데이터베이스 레퍼런스
                DatabaseReference umbrellasRef = FirebaseDatabase.getInstance().getReference("umbrellas");

                // 해당 우산 ID의 rentalStatus를 "X"로 업데이트
                umbrellasRef.child(umbrellaId).child("rentalStatus").setValue("X")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // 업데이트가 성공한 경우
//                                    showReturnStatus(true); // 반납 가능 메시지 표시
//                                    Toast.makeText(this, "반납하였습니다.", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(ReturnMap2.this, "반납하였습니다.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(ReturnMap2.this, MainActivity.class);
                                    // 네이버 지도 화면으로 우산 ID와 주소를 전달할 수 있도록 인텐트에 데이터 추가
                                    startActivity(intent);
                                } else {
                                    // 업데이트가 실패한 경우
                                    Toast.makeText(ReturnMap2.this, "반납을 실패하였습니다. 다시 시도해주십시오.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(ReturnMap2.this, return1.class);
                                    // 네이버 지도 화면으로 우산 ID와 주소를 전달할 수 있도록 인텐트에 데이터 추가
                                    startActivity(intent);
//                                    showReturnStatus(false); // 반납 불가능 메시지 표시
                                }
                            }
                        });
            }
        });

        // 위치 업데이트 요청 초기화
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest
                .create()
                .setInterval(10000) // 위치 업데이트 간격 (10초)
                .setFastestInterval(5000) // 가장 빠른 위치 업데이트 간격 (5초)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // 위치 정보가 업데이트될 때마다 지도 위의 파란색 마커를 업데이트
                    updateCurrentLocationMarker(new LatLng(location.getLatitude(), location.getLongitude()));
                }
            }
        };
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;

        // 지도 줌 레벨 및 위치 설정
        naverMap.setMinZoom(10.0);
        naverMap.setMaxZoom(18.0);
        naverMap.moveCamera(CameraUpdate.zoomTo(15.0));

        // 사용자의 현재 위치를 실시간으로 받아옴
        startLocationUpdates();
    }
    /*
    private void checkLocationPermission() {
        // 위치 권한 확인
        int fineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (fineLocationPermission != PackageManager.PERMISSION_GRANTED || coarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
            // 위치 권한이 없으면 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            // 위치 권한이 있는 경우 위치 업데이트 시작
            startLocationUpdates();
        }
    }
     */

    /*
    // 위치 권한 확인 메서드
private boolean checkLocationPermission() {
    boolean coarseLocationGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED;

    boolean fineLocationGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED;

    return coarseLocationGranted && fineLocationGranted;
}*/
    // 위치 권한 확인 메서드
    private boolean checkLocationPermission() {
        boolean coarseLocationGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        boolean fineLocationGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        return coarseLocationGranted && fineLocationGranted;
    }

    private void startLocationUpdates() {
        // 위치 권한 확인
        if (checkLocationPermission()) {
            // 위치 업데이트 요청
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            // 권한을 요청하지 않았거나 사용자가 권한을 거부한 경우
            // 권한 요청 로직 또는 권한 거부 시 메시지 표시 로직을 추가하세요.
            // 위치 권한을 다시 요청하는 코드
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void updateCurrentLocationMarker(LatLng latLng) {
        // 파란색 마커로 현재 위치 표시
        // 여기에 추가적인 작업을 수행하려면 수정 가능
        // 파란색 마커로 현재 위치 표시
        if (naverMap != null) {
            Marker currentLocationMarker = new Marker();
            currentLocationMarker.setPosition(latLng); // 현재 위치 설정
            currentLocationMarker.setIcon(OverlayImage.fromResource(R.drawable.blue_marker)); // 파란색 마커 아이콘 설정
            currentLocationMarker.setMap(naverMap); // 지도에 마커 추가
        }
    }

    private void performReturn() {
        // 여기에서 반납 작업을 수행
        // 현재 위치와 가장 가까운 편의점을 찾아야 함
        // 파이어베이스에서 데이터를 가져와서 반납 여부를 확인하고 업데이트
        // 반납이 완료되었음을 알리는 알람 표시
    }

    public interface NaverMapApiService {
        @GET("/v1/map")
        Call<MapResponse> getMapData(
                @Query("query") String query,
                @Query("apikey") String apiKey
        );
    }
    // 사용자의 현재 위치와 가장 가까운 편의점을 찾는 함수
    private void getNearestConvenienceStore(LatLng currentLocation) {
        // 여기에 가장 가까운 편의점을 찾는 로직을 구현하세요.
        // 파이어베이스 데이터베이스 또는 다른 데이터 원본을 사용하여
        // 현재 위치에서 가장 가까운 편의점을 찾아야 합니다.
        // 그리고 그 편의점의 위치를 사용하여 지도에 마커를 추가하세요.
    }

    // 반납 가능 여부를 확인하고 업데이트하는 함수
    private void checkReturnEligibility(LatLng convenienceStoreLocation) {
        // 여기에 반납 가능 여부를 확인하고 업데이트하는 로직을 구현하세요.
        // 예를 들어, 해당 편의점의 재고 상태를 파이어베이스에서 확인하고
        // 반납 가능한지 여부를 판단한 후 업데이트하세요.
    }

    // 반납 상태를 사용자에게 표시하는 함수
    private void showReturnStatus(boolean isReturnPossible) {
        // 여기에서 반납 가능 여부에 따라 사용자에게 메시지를 표시하세요.
        if (isReturnPossible) {
            Toast.makeText(this, "반납 가능합니다.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "반납 불가능합니다.", Toast.LENGTH_SHORT).show();
        }
    }
}