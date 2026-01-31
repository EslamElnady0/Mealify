package com.mealify.mealify.data.datasources.meals.remote.service;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class CloudFirestoreService {
    private static CloudFirestoreService instance;
    private final FirebaseFirestore db;
    private final FirebaseAuth auth;

    private CloudFirestoreService() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

    }

    public static CloudFirestoreService getInstance() {
        if (instance == null) {
            instance = new CloudFirestoreService();
        }
        return instance;
    }


    public Completable saveToNestedCollection(String collectionName, String nestedCollectionName, String nestedId, Object data) {
        return Completable.create(emitter -> {
            db.collection(collectionName)
                    .document(auth.getCurrentUser().getUid())
                    .collection(nestedCollectionName)
                    .document(nestedId)
                    .set(data)
                    .addOnCompleteListener(task -> {
                        if (!emitter.isDisposed()) {
                            if (task.isSuccessful()) {
                                emitter.onComplete();
                            } else {
                                emitter.onError(task.getException());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            emitter.onError(e);
                        }
                    });
        });
    }

    public Completable deleteFromNestedCollection(String collectionName, String nestedCollectionName, String nestedId) {
        return Completable.create(emitter -> {
            db.collection(collectionName)
                    .document(auth.getCurrentUser().getUid())
                    .collection(nestedCollectionName)
                    .document(nestedId)
                    .delete()
                    .addOnCompleteListener(task -> {
                        if (!emitter.isDisposed()) {
                            if (task.isSuccessful()) {
                                emitter.onComplete();
                            } else {
                                emitter.onError(task.getException());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            emitter.onError(e);
                        }
                    });
        });
    }

    public <T> Single<List<T>> getFromNestedCollection(
            String collectionName,
            String nestedCollectionName,
            Class<T> tClass
    ) {
        return Single.create(emitter -> {

            if (auth.getCurrentUser() == null) {
                emitter.onError(new IllegalStateException("User not authenticated"));
                return;
            }

            db.collection(collectionName)
                    .document(auth.getCurrentUser().getUid())
                    .collection(nestedCollectionName)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (!emitter.isDisposed()) {
                            if (task.isSuccessful()) {
                                List<T> list = task
                                        .getResult()
                                        .toObjects(tClass);
                                emitter.onSuccess(list);
                            } else {
                                emitter.onError(task.getException());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            emitter.onError(e);
                        }
                    });
        });
    }
}
