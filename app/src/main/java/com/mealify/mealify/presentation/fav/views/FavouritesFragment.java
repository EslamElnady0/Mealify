package com.mealify.mealify.presentation.fav.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mealify.mealify.R;
import com.mealify.mealify.data.favs.model.fav.FavouriteWithMeal;

import java.util.List;

public class FavouritesFragment extends Fragment implements FavsView{


    public FavouritesFragment() {
    }


    public static FavouritesFragment newInstance(String param1, String param2) {
        return new FavouritesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void toggleLoading(boolean isLoading) {

    }

    @Override
    public void onFavsSuccess(LiveData<List<FavouriteWithMeal>> favMealsLiveData) {

    }

    @Override
    public void onFavsFailure(String errorMessage) {

    }
}