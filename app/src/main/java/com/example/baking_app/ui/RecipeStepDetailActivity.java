package com.example.baking_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.baking_app.R;

import butterknife.BindView;

public class RecipeStepDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (!intent.hasExtra(RecipeStepDetailFragment.ARG_STEP) || !intent.hasExtra(RecipeStepDetailFragment.ARG_RECIPE)) {
                Toast.makeText(this, getString(R.string.recipe_step_not_found_error), Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            Bundle arguments = new Bundle();
            arguments.putParcelable(RecipeStepDetailFragment.ARG_STEP, getIntent().getParcelableExtra(RecipeStepDetailFragment.ARG_STEP));
            arguments.putParcelable(RecipeStepDetailFragment.ARG_RECIPE, getIntent().getParcelableExtra(RecipeStepDetailFragment.ARG_RECIPE));
            RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, RecipeDetailActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
