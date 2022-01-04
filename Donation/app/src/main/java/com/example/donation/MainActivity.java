package com.example.donation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private CircleImageView navigationUserImage;
    private TextView navigationUserName, navigationUserEmail,navigationUserPhone, navigationUserArea, navigationUserType;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Donation");
        navigationView = findViewById(R.id.navigationView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationUserImage = navigationView.getHeaderView(0).findViewById(R.id.navigationUserImage);
        navigationUserName = navigationView.getHeaderView(0).findViewById(R.id.navigationUserName);
        navigationUserEmail = navigationView.getHeaderView(0).findViewById(R.id.navigationUserEmail);
        navigationUserPhone = navigationView.getHeaderView(0).findViewById(R.id.navigationUserPhone);
        navigationUserArea = navigationView.getHeaderView(0).findViewById(R.id.navigationUserArea);
        navigationUserType = navigationView.getHeaderView(0).findViewById(R.id.navigationUserType);

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String name = snapshot.child("name").getValue().toString();
                    navigationUserName.setText(name);
                    String email = snapshot.child("email").getValue().toString();
                    navigationUserEmail.setText(email);
                    String phone = snapshot.child("phone number").getValue().toString();
                    navigationUserPhone.setText(phone);
                    String area = snapshot.child("living area").getValue().toString();
                    navigationUserArea.setText(area);
                    String type = snapshot.child("type").getValue().toString();
                    navigationUserType.setText(type);

                    if(snapshot.hasChild("profile picture url")){
                        String imageUrl = snapshot.child("profile picture url").getValue().toString();
                        Glide.with(getApplicationContext()).load(imageUrl).into(navigationUserImage);
                    }
                    else{
                        navigationUserImage.setImageResource(R.drawable.profileimage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile:
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                Intent logout = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(logout);
                finish();
                return true;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}