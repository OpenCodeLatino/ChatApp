package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.chatapp.Login.LoginActivity;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        EasySplashScreen config = new EasySplashScreen(SplashScreenActivity.this    )
                .withTargetActivity(LoginActivity.class)
                .withLogo(R.drawable.chat)
                .withHeaderText("ChatRoom Til House of Code");

        //Set Text Color
        config.getHeaderTextView().setTextColor(Color.WHITE);

        //Set to view
        View view = config.create();

        //Set view to content view
        setContentView(view);
    }
}
