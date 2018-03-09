package com.nanodegree.dnl.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nanodegree.dnl.bakingapp.R;
import com.nanodegree.dnl.bakingapp.data.Recipe;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class StepActivity extends AppCompatActivity {
    @BindView(R.id.stepnum_tv)
    TextView stepNumTv;

    Recipe mRecipe;
    private int currentStepPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);


        mRecipe = Parcels.unwrap(getIntent().getParcelableExtra("recipe"));
        currentStepPosition = getIntent().getIntExtra("step_position", 0);
        if (savedInstanceState == null) {
            displayStepFragment();
        }

        populateSteps();
    }

    private void displayStepFragment() {

        Bundle arguments = new Bundle();
        arguments.putParcelable("recipe", Parcels.wrap(mRecipe));
        arguments.putInt("step_position", currentStepPosition);
        StepDetailFragment fragment = new StepDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_detail_container, fragment)
                .commit();
    }

    public void populateSteps() {
        stepNumTv.setText(String.format("%d of %d", currentStepPosition, mRecipe.getSteps().size() - 1));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, DetailActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.previous_btn)
    public void previousBtnClicked(View view) {
        if (currentStepPosition > 0) {
            currentStepPosition--;
            populateSteps();
            displayStepFragment();
        }
    }

    @OnClick(R.id.next_btn)
    public void nextBtnClicked(View view) {
        if (currentStepPosition < (mRecipe.getSteps().size() - 1)) {
            currentStepPosition++;
            populateSteps();
            displayStepFragment();
        }
    }
}
