package com.nanumy.sharumbrella;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


/*
 * 우산 정보 모델 클래스: { 우산 아이디 | 대여 여부 | 위치 }
 * */

public class DatabaseUmbrella {
    private String UmbID, UmbOX, UmbLocation; // 우산 아이디, 대여 여부, 위치
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("server/saving-data/fireblog");

    DatabaseReference umbrellasRef = ref.child("Umbrellas");
    Map<String, DatabaseUmbrella> umbrellas = new HashMap<>();
    public DatabaseUmbrella(){}
    public DatabaseUmbrella(String UmbID, String UmbOX, String UmbLocation){
        this.UmbID = UmbID;
        this.UmbOX = UmbOX;
        this.UmbLocation = UmbLocation;
    }

    public String getUmbID(){return UmbID;}
    public void setUmbID(String UmbID){this.UmbID = UmbID;}

    public String getUmbOX(){return UmbOX;}
    public void setUmbOX(String UmbOX){this.UmbOX = UmbOX;}

    public String getUmbLocation(){return UmbLocation;}
    public void setUmbLocation(String UmbLocation){this.UmbLocation = UmbLocation;}
}