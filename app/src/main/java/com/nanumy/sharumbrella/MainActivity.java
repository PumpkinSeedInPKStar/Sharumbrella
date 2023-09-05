package com.nanumy.sharumbrella;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private ImageButton mBtnRental, mBtnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnRental = findViewById(R.id.rental);
        mBtnReturn = findViewById(R.id.return1);

        mBtnRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 'Rental' 버튼 누르면 rental 화면으로 넘어감
                Intent intent = new Intent(MainActivity.this, rental.class);
                startActivity(intent);
            }
        });
        mBtnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 'Return' 버튼 누르면 return 화면으로 넘어감
                Intent intent = new Intent(MainActivity.this, return1.class);
                startActivity(intent);
            }
        });

    }
}