package com.example.baking_app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.baking_app.R;
import com.example.baking_app.data.Step;

import org.parceler.Parcels;

public class RecipeStepDetailFragment extends Fragment {
    public static final String ARG_STEP = "step";

    private Step mStep;

    public RecipeStepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(ARG_STEP)) {
            mStep = Parcels.unwrap(arguments.getParcelable(ARG_STEP));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        return rootView;
    }
}
