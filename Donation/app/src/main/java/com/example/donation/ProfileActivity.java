package com.example.donation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar profileToolbar;
    private TextView profileType, profileName, profileEmail, profileIdNumber, profilePhone, profileArea;
    private CircleImageView profileImage;
    private Button profileBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileToolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(profileToolbar);
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        profileType = findViewById(R.id.profileType);
        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profileIdNumber = findViewById(R.id.profileIdNumber);
        profilePhone = findViewById(R.id.profilePhone);
        profileArea = findViewById(R.id.profileArea);
        profileImage = findViewById(R.id.profileImage);
        profileBackButton = findViewById(R.id.profileBackButton);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    profileType.setText(snapshot.child("type").getValue().toString());
                    profileName.setText(snapshot.child("name").getValue().toString());
                    profileIdNumber.setText(snapshot.child("id number").getValue().toString());
                    profilePhone.setText(snapshot.child("phone number").getValue().toString());
                    profileArea.setText(snapshot.child("living area").getValue().toString());
                    profileEmail.setText(snapshot.child("email").getValue().toString());
                    Glide.with(getApplicationContext()).load(snapshot.child("profile picture url").getValue().toString()).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        profileBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}