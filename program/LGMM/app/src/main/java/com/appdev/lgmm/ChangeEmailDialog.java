package com.appdev.lgmm;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GithubAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;

public class ChangeEmailDialog extends AppCompatDialogFragment {
    private TextInputEditText email;
    private TextInputEditText password;
    private ChangeEmailDialogListener listener;
    private FirebaseAuth mAuth;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user.getProviderData().get(1).getProviderId().equals(GoogleAuthProvider.PROVIDER_ID) ||
         user.getProviderData().get(1).getProviderId().equals(GithubAuthProvider.PROVIDER_ID)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setIcon(R.drawable.ic_block).setTitle("Unable to Change Email")
                    .setMessage("Your email cannot be change due to your login provider. " +
                            "Only accounts signed in with email and password can change email.");
            return builder.create();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.change_password_dialog, null);
            builder.setView(view).setTitle("Change Email")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .setPositiveButton("Complete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String newEmail = email.getText().toString();
                            String verifyPassword = password.getText().toString();
                            listener.getData(newEmail, verifyPassword);
                        }
                    });
            email = view.findViewById(R.id.newEmailTextInput);
            password = view.findViewById(R.id.verifyPasswordTextInput);
            return builder.create();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ChangeEmailDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement ChangeEmailDialogListener");
        }
    }

    public interface ChangeEmailDialogListener {
        void getData(String email, String password);
    }
}
