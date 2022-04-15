package com.appdev.lgmm;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.cometchat.pro.uikit.ui_components.cometchat_ui.CometChatUI;
import com.cometchat.pro.uikit.ui_settings.UIKitSettings;
import com.google.firebase.auth.FirebaseAuth;

public class ChatActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    String cometUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        cometUserID = mAuth.getUid();

        initCometChat();
        login();

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                mAuth.signOut();
                startActivity(new Intent(ChatActivity.this, HomeActivity.class));
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void initCometChat() {
        AppSettings appSettings = new AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(Constants.REGION).build();

        CometChat.init(this, Constants.APP_ID, appSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {
                UIKitSettings.setAuthKey(Constants.AUTH_KEY);
                CometChat.setSource("uikit","android","java");
                //Toast.makeText(getApplicationContext(), "comet init: success ", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onError(CometChatException e) {
                //Toast.makeText(getApplicationContext(), "comet init: error " + e.getDetails(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void login() {
        if (CometChat.getLoggedInUser() == null) {
            CometChat.login(cometUserID, Constants.API_KEY, new CometChat.CallbackListener<com.cometchat.pro.models.User>() {
                @Override
                public void onSuccess(com.cometchat.pro.models.User user) {
                    startActivity(new Intent(ChatActivity.this, CometChatUI.class));
                }

                @Override
                public void onError(CometChatException e) {
                    //Toast.makeText(getApplicationContext(), "comet login: error " + e.getDetails(), Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            startActivity(new Intent(ChatActivity.this, CometChatUI.class));
        }
    }
}