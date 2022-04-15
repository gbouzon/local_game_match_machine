package com.appdev.lgmm;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private Button editProfile;
    private Toolbar toolbar;
    private TextView username;
    private TextInputEditText bio;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
        String key = mAuth.getUid();

        username = findViewById(R.id.usernameTextView);
        bio = findViewById(R.id.bioTextInput);

        Query retrieveUser = databaseReference.orderByChild("userID").equalTo(key);
        retrieveUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.i("snapshot", snapshot.getValue(String.class));
                    username.setText(snapshot.child("username").getValue(String.class));
                    Log.i("username", username.getText().toString());
                    bio.setText(snapshot.child("bio").getValue(String.class));
                    Log.i("bio", bio.getText().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        editProfile = findViewById(R.id.editProfileButton);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
            }
        });

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_backbutton);
        setSupportActionBar(toolbar);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings)
            startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
        else
            startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
        return super.onOptionsItemSelected(item);
    }
}