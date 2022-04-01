package com.example.lgmm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class InitPage extends AppCompatActivity {

    Button login;
    Button register;
    Button googleSignIn;
    Button gitHubSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_page);
        login = findViewById(R.id.loginButton);
        register = findViewById(R.id.registerButton);
        //googleSignIn = findViewById(R.id.googleSignInButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InitPage.this, Login.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InitPage.this, Register.class);
                startActivity(intent);
            }
        });
//
//        googleSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
////                        .requestEmail()
////                        .build();
////                GoogleSignInClient signInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //if (!account.equals(null)) {
            //updateUI(account);
        //}
    }
}
