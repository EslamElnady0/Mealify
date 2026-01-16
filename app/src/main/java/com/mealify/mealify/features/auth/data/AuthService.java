package com.mealify.mealify.features.auth.data;
import com.google.firebase.auth.FirebaseAuth;


import java.util.concurrent.Executor;

public class AuthService {
    private FirebaseAuth firebaseAuth;
    public AuthService(){
        this.firebaseAuth  = FirebaseAuth.getInstance();
    }


}
