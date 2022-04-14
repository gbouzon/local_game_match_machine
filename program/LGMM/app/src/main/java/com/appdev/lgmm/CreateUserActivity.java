package com.appdev.lgmm;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateUserActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    private TextInputEditText username;
    private TextInputEditText email;
    private Button finishButton;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private Uri imageUri;
    private String myUri = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePicsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Profile Pic");

        profileImageView = findViewById(R.id.userImage);
        username = findViewById(R.id.usernameTextInput);
        email = findViewById(R.id.emailTextInput);
        finishButton = findViewById(R.id.finishButton);

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadProfileImage();
            }
        });

        profileImageView.setClickable(true);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void uploadProfileImage() {
    }
}