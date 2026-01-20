package com.mealify.mealify.data.auth.datasources;

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
import com.mealify.mealify.core.response.ApiResponse;
import com.mealify.mealify.core.helper.CustomLogger;

import java.util.concurrent.Executors;

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
    public void login(String email, String password, ApiResponse<String> apiResponse) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = authResult.getUser();
                    if (user != null) {
                        apiResponse.onSuccess(user.getUid());
                    } else {
                        apiResponse.onError("Login failed: User not found");
                    }
                })
                .addOnFailureListener(e -> {
                    apiResponse.onError(e.getMessage());
                });
    }
    @Override
    public void register(String email, String password, String name,ApiResponse<String> apiResponse) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = authResult.getUser();
                    if (user != null) {
                        apiResponse.onSuccess(user.getUid());
                    } else {
                        apiResponse.onError("Registration failed: User not created");
                    }
                })
                .addOnFailureListener(e -> {
                    apiResponse.onError(e.getMessage());
                });
    }
    @Override
    public void signInAnonymously(ApiResponse<String> apiResponse) {
        firebaseAuth.signInAnonymously()
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = authResult.getUser();
                    if (user != null) {
                        apiResponse.onSuccess("guest_" + user.getUid());
                    } else {
                        apiResponse.onError("Anonymous sign-in failed: User not found");
                    }
                })
                .addOnFailureListener(e -> {
                    apiResponse.onError(e.getMessage());
                });
    }
    @Override
    public void signInWithGoogle(ApiResponse<String> apiResponse) {
        credentialManager.getCredentialAsync(
                context,
                credentialRequest,
                new CancellationSignal(),
                Executors.newSingleThreadExecutor(),
                new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(@NonNull GetCredentialResponse result) {
                        handleSignIn(result.getCredential(), apiResponse);
                    }

                    @Override
                    public void onError(@NonNull GetCredentialException e) {
                        CustomLogger.log( "Google Sign-In failed: " + e.getMessage());
                        apiResponse.onError("Google Sign-In failed: " + e.getMessage());
                    }
                }
        );
    }

    private void handleSignIn(Credential credential, ApiResponse<String> apiResponse) {
        if (credential instanceof CustomCredential
                && credential.getType().equals(GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {

            Bundle credentialData = credential.getData();
            GoogleIdTokenCredential googleIdTokenCredential =
                    GoogleIdTokenCredential.createFrom(credentialData);

            firebaseAuthWithGoogle(googleIdTokenCredential.getIdToken(), apiResponse);
        } else {
           CustomLogger.log("Credential is not of type Google ID!");
            apiResponse.onError("Invalid credential type");
        }
    }

    private void firebaseAuthWithGoogle(String idToken, ApiResponse<String> apiResponse) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        CustomLogger.log( "signInWithCredential:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            apiResponse.onSuccess(user.getUid());
                        } else {
                            apiResponse.onError("Google authentication failed: User not found");
                        }
                    } else {
                        CustomLogger.log("signInWithCredential:failure " +task.getException());
                        apiResponse.onError("Google authentication failed: " +
                                (task.getException() != null ? task.getException().getMessage() : "Unknown error"));
                    }
                });
    }
    @Override
    public void signOut(ApiResponse<String> apiResponse) {

        firebaseAuth.signOut();
        ClearCredentialStateRequest clearRequest = new ClearCredentialStateRequest();
        credentialManager.clearCredentialStateAsync(
                clearRequest,
                new CancellationSignal(),
                Executors.newSingleThreadExecutor(),
                new CredentialManagerCallback<Void, ClearCredentialException>() {
                    @Override
                    public void onResult(@NonNull Void result) {
                        CustomLogger.log( "Credentials cleared successfully");
                        apiResponse.onSuccess("Signed out successfully");
                    }

                    @Override
                    public void onError(@NonNull ClearCredentialException e) {
                        CustomLogger.log( "Couldn't clear user credentials: " + e.getLocalizedMessage());
                        apiResponse.onError("Sign out failed: " + e.getMessage());
                    }
                }
        );
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }
}