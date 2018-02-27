package com.example.baking_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.baking_app.IRecipeClickListener;
import com.example.baking_app.NetworkUtils;
import com.example.baking_app.R;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recipes_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.loading_indicator_pb)
    ProgressBar mLoadingIndicator;

    @BindView(R.id.error_message_display_tv)
    TextView mErrorMessageDisplay;

    private RecipesAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAdapter = new RecipesAdapter(onRecipeItemClickListener);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        mLoadingIndicator.setVisibility(View.VISIBLE);

        NetworkUtils.fetchRecipes(this, result -> {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (result == null) {
                mAdapter.setRecipeData(null);
                showErrorMessage();
            } else {
                mAdapter.setRecipeData(result);
                showMoviesDataView();
            }
        });
    }

    private void showMoviesDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private IRecipeClickListener onRecipeItemClickListener = recipe -> {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.ARG_RECIPE, Parcels.wrap(recipe));
        startActivity(intent);
    };
}
