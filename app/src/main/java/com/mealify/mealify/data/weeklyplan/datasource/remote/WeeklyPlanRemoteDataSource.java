package com.mealify.mealify.data.weeklyplan.datasource.remote;

import com.mealify.mealify.data.favs.datasource.remote.CloudFirestoreService;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class WeeklyPlanRemoteDataSource {

    private static final String USERS_COLLECTION = "users";
    private static final String NESTED_WEEKLY_PLAN_COLLECTION = "weeklyPlan";

    private final CloudFirestoreService cloudFirestoreService;

    public WeeklyPlanRemoteDataSource() {
        cloudFirestoreService = CloudFirestoreService.getInstance();
    }

    public Completable saveToWeeklyPlan(String nestedId, WeeklyPlanMealEntity data) {
        return cloudFirestoreService.saveToNestedCollection(
                USERS_COLLECTION,
                NESTED_WEEKLY_PLAN_COLLECTION,
                nestedId,
                data
        );
    }

    public Completable deleteFromWeeklyPlan(String nestedId) {
        return cloudFirestoreService.deleteFromNestedCollection(
                USERS_COLLECTION,
                NESTED_WEEKLY_PLAN_COLLECTION,
                nestedId
        );
    }

    public Single<List<WeeklyPlanMealEntity>> getWeeklyPlanFromRemote() {
        return cloudFirestoreService.getFromNestedCollection(
                USERS_COLLECTION,
                NESTED_WEEKLY_PLAN_COLLECTION,
                WeeklyPlanMealEntity.class
        );
    }
}
