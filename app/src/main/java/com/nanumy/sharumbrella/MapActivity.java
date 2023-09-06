package com.nanumy.sharumbrella;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// LoginActivity 대체
public class MapActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth; // 파이어 베이스 인증
    private DatabaseReference mDatabaseRef; //실시간 데이터 베이스
    private EditText mEtEmail, mEtPwd;
    private ImageButton mBtnFindId, mBtnFindPassword, mBtnMembership, mBtnSocialLogin, mBtnLogin;
    // mBtnFindId(아이디 찾기), mBtnFindPassword(비밀번호 찾기), mBtnMembership(회원 가입), mBtnSocialLogin(소셜 로그인),mBtnLogin(로그인)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mEtEmail = findViewById(R.id.id_email);
        mEtPwd = findViewById(R.id.ps);
        mBtnFindId = findViewById(R.id.find_id);
        mBtnFindPassword = findViewById(R.id.find_ps);
        mBtnMembership = findViewById(R.id.membership);
        mBtnSocialLogin = findViewById(R.id.social_login);
        mBtnLogin = findViewById(R.id.login_button);

        // '로그인' 버튼을 눌렀을 때
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(MapActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            // 로그인 성공
                            Intent intent = new Intent(MapActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(MapActivity.this, "로그인 실패", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MapActivity.this, MapActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        mBtnMembership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '회원 가입' 버튼을 눌렀을 때, 회원가입 화면으로 넘어간다
                Intent intent = new Intent(MapActivity.this, MapActivity2.class);
                startActivity(intent);
            }
        });
        mBtnFindId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '아이디 찾기' 버튼을 눌렀을 때, 아이디 찾기 화면으로 넘어간다
                Intent intent = new Intent(MapActivity.this, FindID_phone.class);
                startActivity(intent);
            }
        });
        mBtnFindPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '비밀번호 찾기' 버튼을 눌렀을 때, 비밀번호 찾기 화면으로 넘어감
                Intent intent = new Intent(MapActivity.this, FindPassword_phone.class);
                startActivity(intent);
            }
        });
        mBtnSocialLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '소셜 로그인' 버튼을 눌렀을 때, 소셜 로그인 화면으로 넘어감
                Intent intent = new Intent(MapActivity.this, social_login.class);
                startActivity(intent);
            }
        });
    }
}