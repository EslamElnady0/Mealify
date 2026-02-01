package com.mealify.mealify.data.datasources.auth.remote.services;

import android.content.Context;
import android.os.Bundle;
import android.os.CancellationSignal;

import androidx.annotation.NonNull;
import androidx.credentials.ClearCredentialStateRequest;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.ClearCredentialException;
import androidx.credentials.exceptions.GetCredentialException;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mealify.mealify.R;
import com.mealify.mealify.core.helper.CustomLogger;

import java.util.concurrent.Executors;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class FirebaseAuthService implements AuthService {
    private static FirebaseAuthService instance;
    private final FirebaseAuth firebaseAuth;
    private final CredentialManager credentialManager;
    private final GetCredentialRequest credentialRequest;
    private final Context context;

    private FirebaseAuthService(Context context) {
        this.context = context;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.credentialManager = CredentialManager.create(context);

        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.default_web_client_id))
                .build();

        this.credentialRequest = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();
    }

    public static FirebaseAuthService getInstance(Context context) {
        if (instance == null) {
            instance = new FirebaseAuthService(context);
        }
        return instance;
    }

    @Override
    public Single<String> login(String email, String password) {
        return Single.create(emitter -> {
            CustomLogger.log("Attempting login for email: " + email, "AUTH_SERVICE");
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser user = authResult.getUser();
                        if (user != null) {
                            CustomLogger.log("Login successful: " + user.getUid(), "AUTH_SERVICE");
                            emitter.onSuccess(user.getUid());
                        } else {
                            CustomLogger.log("Login failed: User not found", "AUTH_SERVICE");
                            emitter.onError(new Exception("Login failed: User not found"));
                        }
                    })
                    .addOnFailureListener(e -> {
                        CustomLogger.log("Login failed: " + e.getMessage(), "AUTH_SERVICE");
                        emitter.onError(e);
                    });
        });
    }

    @Override
    public Single<String> register(String email, String password, String name) {
        return Single.create(emitter -> {
            CustomLogger.log("Attempting registration for email: " + email, "AUTH_SERVICE");
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser user = authResult.getUser();
                        if (user != null) {
                            CustomLogger.log("Registration successful: " + user.getUid(), "AUTH_SERVICE");
                            emitter.onSuccess(user.getUid());
                        } else {
                            CustomLogger.log("Registration failed: User not created", "AUTH_SERVICE");
                            emitter.onError(new Exception("Registration failed: User not created"));
                        }
                    })
                    .addOnFailureListener(e -> {
                        CustomLogger.log("Registration failed: " + e.getMessage(), "AUTH_SERVICE");
                        emitter.onError(e);
                    });
        });
    }

    @Override
    public Single<String> signInAnonymously() {
        return Single.create(emitter -> {
            CustomLogger.log("Attempting anonymous sign-in", "AUTH_SERVICE");
            firebaseAuth.signInAnonymously()
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser user = authResult.getUser();
                        if (user != null) {
                            CustomLogger.log("Anonymous sign-in successful: " + user.getUid(), "AUTH_SERVICE");
                            emitter.onSuccess("guest_" + user.getUid());
                        } else {
                            CustomLogger.log("Anonymous sign-in failed: User not found", "AUTH_SERVICE");
                            emitter.onError(new Exception("Anonymous sign-in failed: User not found"));
                        }
                    })
                    .addOnFailureListener(e -> {
                        CustomLogger.log("Anonymous sign-in failed: " + e.getMessage(), "AUTH_SERVICE");
                        emitter.onError(e);
                    });
        });
    }

    @Override
    public Single<String> signInWithGoogle() {
        return Single.create(emitter -> {
            credentialManager.getCredentialAsync(
                    context,
                    credentialRequest,
                    new CancellationSignal(),
                    Executors.newSingleThreadExecutor(),
                    new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                        @Override
                        public void onResult(@NonNull GetCredentialResponse result) {
                            handleSignIn(result.getCredential(), emitter);
                        }

                        @Override
                        public void onError(@NonNull GetCredentialException e) {
                            CustomLogger.log("Google Sign-In failed: " + e.getMessage());
                            emitter.onError(new Exception("Google Sign-In failed: " + e.getMessage()));
                        }
                    }
            );
        });
    }

    private void handleSignIn(Credential credential, io.reactivex.rxjava3.core.SingleEmitter<String> emitter) {
        if (credential instanceof CustomCredential
                && credential.getType().equals(GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {

            Bundle credentialData = credential.getData();
            GoogleIdTokenCredential googleIdTokenCredential =
                    GoogleIdTokenCredential.createFrom(credentialData);

            firebaseAuthWithGoogle(googleIdTokenCredential.getIdToken(), emitter);
        } else {
            CustomLogger.log("Credential is not of type Google ID!");
            emitter.onError(new Exception("Invalid credential type"));
        }
    }

    private void firebaseAuthWithGoogle(String idToken, io.reactivex.rxjava3.core.SingleEmitter<String> emitter) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        CustomLogger.log("signInWithCredential:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            emitter.onSuccess(user.getUid());
                        } else {
                            emitter.onError(new Exception("Google authentication failed: User not found"));
                        }
                    } else {
                        CustomLogger.log("signInWithCredential:failure " + task.getException());
                        emitter.onError(new Exception("Google authentication failed: " +
                                (task.getException() != null ? task.getException().getMessage() : "Unknown error")));
                    }
                });
    }

    @Override
    public Completable signOut() {
        return Completable.create(emitter -> {
            firebaseAuth.signOut();
            ClearCredentialStateRequest clearRequest = new ClearCredentialStateRequest();
            credentialManager.clearCredentialStateAsync(
                    clearRequest,
                    new CancellationSignal(),
                    Executors.newSingleThreadExecutor(),
                    new CredentialManagerCallback<Void, ClearCredentialException>() {
                        @Override
                        public void onResult(@NonNull Void result) {
                            CustomLogger.log("Credentials cleared successfully");
                            emitter.onComplete();
                        }

                        @Override
                        public void onError(@NonNull ClearCredentialException e) {
                            CustomLogger.log("Couldn't clear user credentials: " + e.getLocalizedMessage());
                            emitter.onError(new Exception("Sign out failed: " + e.getMessage()));
                        }
                    }
            );
        });
    }

    @Override
    public String getCurrentUserId() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }
}