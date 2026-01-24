package com.mealify.mealify;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InnerAppFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InnerAppFragment extends Fragment {

    public InnerAppFragment() {
        // Required empty public constructor
    }


    public static InnerAppFragment newInstance(String param1, String param2) {
        InnerAppFragment fragment = new InnerAppFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inner_app, container, false);
    }
}