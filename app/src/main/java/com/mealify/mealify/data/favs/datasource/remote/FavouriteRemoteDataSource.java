package com.mealify.mealify.data.favs.datasource.remote;

import com.mealify.mealify.data.favs.model.fav.FavouriteEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class FavouriteRemoteDataSource {
    private static final String USERS_COLLECTION = "users";
    private static final String NESTED_FAV_COLLECTION_NAME = "favourites";
    private CloudFirestoreService cloudFirestoreService;

    public FavouriteRemoteDataSource() {
        cloudFirestoreService = CloudFirestoreService.getInstance();
    }

    public Completable saveToFavourites(String nestedId, Object data) {
        return cloudFirestoreService.saveToNestedCollection(USERS_COLLECTION, NESTED_FAV_COLLECTION_NAME, nestedId, data);
    }

    public Completable deleteFromFavourites(String nestedId) {
        return cloudFirestoreService.deleteFromNestedCollection(USERS_COLLECTION, NESTED_FAV_COLLECTION_NAME, nestedId);
    }

    public Single<List<FavouriteEntity>> getFavouritesFromRemote() {
        return cloudFirestoreService.getFromNestedCollection(USERS_COLLECTION, NESTED_FAV_COLLECTION_NAME, FavouriteEntity.class);
    }
}
