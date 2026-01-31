package com.mealify.mealify.presentation.fav.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mealify.mealify.InnerAppFragmentDirections;
import com.mealify.mealify.R;
import com.mealify.mealify.core.utils.DialogUtils;
import com.mealify.mealify.data.models.fav.FavouriteWithMeal;
import com.mealify.mealify.data.repos.meals.MealsRepo;
import com.mealify.mealify.presentation.auth.AuthActivity;
import com.mealify.mealify.presentation.fav.presenter.FavsPresenter;
import com.mealify.mealify.presentation.fav.presenter.FavsPresenterImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FavouritesFragment extends Fragment implements FavsView {

    private FavsPresenter presenter;
    private RecyclerView favoritesRecycler;
    private FavsAdapter adapter;
    private LinearLayout emptyView;
    private TextInputEditText searchInput;
    private TextView favoritesCount;
    private View guestContainer;
    private MaterialButton loginBtn;
    private View searchLayout;
    private View headerLayout;
    private List<FavouriteWithMeal> allFavs = new ArrayList<>();

    public FavouritesFragment() {
    }

    public static FavouritesFragment newInstance() {
        return new FavouritesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new FavsPresenterImpl(new MealsRepo(requireContext()), this, requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        favoritesRecycler = view.findViewById(R.id.favoritesRecycler);
        emptyView = view.findViewById(R.id.emptyView);
        searchInput = view.findViewById(R.id.searchInput);
        favoritesCount = view.findViewById(R.id.favorites_count);
        guestContainer = view.findViewById(R.id.guest_container);
        loginBtn = view.findViewById(R.id.login_btn);
        searchLayout = view.findViewById(R.id.searchLayout);
        headerLayout = view.findViewById(R.id.headerLayout);

        loginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AuthActivity.class);
            startActivity(intent);
        });

        setupRecyclerView();
        setupSearch();

        presenter.getFavouriteMeals();
    }

    private void setupRecyclerView() {
        adapter = new FavsAdapter(
                fav -> {
                    if (fav.meal != null) {
                        int mealId = Integer.parseInt(fav.meal.getId());
                        InnerAppFragmentDirections.ActionInnerAppFragmentToMealDetailsFragment action =
                                InnerAppFragmentDirections.actionInnerAppFragmentToMealDetailsFragment(mealId);

                        NavHostFragment navHostFragment = (NavHostFragment)
                                getActivity().getSupportFragmentManager().findFragmentById(R.id.inner_home_container);
                        if (navHostFragment != null) {
                            NavController navController = navHostFragment.getNavController();
                            navController.navigate(action);
                        }
                    }
                },
                fav -> {
                    if (fav.meal != null) {
                        DialogUtils.showConfirmationDialog(
                                getContext(),
                                fav.meal.getThumbnail(),
                                getString(R.string.remove_from_favorites),
                                getString(R.string.are_you_sure_you_want_to_remove_this_meal_from_your_favorites),
                                new DialogUtils.DialogCallback() {
                                    @Override
                                    public void onConfirm() {
                                        presenter.removeFavouriteMeal(fav.meal.getId());
                                    }

                                    @Override
                                    public void onCancel() {
                                    }
                                }
                        );
                    }
                }
        );
        favoritesRecycler.setAdapter(adapter);
    }

    private void setupSearch() {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void filter(String query) {
        if (query.isEmpty()) {
            adapter.setFavs(allFavs);
            updateCount(allFavs.size());
        } else {
            List<FavouriteWithMeal> filteredList = allFavs.stream()
                    .filter(fav -> fav.meal != null && fav.meal.getName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
            adapter.setFavs(filteredList);
            updateCount(filteredList.size());
        }
    }

    private void updateCount(int count) {
        if (favoritesCount != null) {
            favoritesCount.setText(String.valueOf(count));
        }
    }

    @Override
    public void onFavsSuccess(List<FavouriteWithMeal> favMeals) {
        if (favMeals == null || favMeals.isEmpty()) {
            allFavs = new ArrayList<>();
            favoritesRecycler.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            updateCount(0);
        } else {
            allFavs = favMeals;
            favoritesRecycler.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            adapter.setFavs(favMeals);
            updateCount(favMeals.size());
        }
    }

    @Override
    public void onFavsFailure(String errorMessage) {
        // Handle failure
    }

    @Override
    public void setGuestMode(boolean isGuest) {
        if (guestContainer != null) guestContainer.setVisibility(isGuest ? View.VISIBLE : View.GONE);
        if (favoritesRecycler != null) favoritesRecycler.setVisibility(isGuest ? View.GONE : View.VISIBLE);
        if (searchLayout != null) searchLayout.setVisibility(isGuest ? View.GONE : View.VISIBLE);
        if (headerLayout != null) headerLayout.setVisibility(isGuest ? View.GONE : View.VISIBLE);
        if (emptyView != null && isGuest) emptyView.setVisibility(View.GONE);
    }
}