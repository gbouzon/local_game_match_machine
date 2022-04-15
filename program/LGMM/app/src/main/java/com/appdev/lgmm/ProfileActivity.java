package com.appdev.lgmm;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
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

public class ProfileActivity extends AppCompatActivity {
    private Button editProfile;
    private Toolbar toolbar;
    private TextView username;
    private TextInputEditText bio;
    private Object Menu;
    private DAOUser db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        db = new DAOUser();
        User user = db.getCurrentUser();
        Log.i("user", user.toString());

        username = findViewById(R.id.usernameTextView);
        username.setText(user.getUsername());

        bio = findViewById(R.id.bioTextInput);
        bio.setText(user.getBio());

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