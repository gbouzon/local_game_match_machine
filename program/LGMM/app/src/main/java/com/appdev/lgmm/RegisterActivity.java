package com.appdev.lgmm;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText email;
    TextInputEditText password;
    TextView login;
    Button register;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.emailTextInput);
        password = findViewById(R.id.passwordTextInput);
        login = findViewById(R.id.loginTextView);
        register = findViewById(R.id.registerButton);

        mAuth = FirebaseAuth.getInstance();

        login.setClickable(true);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewUser();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void createNewUser() {
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        if (emailText.isEmpty() || passwordText.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter BOTH an email and a password to register.", Toast.LENGTH_LONG).show();
        }
        else {
            mAuth.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                        mAuth.signInWithEmailAndPassword(emailText, passwordText);
                        mAuth.getCurrentUser().sendEmailVerification();
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setTitle("Email Verification sent").
                                setMessage("A verification email has been sent to: " + emailText + ". " +
                                        "You will need to verify your account to use features such as Geolocation, and Chatting.")
                                .setIcon(R.drawable.ic_verified)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            }
                        });

                        builder.create().show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}