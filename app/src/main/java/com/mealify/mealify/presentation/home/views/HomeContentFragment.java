package com.mealify.mealify.presentation.home.views;

import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.mealify.mealify.InnerAppFragmentDirections;
import com.mealify.mealify.R;
import com.mealify.mealify.core.helper.CustomToast;
import com.mealify.mealify.data.meals.model.category.CategoryDto;
import com.mealify.mealify.data.meals.model.filteredmeals.FilterType;
import com.mealify.mealify.data.meals.model.meal.MealDto;
import com.mealify.mealify.network.NetworkObservation;
import com.mealify.mealify.presentation.home.presenter.HomePresenter;
import com.mealify.mealify.presentation.home.presenter.HomePresenterImpl;

import java.util.Calendar;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;


public class HomeContentFragment extends Fragment implements HomeView {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private HomePresenter presenter;
    private RecyclerView categoriesRecyclerView;
    private RecyclerView categoriesLoadingRecyclerView;
    private CategoryAdapter categoryAdapter;
    private ShimmerCategoryAdapter shimmerCategoryAdapter;
    private MaterialCardView mealOfTheDayCard;
    private View mealOfTheDayLoadingShimmer;
    private TextView mealOfTheDayText;
    private TextView greetingText;
    private TextView browseCats;
    private NavController navController;

    public HomeContentFragment() {

    }

    public static HomeContentFragment newInstance() {
        return new HomeContentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenter = new HomePresenterImpl(requireContext(), this);
        View view = inflater.inflate(R.layout.fragment_home_content, container, false);

        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
        categoriesLoadingRecyclerView = view.findViewById(R.id.categoriesLoading);
        mealOfTheDayCard = view.findViewById(R.id.mealOfTheDayCard);
        mealOfTheDayLoadingShimmer = view.findViewById(R.id.mealOfTheDayLoading);
        mealOfTheDayText = view.findViewById(R.id.mealOfTheDayTitle);
        greetingText = view.findViewById(R.id.greetingText);
        browseCats = view.findViewById(R.id.browseCategoriesTitle);

        setGreeting();
        setupShimmerLoaders();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupCategoriesRecyclerView();
        setupNetworkMonitoring();
    }

    private void reloadData() {
        if (presenter != null && getContext() != null) {
            presenter.getMealOfTheDay();
            presenter.getCategories();
        }
    }

    private void setupNetworkMonitoring() {
        if (getContext() == null) return;
        disposables.add(
                NetworkObservation.getInstance(requireContext())
                        .observeConnection()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(isConnected -> {
                            if (isConnected && getContext() != null) {
                                reloadData();
                            }
                        }, throwable -> {
                        })
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposables.clear();
    }

    private void setupShimmerLoaders() {
        shimmerCategoryAdapter = new ShimmerCategoryAdapter();
        LinearLayoutManager shimmerLayoutManager = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        categoriesLoadingRecyclerView.setLayoutManager(shimmerLayoutManager);
        categoriesLoadingRecyclerView.setAdapter(shimmerCategoryAdapter);
        shimmerCategoryAdapter.setItemCount(4);
    }

    private void setGreeting() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        String greeting;
        if (hourOfDay >= 5 && hourOfDay < 12) {
            greeting = "Good Morning ☀️";
        } else if (hourOfDay >= 12 && hourOfDay < 17) {
            greeting = "Good Afternoon 🌤️";
        } else if (hourOfDay >= 17 && hourOfDay < 21) {
            greeting = "Good Evening 🌆";
        } else {
            greeting = "Good Night 🌙";
        }

        greetingText.setText(greeting);
    }

    private void bindMealOfTheDay(MealDto meal) {
        if (!isAdded()) return;

        TextView title = mealOfTheDayCard.findViewById(R.id.mealName);
        TextView category = mealOfTheDayCard.findViewById(R.id.categoryChip);
        TextView area = mealOfTheDayCard.findViewById(R.id.typeChip);
        ImageView mealImage = mealOfTheDayCard.findViewById(R.id.mealImage);
        MaterialButton viewDetailsButton = mealOfTheDayCard.findViewById(R.id.viewDetailsButton);

        title.setText(meal.name);
        category.setText(meal.category);
        area.setText(meal.area);

        Glide.with(this)
                .load(meal.thumbnail)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.mealify_logo)
                        .error(R.drawable.mealify_logo))
                .into(mealImage);

        mealOfTheDayCard.setVisibility(VISIBLE);

        mealOfTheDayCard.setOnClickListener(v -> navigateToMealDetails(meal.id));
        viewDetailsButton.setOnClickListener(v -> navigateToMealDetails(meal.id));
    }

    private void navigateToMealDetails(String mealId) {
        if (!isAdded()) return;
        int id = Integer.parseInt(mealId);
        InnerAppFragmentDirections.ActionInnerAppFragmentToMealDetailsFragment action =
                InnerAppFragmentDirections.actionInnerAppFragmentToMealDetailsFragment(id);
        NavHostFragment navHostFragment = (NavHostFragment)
                getActivity().getSupportFragmentManager().findFragmentById(R.id.inner_home_container);
        navController = navHostFragment.getNavController();
        navController.navigate(action);
    }

    private void setupCategoriesRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        categoriesRecyclerView.setLayoutManager(layoutManager);
        categoryAdapter = new CategoryAdapter(category -> {
            InnerAppFragmentDirections.ActionInnerAppFragmentToMealSearchResultFragment action =
                    InnerAppFragmentDirections.actionInnerAppFragmentToMealSearchResultFragment(FilterType.CATEGORY, category.name);
            NavHostFragment navHostFragment = (NavHostFragment)
                    getActivity().getSupportFragmentManager().findFragmentById(R.id.inner_home_container);
            NavController navController = navHostFragment.getNavController();
            navController.navigate(action);
        });

        categoriesRecyclerView.setAdapter(categoryAdapter);
    }

    @Override
    public void showMealOfTheDay(MealDto meal) {
        if (isAdded() && getContext() != null) {
            bindMealOfTheDay(meal);
        }
    }

    @Override
    public void showCategories(List<CategoryDto> categories) {
        if (isAdded() && categoryAdapter != null) {
            categoryAdapter.setCategories(categories);
        }
    }

    @Override
    public void showError(String message) {
        if (isAdded() && getContext() != null) {
            CustomToast.show(requireContext(), message);
        }
    }

    @Override
    public void toggleMealOfTheDayLoading(boolean isLoading) {
        if (isAdded() && mealOfTheDayLoadingShimmer != null) {
            mealOfTheDayLoadingShimmer.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            mealOfTheDayCard.setVisibility(isLoading ? View.GONE : View.VISIBLE);
            mealOfTheDayText.setVisibility(isLoading ? View.GONE : VISIBLE);
            greetingText.setVisibility(isLoading ? View.GONE : VISIBLE);
        }
    }

    @Override
    public void toggleCategoriesLoading(boolean isLoading) {
        if (isAdded() && categoriesLoadingRecyclerView != null) {
            categoriesLoadingRecyclerView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            categoriesRecyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
            browseCats.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        }
    }
}