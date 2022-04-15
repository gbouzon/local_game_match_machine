package com.appdev.lgmm;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {
    TextInputEditText username;
    TextInputEditText bio;
    DAOUser db;
    Button finish;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        db = new DAOUser();
        User currentUser = db.getCurrentUser();

        username = findViewById(R.id.usernameTextInput);
        bio = findViewById(R.id.bioTextInput);
        username.setText(currentUser.getUsername());
        bio.setText(currentUser.getBio());

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