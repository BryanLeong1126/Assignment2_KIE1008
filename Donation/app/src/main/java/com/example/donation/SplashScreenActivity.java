package com.example.donation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView donationLogo;
    private TextView frontTitle, introQuotes, signInOption;
    private Button donorButton, recipientButton;

    Animation topAnimation, bottomAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        donationLogo = findViewById(R.id.donationLogo);
        frontTitle = findViewById(R.id.frontTitle);
        donorButton = findViewById(R.id.donorButton);
        recipientButton = findViewById(R.id.recipientButton);
        signInOption = findViewById(R.id.signInOption);
        introQuotes = findViewById(R.id.introQuotes);

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        donationLogo.setAnimation(topAnimation);
        frontTitle.setAnimation(bottomAnimation);
        donorButton.setAnimation(bottomAnimation);
        recipientButton.setAnimation(bottomAnimation);
        introQuotes.setAnimation(bottomAnimation);

        donorButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(SplashScreenActivity.this, DonorInfoDetailsActivity.class);
                startActivity(intent);
            }
        });

        recipientButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(SplashScreenActivity.this, InfoDetailsActivity.class);
                startActivity(intent);
            }
        });

        signInOption.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(SplashScreenActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }
}