package com.nanumy.sharumbrella;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class social_login extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private ImageButton mBtnGooglejoin, mBtnFacebookjoin, mBtnKakaojoin, mBtnBacktojoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Sharumbrella");

        mBtnGooglejoin = findViewById(R.id.googlelogin);
        mBtnFacebookjoin = findViewById(R.id.facebooklogin);
        mBtnKakaojoin = findViewById(R.id.kakaologin);
        mBtnBacktojoin = findViewById(R.id.backtologin);

        mBtnBacktojoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(social_login.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }
}