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

public class SignInActivity extends AppCompatActivity {

    private ImageView signInLogo;
    private TextView signInTitle, signInEmail, signInPassword, signUpOption;
    private Button signInButton;

    Animation topAnimation, bottomAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in);

        signInLogo = findViewById(R.id.signInLogo);
        signInTitle = findViewById(R.id.signInTitle);
        signInEmail = findViewById(R.id.signInEmail);
        signInPassword = findViewById(R.id.signInPassword);
        signInButton = findViewById(R.id.signInButton);
        signUpOption = findViewById(R.id.signUpOption);

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        signInLogo.setAnimation(topAnimation);
        signInTitle.setAnimation(bottomAnimation);
        signInEmail.setAnimation(bottomAnimation);
        signInPassword.setAnimation(bottomAnimation);
        signInButton.setAnimation(bottomAnimation);
        signUpOption.setAnimation(bottomAnimation);

        signInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        signUpOption.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(SignInActivity.this, SplashScreenActivity.class);
                startActivity(intent);
            }
        });
    }
}