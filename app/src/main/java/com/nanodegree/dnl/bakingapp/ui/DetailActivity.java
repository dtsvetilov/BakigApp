package com.nanodegree.dnl.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.nanodegree.dnl.bakingapp.R;
import com.nanodegree.dnl.bakingapp.data.Recipe;

import org.parceler.Parcels;

import butterknife.ButterKnife;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;


public class DetailActivity extends AppCompatActivity implements RecipeDetailFragment.IStepClickListener {

    private boolean mTwoPane;
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        mRecipe = Parcels.unwrap(getIntent().getParcelableExtra("recipe"));


        if (findViewById(R.id.step_detail_container) != null) {
            mTwoPane = true;
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable("recipe", Parcels.wrap(mRecipe));
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setArguments(arguments);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_recipe_detail, recipeDetailFragment)
                    .commit();

        }
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

    @Override
    public void onStepClicked(int stepPosition) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable("recipe", Parcels.wrap(mRecipe));
            arguments.putInt("step_position", stepPosition);
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(DetailActivity.this, StepActivity.class);
            intent.putExtra("recipe", Parcels.wrap(mRecipe));
            intent.putExtra("step_position", stepPosition);

            startActivity(intent);
        }
    }
}
