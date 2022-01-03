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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

public class DonorInfoDetailsActivity extends AppCompatActivity {

    private CircleImageView donorProfileImage;
    private TextInputEditText donorFullName, donorIdNumber, donorPhoneNumber, donorEmergencyNumber, donorEmail, donorPassword;
    private Button donorContinueButton;
    private Uri resultUri;
    private ProgressDialog loader;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;

    String[] areas = {"Perlis","Kedah","Kelantan","Terengganu","Penang","Perak","Pahang","Selangor","Negeri Sembilan","Malacca","Johor","Sabah","Sarawak"};
    AutoCompleteTextView donorAREA;
    ArrayAdapter<String> adapterAreas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_info_details);

        donorAREA = findViewById(R.id.donorArea);

        adapterAreas = new ArrayAdapter<String>(this,R.layout.list_area, areas);
        donorAREA.setAdapter(adapterAreas);

        donorAREA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String area = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(),"Area: "+area, Toast.LENGTH_SHORT).show();
            }
        });
        donorProfileImage = findViewById(R.id.donorProfileImage);
        donorFullName = findViewById(R.id.donorFullName);
        donorIdNumber = findViewById(R.id.donorIdNumber);
        donorPhoneNumber = findViewById(R.id.donorPhoneNumber);
        donorEmergencyNumber = findViewById(R.id.donorEmergencyNumber);
        donorEmail = findViewById(R.id.donorEmail);
        donorPassword = findViewById(R.id.donorPassword);
        donorContinueButton = findViewById(R.id.donorContinueButton);
        loader = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        donorProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        donorContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = donorEmail.getText().toString().trim();
                final String password = donorPassword.getText().toString().trim();
                final String fullName = donorFullName.getText().toString().trim();
                final String idNumber = donorIdNumber.getText().toString().trim();
                final String phoneNumber = donorPhoneNumber.getText().toString().trim();
                final String emergencyNumber = donorEmergencyNumber.getText().toString().trim();
                final String areas = donorAREA.getText().toString();

                if(TextUtils.isEmpty(email)) {
                    donorEmail.setError("Email is required");
                    return; }
                if(TextUtils.isEmpty(password)){
                    donorPassword.setError("Password is required");
                    return; }
                if(TextUtils.isEmpty(fullName)) {
                    donorFullName.setError("Full name is required");
                    return; }
                if(TextUtils.isEmpty(idNumber)) {
                    donorIdNumber.setError("Id number is required");
                    return; }
                if(TextUtils.isEmpty(phoneNumber)) {
                    donorPhoneNumber.setError("Phone number is required");
                    return; }
                if(TextUtils.isEmpty(emergencyNumber)) {
                    donorEmergencyNumber.setError("Emergency number is required");
                    return; }
                if(TextUtils.isEmpty(areas)) {
                    donorAREA.setError("Living area is required");
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
                                Toast.makeText(DonorInfoDetailsActivity.this, "Error"+error, Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String currentUserId = mAuth.getCurrentUser().getUid();
                                userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);
                                HashMap userInfo = new HashMap();
                                userInfo.put("id", currentUserId);
                                userInfo.put("type", "donor");
                                userInfo.put("email", donorEmail);
                                userInfo.put("password", donorPassword);
                                userInfo.put("name", donorFullName);
                                userInfo.put("id number", donorIdNumber);
                                userInfo.put("phone number", donorPhoneNumber);
                                userInfo.put("emergency number", donorEmergencyNumber);
                                userInfo.put("living area", donorAREA);
                                userInfo.put("search", "donor"+donorAREA);

                                userDatabaseRef.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(DonorInfoDetailsActivity.this,"Data set successfully",Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(DonorInfoDetailsActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(DonorInfoDetailsActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
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
                                                                if(task.isSuccessful()){ Toast.makeText(DonorInfoDetailsActivity.this, "Image url added to database successfully", Toast.LENGTH_SHORT).show(); }
                                                                else{ Toast.makeText(DonorInfoDetailsActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show(); }
                                                            }
                                                        });
                                                        finish();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                    Intent intent = new Intent(DonorInfoDetailsActivity.this,MainActivity.class);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null){
            resultUri = data.getData();
            donorProfileImage.setImageURI(resultUri);
        }
    }
}