package com.example.donation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoDetailsActivity extends AppCompatActivity {

    private CircleImageView recipientProfileImage;
    private TextInputEditText recipientFullName, recipientIdNumber, recipientPhoneNumber, recipientEmergencyNumber, recipientEmail;
    private Button recipientContinueButton;

    String[] areas = {"Perlis","Kedah","Kelantan","Terengganu","Penang","Perak","Pahang","Selangor","Negeri Sembilan","Malacca","Johor","Sabah","Sarawak"};
    AutoCompleteTextView recipientAREA, recipientArea;
    ArrayAdapter<String> adapterAreas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_details);

        recipientAREA = findViewById(R.id.recipientArea);

        adapterAreas = new ArrayAdapter<String>(this,R.layout.list_area, areas);
        recipientAREA.setAdapter(adapterAreas);

        recipientAREA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String area = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(),"Area: "+area, Toast.LENGTH_SHORT).show();
            }
        });
    }
}