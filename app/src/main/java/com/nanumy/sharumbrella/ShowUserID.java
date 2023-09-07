package com.nanumy.sharumbrella;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.Intent;

public class ShowUserID extends AppCompatActivity {
    private TextView etID;
    private ImageButton mBtnBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_id);

        etID = findViewById(R.id.inputname_b);

        Intent intent = getIntent();
        String foundEmail = intent.getStringExtra("foundEmail");
        // TextView에 이메일 정보 설정
        etID.setText("Found Email: " + foundEmail);

        // "Check" 버튼 처리
        mBtnBackground = findViewById(R.id.background);
        mBtnBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MapActivity 화면으로 돌아가기
                Intent mapIntent = new Intent(ShowUserID.this, MapActivity.class);
                startActivity(mapIntent);
                finish(); // 현재 액티비티 종료
            }
        });
    }
}