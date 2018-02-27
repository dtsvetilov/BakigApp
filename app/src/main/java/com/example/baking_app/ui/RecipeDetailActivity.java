package com.example.baking_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baking_app.IRecipeStepClickListener;
import com.example.baking_app.R;
import com.example.baking_app.data.Ingredient;
import com.example.baking_app.data.Recipe;
import com.example.baking_app.data.Step;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;

public class RecipeDetailActivity extends AppCompatActivity {
    public static final String ARG_RECIPE = "recipe";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.ingredients_tv)
    TextView mIngredientsTv;

    @BindView(R.id.recipe_steps_rv)
    RecyclerView mRecipeStepsRv;

    private boolean mTwoPane;

    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.recipe_detail_container) != null) {
            mTwoPane = true;
        }

        Intent intent = getIntent();
        if (!intent.hasExtra(ARG_RECIPE)) {
            Toast.makeText(this, getString(R.string.recipe_not_found_error), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        mRecipe = Parcels.unwrap(intent.getParcelableExtra(ARG_RECIPE));

        setupRecyclerView();
        fillIngredients();
    }

    private void fillIngredients() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Ingredient ingredient : mRecipe.getIngredients()) {
            stringBuilder
                    .append(getString(R.string.list_bullet))
                    .append(" ")
                    .append(ingredient.getIngredient())
                    .append(" (")
                    .append(ingredient.getQuantity())
                    .append(" ")
                    .append(ingredient.getMeasure())
                    .append(")")
                    .append("\n\n");
        }

        mIngredientsTv.setText(stringBuilder.toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView() {
        RecipeStepsAdapter adapter = new RecipeStepsAdapter(mRecipe.getSteps(), mTwoPane, recipeStepClickListener);
        mRecipeStepsRv.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecipeStepsRv.setLayoutManager(layoutManager);
    }

    private IRecipeStepClickListener recipeStepClickListener = new IRecipeStepClickListener() {
        @Override
        public void onRecipeStepClick(Step step) {
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putParcelable(RecipeStepDetailFragment.ARG_STEP, Parcels.wrap(step));
                RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.recipe_detail_container, fragment)
                        .commit();
            } else {
                Intent intent = new Intent(RecipeDetailActivity.this, RecipeStepDetailActivity.class);
                intent.putExtra(RecipeStepDetailFragment.ARG_STEP, Parcels.wrap(step));

                startActivity(intent);
            }
        }
    };
}
