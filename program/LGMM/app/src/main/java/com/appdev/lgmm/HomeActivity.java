package com.appdev.lgmm;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.uikit.ui_components.cometchat_ui.CometChatUI;
import com.cometchat.pro.uikit.ui_settings.UIKitSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GithubAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    TextView chatButton;
    CircleImageView profileButton;
    Button nearby;
    String cometUserID;
    boolean verified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("User");
        cometUserID = mAuth.getUid();
        verifyUser();

        profileButton = findViewById(R.id.profileButton);
        chatButton = findViewById(R.id.chatButton);
        nearby = findViewById(R.id.nearbyPlayersButton);
        verified = emailVerified();

        Query retrieveUser = dbRef.orderByChild("userID").equalTo(mAuth.getUid());
        retrieveUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.child(mAuth.getUid()).child("profileImage").getValue(String.class) != null)
                        Picasso.get().load(Uri.parse(snapshot.child(mAuth.getUid()).child("profileImage")
                            .getValue(String.class))).into(profileButton);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });


        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!verified)
                    verified = emailVerified();
                else {
                    initCometChat();
                    login();
                    Intent intent = new Intent(HomeActivity.this, CometChatUI.class);
                    startActivity(intent);
                }
            }
        });


        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!verified)
                    verified = emailVerified();
                else
                    startActivity(new Intent(HomeActivity.this, NearbyPlayersActivity.class));
            }
        });


        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                mAuth.signOut();
                logout();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void verifyUser() {
        mAuth.getCurrentUser().reload();
        String UID = mAuth.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User");
        Query checkUser = ref.orderByChild("userID").equalTo(UID);
        checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists() || snapshot.getChildrenCount() <= 0)
                    startActivity(new Intent(HomeActivity.this, CreateUserActivity.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
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
                Log.i("comet init", "error");
            }
        });
    }

    private void login() {
        if (CometChat.getLoggedInUser() == null) {
            CometChat.login(cometUserID, Constants.API_KEY, new CometChat.CallbackListener<com.cometchat.pro.models.User>() {
                @Override
                public void onSuccess(com.cometchat.pro.models.User user) {
                    Log.i("comet login", "success");
                    startActivity(new Intent(HomeActivity.this, CometChatUI.class));
                }

                @Override
                public void onError(CometChatException e) {
                    Log.i("comet login", "error");
                }
            });
        }
        else {
            startActivity(new Intent(HomeActivity.this, CometChatUI.class));
        }
    }

    private void logout() {
        if (CometChat.getLoggedInUser() != null) {
            CometChat.logout(new CometChat.CallbackListener<String>() {
                @Override
                public void onSuccess(String s) {
                    Toast.makeText(HomeActivity.this, "User successfully logged out.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(CometChatException e) {

                }
            });
        }
    }

    private boolean emailVerified() {
        mAuth.getCurrentUser().reload();
        if (mAuth.getCurrentUser().isEmailVerified()) {
            return true;
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("Email must be verified").setMessage("Your email has yet to be verified. " +
                    "Would you like us to send another verification email?").setIcon(R.drawable.ic_verified)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mAuth.getCurrentUser().sendEmailVerification();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(HomeActivity.this, "No verification email sent.", Toast.LENGTH_SHORT).show();
                        }
                    });
            builder.create().show();
        }
        mAuth.getCurrentUser().reload();
        return false;
    }
}