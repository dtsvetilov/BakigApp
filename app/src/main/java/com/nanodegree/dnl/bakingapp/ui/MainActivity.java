package com.nanodegree.dnl.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;


import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.nanodegree.dnl.bakingapp.R;
import com.nanodegree.dnl.bakingapp.data.Recipe;
import com.nanodegree.dnl.bakingapp.data.SimpleIdlingResource;
import com.nanodegree.dnl.bakingapp.networking.IDataReceived;
import com.nanodegree.dnl.bakingapp.networking.NetworkUtils;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindBool;
import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IDataReceived, RecipesAdapter.RecipeClickedHandler {
    @BindView(R.id.recipes_rv)
    RecyclerView recyclerView;

    @BindBool(R.bool.is_tablet)
    boolean isTablet;

    @BindInt(R.integer.grid_column_count)
    int gridColumnsCount;

    private List<Recipe> recipesData = new ArrayList<>();
    private RecipesAdapter adapter;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (isTablet) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, gridColumnsCount);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
        }

        adapter = new RecipesAdapter(recipesData, this);
        recyclerView.setAdapter(adapter);

        if (mIdlingResource != null)
            mIdlingResource.setIdleState(false);
        NetworkUtils.fetchRecipes(this, this);
    }

    @Override
    public void onDataReceived(List<Recipe> recipes) {
        recipesData.clear();
        recipesData.addAll(recipes);
        BakingApp.clearAllRecipes();
        BakingApp.addMultipleRecipes(recipes);
        adapter.notifyDataSetChanged();

        if (mIdlingResource != null)
            mIdlingResource.setIdleState(recipes.size() > 0);

    }

    @Override
    public void onRecipeClicked(Recipe recipe) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("recipe", Parcels.wrap(recipe));
        startActivity(intent);
    }
}
