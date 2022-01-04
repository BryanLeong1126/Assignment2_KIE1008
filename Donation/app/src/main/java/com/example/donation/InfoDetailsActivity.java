package com.example.donation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoDetailsActivity extends AppCompatActivity {

    private CircleImageView recipientProfileImage;
    private TextInputEditText recipientFullName, recipientIdNumber, recipientPhoneNumber, recipientEmergencyNumber, recipientEmail, recipientPassword;
    private Button recipientContinueButton;
    private TextView recipientSignInOption;
    private Uri resultUri;
    private ProgressDialog loader;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;

    String[] areas = {"Perlis","Kedah","Kelantan","Terengganu","Penang","Perak","Pahang","Selangor","Negeri Sembilan","Malacca","Johor","Sabah","Sarawak"};
    AutoCompleteTextView recipientAREA;
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
        recipientProfileImage = findViewById(R.id.recipientProfileImage);
        recipientFullName = findViewById(R.id.recipientFullName);
        recipientIdNumber = findViewById(R.id.recipientIdNumber);
        recipientPhoneNumber = findViewById(R.id.recipientPhoneNumber);
        recipientEmergencyNumber = findViewById(R.id.recipientEmergencyNumber);
        recipientEmail = findViewById(R.id.recipientEmail);
        recipientPassword = findViewById(R.id.recipientPassword);
        recipientContinueButton = findViewById(R.id.recipientContinueButton);
        recipientSignInOption = findViewById(R.id.recipientSignInOption);
        loader = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        recipientProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        recipientContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = recipientEmail.getText().toString().trim();
                final String password = recipientPassword.getText().toString().trim();
                final String fullName = recipientFullName.getText().toString().trim();
                final String idNumber = recipientIdNumber.getText().toString().trim();
                final String phoneNumber = recipientPhoneNumber.getText().toString().trim();
                final String emergencyNumber = recipientEmergencyNumber.getText().toString().trim();
                final String areas = recipientAREA.getText().toString();

                if(TextUtils.isEmpty(email)) {
                    recipientEmail.setError("Email is required");
                    return; }
                if(TextUtils.isEmpty(password)){
                    recipientPassword.setError("Password is required");
                    return; }
                if(TextUtils.isEmpty(fullName)) {
                    recipientFullName.setError("Full name is required");
                    return; }
                if(TextUtils.isEmpty(idNumber)) {
                    recipientIdNumber.setError("Id number is required");
                    return; }
                if(TextUtils.isEmpty(phoneNumber)) {
                    recipientPhoneNumber.setError("Phone number is required");
                    return; }
                if(TextUtils.isEmpty(emergencyNumber)) {
                    recipientEmergencyNumber.setError("Emergency number is required");
                    return; }
                if(TextUtils.isEmpty(areas)) {
                    recipientAREA.setError("Living area is required");
                    //Toast.makeText(DonorInfoDetailsActivity.this,"Living area is required", Toast.LENGTH_SHORT).show();
                    return; }
                else {
                    loader.setMessage("Registering your informartion...");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                String error = task.getException().toString();
                                Toast.makeText(InfoDetailsActivity.this, "Error"+error, Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String currentUserId = mAuth.getCurrentUser().getUid();
                                userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);
                                HashMap userInfo = new HashMap();
                                userInfo.put("id", currentUserId);
                                userInfo.put("type", "recipient");
                                userInfo.put("email", email);
                                userInfo.put("password", password);
                                userInfo.put("name", fullName);
                                userInfo.put("id number", idNumber);
                                userInfo.put("phone number", phoneNumber);
                                userInfo.put("emergency number", emergencyNumber);
                                userInfo.put("living area", areas);
                                userInfo.put("search", "recipient"+areas);

                                userDatabaseRef.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(InfoDetailsActivity.this,"Data set successfully",Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(InfoDetailsActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                        finish();
                                        //loader.dismiss();
                                    }
                                });

                                if(resultUri!=null){
                                    final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile images").child(currentUserId);
                                    Bitmap bitmap = null;

                                    try{ bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),resultUri); }
                                    catch (IOException e){ e.printStackTrace(); }
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                                    byte[] data = byteArrayOutputStream.toByteArray();
                                    UploadTask uploadTask = filePath.putBytes(data);

                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(InfoDetailsActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            if(taskSnapshot.getMetadata()!=null && taskSnapshot.getMetadata().getReference()!=null){
                                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String imageUrl = uri.toString();
                                                        Map newImageMap = new HashMap();
                                                        newImageMap.put("profilepictureurl", imageUrl);

                                                        userDatabaseRef.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(@NonNull Task task) {
                                                                if(task.isSuccessful()){ Toast.makeText(InfoDetailsActivity.this, "Image url added to database successfully", Toast.LENGTH_SHORT).show(); }
                                                                else{ Toast.makeText(InfoDetailsActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show(); }
                                                            }
                                                        });
                                                        finish();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                    Intent intent = new Intent(InfoDetailsActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    loader.dismiss();
                                }
                            }
                        }
                    });
                }
            }
        } );
        recipientSignInOption.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(InfoDetailsActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null){
            resultUri = data.getData();
            recipientProfileImage.setImageURI(resultUri);
        }
    }
}