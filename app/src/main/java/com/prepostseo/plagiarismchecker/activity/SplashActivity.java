package com.prepostseo.plagiarismchecker.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Hassan on 9/25/17.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent;
        if(!checkLoginStatus()) {
            // Start home activity
            intent = new Intent(SplashActivity.this, PublicActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else{
            intent = new Intent(SplashActivity.this, MainDrawerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        // close splash activity
        finish();
    }

    public boolean checkLoginStatus(){
        SharedPreferences shared = getSharedPreferences("com.prepostseo.plagiarismchecker", MODE_PRIVATE);
        String api_key = (shared.getString("api_key", ""));
        if(api_key != null && !api_key.equalsIgnoreCase(""))
            return true;
        else
            return false;
    }
}
