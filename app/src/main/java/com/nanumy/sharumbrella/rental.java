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

public class rental extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private ImageButton mBtnRrcheck, mBtnSome_id;
    private EditText etIdqrenter;
    private TextView tvRentalplacespace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Sharumbrella");

        mBtnRrcheck = findViewById(R.id.rrcheck);
        mBtnSome_id = findViewById(R.id.some_id);
        etIdqrenter = findViewById(R.id.idqrenter);
        tvRentalplacespace = findViewById(R.id.rentalplacespace);

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
                                // 대여 가능한 상태일 때
                                tvRentalplacespace.setText(umbrellaAddress);

                                // 대여 상태를 "O"로 변경
                                umbrellaRef.child("rentalStatus").setValue("O");
                                Toast.makeText(rental.this, "대여하셨습니다.", Toast.LENGTH_LONG).show();
                                // mainActivity 화면으로 이동
                                Intent intent = new Intent(rental.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                // 이미 대여 중일 때
                                Toast.makeText(rental.this, "이 우산은 이미 대여 중입니다. 다른 우산을 이용해 주세요.", Toast.LENGTH_LONG).show();
//                                tvRentalplacespace.setText("이 우산은 이미 대여 중입니다.");
                            }
                        } else {
                            // 해당 우산 ID가 존재하지 않을 때
                            Toast.makeText(rental.this, "해당 우산 ID가 존재하지 않습니다. 다시 입력해주세요.", Toast.LENGTH_LONG).show();
//                            tvRentalplacespace.setText("해당 우산 ID가 존재하지 않습니다.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // 에러 처리
                        tvRentalplacespace.setText("우산 정보 가져오기 실패: " + databaseError.getMessage());
                    }
                });
            }
        });
    }
}