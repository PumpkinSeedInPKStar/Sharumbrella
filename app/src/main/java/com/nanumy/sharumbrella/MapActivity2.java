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

import java.util.HashMap;
import java.util.Map;

public class MapActivity2 extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private EditText mEtId, mEtPwd, mEtchPwd, mEtUserName;
    private ImageButton mBTnCancle, mBTnRegister, mBTnSocialLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

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

                if (strPwd.equals(strChPwd)) { // 비밀번호를 둘다 제대로 작성했는지
                    // 이메일 중복 확인 후 인증 메일 보내기
                    // mBTnRegister을 '인증 및 회원가입'으로 변경하기.
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
                // '소셜 로그인으로 회원 가입' 누르면 소셜 로그인 회원가입 창으로 넘어감
                Intent intent = new Intent(MapActivity2.this, join_social_login.class);
                startActivity(intent);
            }
        });
    }

    private void checkAndSendEmailVerification(String email, String password, String userName) {
        mDatabaseRef.child("UserAccount").orderByChild("email").equalTo(email)
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
                            String IdToken = firebaseUser.getUid();

                            // 사용자 정보를 저장할 데이터베이스 레퍼런스 가져오기
                            DatabaseReference userRef = mDatabaseRef.child("UserAccount").child(IdToken);
                            // 사용자 정보를 Map 또는 객체로 만들어서 저장
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("email", email);
                            userInfo.put("password", password);
                            userInfo.put("name", userName);

                            // 사용자 정보를 Firebase Realtime Database에 저장
                            userRef.setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Toast.makeText(MapActivity2.this, "회원 정보를 저장했습니다.", Toast.LENGTH_SHORT).show();
                                        firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(MapActivity2.this, "인증 메일을 " + email + " 주소로 보냈습니다. 이메일을 확인해주십시오.", Toast.LENGTH_SHORT).show();
                                                    // 회원 가입 완료 후 다음 화면으로 이동
//                                        completeRegistration(email, password, userName);
                                                    // 추가 정보를 저장한 뒤, 다음 화면으로 이동하면 됩니다.
                                                    Intent intent = new Intent(MapActivity2.this, MapActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(MapActivity2.this, "이메일 인증 메일 보내기 실패", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                        // 회원 가입 완료 후 다음 화면으로 이동
//                                        completeRegistration(email, password, userName);
                                    } else {
                                        Toast.makeText(MapActivity2.this, "회원 정보 저장 실패", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

//                            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Toast.makeText(MapActivity2.this, "인증 메일을 " + email + " 주소로 보냈습니다. 이메일을 확인해주십시오.", Toast.LENGTH_SHORT).show();
//                                        // 회원 가입 완료 후 다음 화면으로 이동
////                                        completeRegistration(email, password, userName);
//                                        // 추가 정보를 저장한 뒤, 다음 화면으로 이동하면 됩니다.
//                                        Intent intent = new Intent(MapActivity2.this, MapActivity.class);
//                                        startActivity(intent);
//                                    } else {
//                                        Toast.makeText(MapActivity2.this, "이메일 인증 메일 보내기 실패", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
                        } else {
                            Toast.makeText(MapActivity2.this, "계정 생성 실패.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MapActivity2.this, MapActivity2.class);
                            startActivity(intent);
                        }
                    }
                });
    }
    /*
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
    }*/
}
