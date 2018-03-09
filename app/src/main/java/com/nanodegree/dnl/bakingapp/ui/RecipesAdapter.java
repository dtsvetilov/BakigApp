package com.nanodegree.dnl.bakingapp.ui;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanodegree.dnl.bakingapp.R;
import com.nanodegree.dnl.bakingapp.data.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {
    private List<Recipe> mData;
    private RecipeClickedHandler mClickHandler;

    public interface RecipeClickedHandler {
        void onRecipeClicked(Recipe recipe);
    }

    public RecipesAdapter(List<Recipe> data, RecipeClickedHandler clickedHandler) {
        mData = data;
        this.mClickHandler = clickedHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recycler_view_item_recipe, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipe = mData.get(position);
        holder.recipe = recipe;
        holder.name.setText(recipe.getName());
        holder.servingsCount.setText(String.format("Servings: %d", recipe.getServings()));


        if (recipe.getImage().isEmpty()) {
            holder.image.setImageResource(R.drawable.ic_recipe_placeholder);
        } else {
            Picasso.with(holder.image.getContext())
                    .load(recipe.getImage())
                    .placeholder(R.drawable.ic_recipe_placeholder)
                    .error(R.drawable.ic_recipe_placeholder)
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.image)
        ImageView image;

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.servings_count)
        TextView servingsCount;

        Recipe recipe;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.onRecipeClicked(recipe);
        }

    }


}
