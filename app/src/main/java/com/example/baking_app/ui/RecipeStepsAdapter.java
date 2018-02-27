package com.example.baking_app.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baking_app.IRecipeStepClickListener;
import com.example.baking_app.R;
import com.example.baking_app.data.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.ViewHolder> {

    private List<Step> mSteps;
    private final IRecipeStepClickListener mRecipeStepClickListener;
    private final boolean mTwoPane;


    RecipeStepsAdapter(List<Step> steps, boolean twoPane, IRecipeStepClickListener mRecipeStepClickListener) {
        this.mSteps = steps;
        this.mRecipeStepClickListener = mRecipeStepClickListener;
        mTwoPane = twoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_recipe_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Step step = mSteps.get(position);
        holder.bindToData(step);
    }

    @Override
    public int getItemCount() {
        if (mSteps == null)
            return 0;

        return mSteps.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.id_text)
        TextView mIdView;

        @BindView(R.id.content)
        TextView mContentView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindToData(Step step) {
            mIdView.setText(step.getShortDescription());

            itemView.setOnClickListener(view -> mRecipeStepClickListener.onRecipeStepClick(step));
        }
    }
}