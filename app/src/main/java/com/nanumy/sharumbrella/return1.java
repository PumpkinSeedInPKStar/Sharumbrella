package com.nanumy.sharumbrella;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class return1 extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private ImageButton mBtnRrcheck, mBtnSome_id;
    private EditText etIdqrenter;
    private TextView tvReturnplacespace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return1);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Sharumbrella");

        mBtnRrcheck = findViewById(R.id.rrcheck);
        mBtnSome_id = findViewById(R.id.some_id);
        etIdqrenter = findViewById(R.id.idqrenter);
        tvReturnplacespace = findViewById(R.id.rentalplacespace);

        mBtnRrcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EditText에서 우산 ID를 가져옴
                final String umbrellaId = etIdqrenter.getText().toString().trim();

                // Firebase 실시간 데이터베이스의 'umbrellas' 경로에서 해당 우산 정보를 가져옴
                DatabaseReference umbrellaRef = FirebaseDatabase.getInstance().getReference("umbrellas").child(umbrellaId);

                umbrellaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // 해당 우산 정보가 존재할 때
                            String rentalStatus = dataSnapshot.child("rentalStatus").getValue(String.class);
                            String umbrellaAddress = dataSnapshot.child("Location").getValue(String.class);

                            if ("X".equals(rentalStatus)) {
                                // 반납된 상태일 때
                                Toast.makeText(return1.this, "이 우산은 이미 반납된 상태입니다.", Toast.LENGTH_LONG).show();
//                                tvReturnplacespace.setText("이미 반납된 상태입니다.");
                            } else if ("O".equals(rentalStatus)) {
                                // 대여 중인 상태일 때
                                // 여기서 네이버 지도 화면으로 이동하는 코드를 작성해야 합니다.
                                // 네이버 지도 화면에서 편의점 위치를 찾고 반납 처리를 완료해야 합니다.

                                // 네이버 지도 화면으로 이동
                                Intent intent = new Intent(return1.this, ReturnMap2.class);
                                // 네이버 지도 화면으로 우산 ID와 주소를 전달할 수 있도록 인텐트에 데이터 추가
                                intent.putExtra("umbrellaId", umbrellaId);
                                intent.putExtra("umbrellaAddress", umbrellaAddress);
                                startActivity(intent);
                            }
                        } else {
                            // 해당 우산 ID가 존재하지 않을 때
                            Toast.makeText(return1.this, "해당 우산 ID가 존재하지 않습니다. 다시 입력해주세요.", Toast.LENGTH_LONG).show();
//                            returnPlaceTextView.setText("해당 우산 ID가 존재하지 않습니다.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // 에러 처리
                        Toast.makeText(return1.this, "우산 정보 가져오기 실패: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
//                        returnPlaceTextView.setText("우산 정보 가져오기 실패: " + databaseError.getMessage());
                    }
                });
            }
        });
    }
}