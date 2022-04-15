package com.appdev.lgmm;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {
    TextInputEditText username;
    TextInputEditText bio;
    FirebaseAuth mAuth;
    DAOUser db;
    Button finish;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        mAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(User.class.getSimpleName());
        String key = mAuth.getUid();

        username = findViewById(R.id.usernameTextInput);
        bio = findViewById(R.id.bioTextInput);

        Query retrieveUser = databaseReference.orderByChild("userID").equalTo(key);
        retrieveUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.i("UID", key);
                    username.setText(snapshot.child("username").getValue(String.class));
                    bio.setText(snapshot.child("bio").getValue(String.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_backbutton);
        setSupportActionBar(toolbar);

        finish = findViewById(R.id.finishButton);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("username", username.getText().toString());
                map.put("bio", bio.getText().toString());
                db.update(map);
                startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
        return super.onOptionsItemSelected(item);
    }
}