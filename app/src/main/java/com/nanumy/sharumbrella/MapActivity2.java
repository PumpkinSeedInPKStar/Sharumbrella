package com.nanumy.sharumbrella;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class MapActivity2 extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private EditText mEtId, mEtPwd, mEtchPwd, mEtUserName;
    private ImageButton mBTnAuthCheck, mBTnCancle, mBTnRegister, mBTnSocialLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Sharumbrella");

        mEtId = findViewById(R.id.email);
        mEtPwd = findViewById(R.id.mEtPwd);
        mEtchPwd = findViewById(R.id.chPwd);
        mEtUserName = findViewById(R.id.UserName);

        mBTnRegister = findViewById(R.id.background);
        mBTnCancle = findViewById(R.id.Cancel_button);
        mBTnSocialLogin = findViewById(R.id.social);

        mBTnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = mEtId.getText().toString();
                String strPwd = mEtPwd.getText().toString();
                String strChPwd = mEtchPwd.getText().toString();
                String strUserName = mEtUserName.getText().toString();

                if (strPwd.equals(strChPwd)) {
                    // 이메일 중복 확인 후 인증 메일 보내기
                    // mBTnRegister을 '인증 및 회원가입'으로 변경하기.
                    // 그리고 mBTnAuthCheck은 지우기
                    checkAndSendEmailVerification(strEmail, strPwd, strUserName);
                } else {
                    Toast.makeText(MapActivity2.this, "비밀번호가 일치하지 않습니다. 다시 입력해주세요.", Toast.LENGTH_LONG).show();
                }
            }
        });

        mBTnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 취소 버튼을 누르면 이전 페이지로 넘어감
                Intent intent = new Intent(MapActivity2.this, MapActivity.class);
                startActivity(intent);
            }
        });

        mBTnSocialLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '소셜 로그린으로 회원 가입' 누르면 소셜 로그인 회원가입 창으로 넘어감
                Intent intent = new Intent(MapActivity2.this, join_social_login.class);
                startActivity(intent);
            }
        });

        /*
        mBTnAuthCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = mEtId.getText().toString();
                // '인증 하기' 버튼을 누르면, 해당 이메일로 인증이 진행됩니다.
                // 이 부분은 필요에 따라 이메일 인증 링크를 보내는 기능을 추가하면 됩니다.
                // 앞서 설명드린 'sendEmailVerification()' 함수를 사용하면 됩니다.
                checkAndSendEmailVerification(strEmail, strPwd, strUserName);
                Toast.makeText(MapActivity2.this, "인증 메일을 전송했습니다. 이메일을 확인하세요.", Toast.LENGTH_SHORT).show();
            }
        });
        */
    }

    private void checkAndSendEmailVerification(String email, String password, String userName) {
        mDatabaseRef.child("UserAccount").orderByChild("emailId").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(MapActivity2.this, "이미 존재하는 이메일입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            // 이메일 중복 없음, 이메일 인증 메일 보내기
                            sendEmailVerification(email, password, userName);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(MapActivity2.this, "오류 발생: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendEmailVerification(final String email, final String password, final String userName) {
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(MapActivity2.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MapActivity2.this, "인증 메일을 " + email + " 주소로 보냈습니다. 이메일을 확인해주십시오.", Toast.LENGTH_SHORT).show();
                                        // 회원 가입 완료 후 다음 화면으로 이동
                                        completeRegistration(email, password, userName);
                                    } else {
                                        Toast.makeText(MapActivity2.this, "이메일 인증 메일 보내기 실패", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(MapActivity2.this, "계정 생성 실패", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MapActivity2.this, MapActivity2.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    private void completeRegistration(String email, String password, String userName) {
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            // 이 부분에서 추가적인 사용자 정보를 저장할 수 있습니다.
            // 예를 들어, 이름을 저장하는 코드는 다음과 같을 수 있습니다.
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(userName)
                    .build();

            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MapActivity2.this, "회원이 되신 것을 축하합니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // 추가 정보를 저장한 뒤, 다음 화면으로 이동하면 됩니다.
            Intent intent = new Intent(MapActivity2.this, MapActivity.class);
            startActivity(intent);
        }
    }
}
