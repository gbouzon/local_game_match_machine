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
    User user;

    public DAOUser() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(User.class.getSimpleName());
        mAuth = FirebaseAuth.getInstance();
        user = new User();
    }

    public void add(User user) {
        DatabaseReference dbr = databaseReference.child(mAuth.getUid());
        dbr.setValue(user);
        dbr.push();
    }

    public Task<Void> update(HashMap<String, Object> hashMap) {
        String key = mAuth.getUid();
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> delete(String key) {
        return databaseReference.child(key).removeValue();
    }
}
