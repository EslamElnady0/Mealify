package com.mealify.mealify.presentation.search.presenter.area;

import android.content.Context;

import com.mealify.mealify.core.response.GeneralResponse;
import com.mealify.mealify.data.meals.model.country.CountryDto;
import com.mealify.mealify.data.meals.repo.MealsRepo;
import com.mealify.mealify.presentation.search.views.area.SearchAreaView;

import java.util.ArrayList;
import java.util.List;

public class SearchAreaPresenterImpl implements SearchAreaPresenter {

    private final SearchAreaView view;
    private final MealsRepo mealsRepo;
    private List<CountryDto> allAreas = new ArrayList<>();

    public SearchAreaPresenterImpl(Context context, SearchAreaView view) {
        this.view = view;
        this.mealsRepo = new MealsRepo(context);
    }

    @Override
    public void getAreas() {
        view.toggleLoading(true);
        mealsRepo.listAreas(new GeneralResponse<List<CountryDto>>() {
            @Override
            public void onSuccess(List<CountryDto> data) {
                view.toggleLoading(false);
                allAreas = data;
                view.showAreas(data);
            }

            @Override
            public void onError(String errorMessage) {
                view.toggleLoading(false);
                view.showError(errorMessage);
            }
        });
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
