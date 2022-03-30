package com.example.lgmm;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DAOUser {
    private DatabaseReference dbReference;

    public DAOUser() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        dbReference = db.getReference(User.class.getSimpleName());
    }

    public Task<Void> add(User user) {
        return dbReference.push().setValue(user);
    }
}
