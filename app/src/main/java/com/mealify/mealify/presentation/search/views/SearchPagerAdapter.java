package com.mealify.mealify.presentation.search.views;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SearchPagerAdapter extends FragmentStateAdapter {

    public SearchPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public SearchPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return SearchByNameFragment.newInstance();
            case 1:
                return SearchByCategoryFragment.newInstance();
            case 2:
                return SearchByIngredientFragment.newInstance();
            case 3:
                return SearchByAreaFragment.newInstance();
            default:
                return SearchByNameFragment.newInstance();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
