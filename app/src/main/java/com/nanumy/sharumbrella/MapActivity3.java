package com.nanumy.sharumbrella;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


/*
* 사용자 계정 정보 모델 클래스
* */
// UserAccount Activity
public class MapActivity3 {
    private String idToken; // firebase Uid (고유 토큰정보)
    private String emailId; // 이메일 아이디
    private String password;// 비밀번호
    private String UserName;// 유저 이름

    public MapActivity3(){}
    public MapActivity3(String emailId, String password, String UserName) {
        this.emailId = emailId;
        this.password = password;
        this.UserName = UserName;
    }

    public String getIdToken(){return idToken;}
    public void setIdToken(String idToken){this.idToken = idToken;}
    public String getEmailId(){return emailId;}
    public void setEmailId(String emailId){this.emailId = emailId;}
    public String getPassword(){return password;}
    public void setPassword(String password){this.password = password;}
    public String getUserName(){return UserName;}
    public void setUserName(String UserName){this.UserName = UserName;}
}