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

public class FindID_phone extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private ImageButton mBtnBackground;
    private EditText eEmail, eName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id_phone);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Sharumbrella");

        mBtnBackground = findViewById(R.id.background);
        eEmail = findViewById(R.id.InputPhoneNum_box);
        eName = findViewById(R.id.inputname_b);

        // 데이터베이스 레퍼런스
        DatabaseReference userAccountRef = mDatabaseRef.child("UserAccount");

        mBtnBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = eEmail.getText().toString();
                String strName = eName.getText().toString();

                // ValueEventListener를 사용하여 데이터베이스에서 데이터를 읽어옴
                userAccountRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean userFound = false;
                        String userEmail = "";

                        // 모든 사용자에 대한 루프
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            DataSnapshot emailSnapshot = userSnapshot.child("email");
                            DataSnapshot nameSnapshot = userSnapshot.child("name");

                            if (emailSnapshot.exists() && nameSnapshot.exists()) {
                                String email = emailSnapshot.getValue(String.class);
                                String name = nameSnapshot.getValue(String.class);

                                // 이메일과 이름이 모두 일치하는 사용자를 찾음
                                if (email != null && name != null &&
                                        email.equalsIgnoreCase(strEmail) && name.equalsIgnoreCase(strName)) {
                                    userFound = true;
                                    userEmail = email;
                                    break; // 일치하는 사용자를 찾았으므로 루프를 종료
                                }
                            }
                        }

                        if (userFound) {
                            // 사용자가 찾아졌을 때 새로운 화면(FindID)로 전환하여 이메일을 보여줌
                            Intent intent = new Intent(FindID_phone.this, ShowUserID.class);
                            intent.putExtra("email", userEmail);
                            startActivity(intent);
                        } else {
                            // 사용자를 찾지 못했을 때 메시지 표시
                            Toast.makeText(FindID_phone.this, "일치하는 사용자를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // 에러 처리 (예: 데이터베이스 읽기 실패)
                        Toast.makeText(FindID_phone.this, "데이터베이스 오류: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}