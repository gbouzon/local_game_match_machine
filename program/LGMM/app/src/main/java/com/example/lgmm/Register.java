package com.example.lgmm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Register extends AppCompatActivity {

    EditText email;
    EditText username;
    EditText password;
    EditText confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        email = findViewById(R.id.emailField);
        username = findViewById(R.id.usernameField);
        password = findViewById(R.id.passwordField);
        confirm = findViewById(R.id.confirmField);
        Button registerButton = findViewById(R.id.registerButton);
        DAOUser dao = new DAOUser();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!password.getText().toString().equals(confirm.getText().toString()) && !fieldsComplete())
                    Toast.makeText(getApplicationContext(), "Passwords do not match. Please try again", Toast.LENGTH_LONG).show();
                else {
                    try {
                        User user = new User(email.getText().toString(), username.getText().toString(),
                                /*passwordHash(*/password.getText().toString()/*)*/);
                        dao.add(user).addOnSuccessListener(suc -> {
                            Toast.makeText(getApplicationContext(), "Registration Complete", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(er -> {
                            Toast.makeText(getApplicationContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public String passwordHash(String password) throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        return md.digest(password.getBytes(StandardCharsets.UTF_8)).toString();
    }

    public boolean fieldsComplete() {
        if (email.getText().toString().equals("") || username.getText().toString().equals("") ||
        password.getText().toString().equals("")) {
            return false;
        }
        return true;
    }
}