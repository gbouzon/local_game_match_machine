package com.appdev.lgmm;



import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    public Task<Void> delete() {
        String key = mAuth.getUid();
        return databaseReference.child(key).removeValue();
    }
}
