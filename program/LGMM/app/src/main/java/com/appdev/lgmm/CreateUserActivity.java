package com.appdev.lgmm;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageActivity;
import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.uikit.ui_settings.UIKitSettings;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;

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
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("profilepic");

        profileImageView = findViewById(R.id.userImage);
        username = findViewById(R.id.usernameTextInput);
        email = findViewById(R.id.emailTextInput);

        Intent intent = getIntent();
        if (intent.getStringExtra("email") != null)
            email.setText(intent.getStringExtra("email"));

        finishButton = findViewById(R.id.finishButton); //save button
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadProfileImage();
                Intent intent = new Intent(CreateUserActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        //starts picking the image
        profileImageView.setClickable(true);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

        getUserInfo();

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(CreateUserActivity.this, MainActivity.class));
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void createUser(String profileImage) {
        DAOUser db = new DAOUser();
        User user = new User(mAuth.getUid(), username.getText().toString(), email.getText().toString(), "", profileImage);
        db.add(user);
        createCometUser(user);
    }

    private void initCometChat() {
        AppSettings appSettings = new AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(Constants.REGION).build();

        CometChat.init(this, Constants.APP_ID, appSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {
                UIKitSettings.setAuthKey(Constants.AUTH_KEY);
                CometChat.setSource("uikit","android","java");
                Log.i("comet init", "success");
            }
            @Override
            public void onError(CometChatException e) {
                Log.i("comet init", "error" + e.getDetails());
            }
        });
    }

    private void createCometUser(User user) { //works, uid is the same as mauth id
        initCometChat();
        com.cometchat.pro.models.User chatUser = new com.cometchat.pro.models.User();
        chatUser.setUid(user.getUserID());
        chatUser.setName(user.getUsername());
        chatUser.setAvatar(user.getProfileImage());
        CometChat.createUser(chatUser, Constants.API_KEY, new CometChat.CallbackListener<com.cometchat.pro.models.User>() {
            @Override
            public void onSuccess(com.cometchat.pro.models.User user) {
                Log.i("create success", chatUser.toString());
            }

            @Override
            public void onError(CometChatException e) {
                Log.i("create error", e.getDetails());
            }
        });
    }

    private void getUserInfo() {
        databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    if (snapshot.hasChild("image")) {
                        String image = snapshot.child("image").getValue().toString();
                        if (image != null)
                            Picasso.get().load(image).into(profileImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                imageUri = data.getData();
                profileImageView.setImageURI(imageUri);
            }
        } else {
            Toast.makeText(this, "Error, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadProfileImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Set your profile");
        progressDialog.setMessage("Please wait, while we set your data");
        progressDialog.show();
        DAOUser db = new DAOUser();

        if (imageUri != null) {
            final StorageReference fileRef = storageProfilePicsRef.child(mAuth.getCurrentUser().getUid() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        myUri = downloadUri.toString();
                        createUser(myUri);

                        progressDialog.dismiss();
                    }
                }
            });
        } else {
            createUser("https://firebasestorage.googleapis.com/v0/b/lgmm-17b5b.appspot.com/o/profilepic%2Fprofile_pic.jpg?alt=media&token=d1b67ae6-718b-4461-bcf2-583568e8a77e");
            progressDialog.dismiss();
            //Toast.makeText(this, "Image not selected", Toast.LENGTH_SHORT).show();
        }
    }
}