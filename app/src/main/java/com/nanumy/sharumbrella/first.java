package com.nanumy.sharumbrella;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class first extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        // 1초(1000ms) 후에 MapActivity로 전환
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // MapActivity로 전환
                Intent intent = new Intent(first.this, MapActivity.class);
                startActivity(intent);

                // 현재 액티비티를 종료
                finish();
            }
        }, 2300); // 1000ms = 1초

    }
}