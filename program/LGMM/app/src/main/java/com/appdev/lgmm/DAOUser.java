package com.appdev.lgmm;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;

public class DAOUser {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    public DAOUser() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(User.class.getSimpleName());
        mAuth = FirebaseAuth.getInstance();
    }

    public Task<Void> add(User user) {
        return databaseReference.push().setValue(user);
    }

    public Task<Void> update(HashMap<String, Object> hashMap) {
        String key = mAuth.getUid();
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> delete(String key) {
        return databaseReference.child(key).removeValue();
    }

    public User getCurrentUser() {
        User user = new User();
        String key = mAuth.getUid();
        Query retrieveUser = databaseReference.orderByChild("userID").equalTo(key);
        retrieveUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.i("UID", key);
                     user = new User(key, snapshot.child("username").getValue(String.class),
                            snapshot.child("email").getValue(String.class),
                             snapshot.child("bio").getValue(String.class),
                             Boolean.parseBoolean(snapshot.child("status").getValue(String.class)),
                             snapshot.child("profileImage").getValue(String.class));
                     Log.i("USERRRRR", user.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return user;
    }
}
