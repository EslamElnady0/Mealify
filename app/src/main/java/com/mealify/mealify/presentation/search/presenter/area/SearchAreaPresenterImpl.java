package com.mealify.mealify.presentation.search.presenter.area;

import android.annotation.SuppressLint;
import android.content.Context;

import com.mealify.mealify.data.models.country.CountryDto;
import com.mealify.mealify.data.repos.meals.MealsRepo;
import com.mealify.mealify.presentation.search.views.area.SearchAreaView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class SearchAreaPresenterImpl implements SearchAreaPresenter {

    private final SearchAreaView view;
    private final MealsRepo mealsRepo;
    private List<CountryDto> allAreas = new ArrayList<>();

    public SearchAreaPresenterImpl(Context context, SearchAreaView view) {
        this.view = view;
        this.mealsRepo = new MealsRepo(context);
    }

    @Override
    @SuppressLint("CheckResult")
    public void getAreas() {
        view.toggleLoading(true);
        mealsRepo.listAreas()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        data -> {
                            view.toggleLoading(false);
                            allAreas = data;
                            view.showAreas(data);
                        },
                        error -> {
                            view.toggleLoading(false);
                            view.showError(error.getMessage());
                        }
                );
    }

    @Override
    public void searchAreas(String query) {
        if (query == null || query.trim().isEmpty()) {
            view.showAreas(allAreas);
            return;
        }

        List<CountryDto> filteredList = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase().trim();

        for (CountryDto area : allAreas) {
            if (area.getStrArea() != null && area.getStrArea().toLowerCase().contains(lowerCaseQuery)) {
                filteredList.add(area);
            }
        }

        view.showAreas(filteredList);
    }
}
