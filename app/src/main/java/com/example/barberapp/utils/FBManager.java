package com.example.barberapp.utils;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class FBManager {
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firebaseFirestore;
    private final StorageReference storageReference;
    private static FBManager instance;
    public static final String USERS = "users";
    public static final String APPOINTMENTS = "appointments";
    public static final String CALENDAR = "calendar";
    public static final String TREATMENTS = "treatments";
    public static final String BARBERS = "barbers";
    public static final String DAYS_OFF = "days_off";


    private FBManager() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

    }

    public static FBManager getInstance() {
        return instance;
    }


    public static void init() {
        if (instance == null) {
            instance = new FBManager();
        }
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public FirebaseFirestore getFirebaseFirestore() {
        return firebaseFirestore;
    }

    public String getUserID() {
        return Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
    }

    public StorageReference getStorageReference() {
        return storageReference;
    }

}
