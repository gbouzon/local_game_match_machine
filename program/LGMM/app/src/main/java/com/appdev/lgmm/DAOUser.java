package com.appdev.lgmm;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DAOUser {
    private DatabaseReference databaseReference;

    public DAOUser() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(User.class.getSimpleName()); // Giu is a cutie uwu OwO
    }

    public Task<Void> add(User user) {
        return databaseReference.push().setValue(user);
    }
}
