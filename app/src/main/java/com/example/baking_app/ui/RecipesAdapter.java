/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.baking_app.ui;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baking_app.IRecipeClickListener;
import com.example.baking_app.R;
import com.example.baking_app.data.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesAdapterViewHolder> {

    private List<Recipe> mRecipeData;
    final private IRecipeClickListener mClickHandler;

    public RecipesAdapter(IRecipeClickListener clickHandler) {
        mClickHandler = clickHandler;
    }

    public class RecipesAdapterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_iv)
        ImageView imageIv;

        @BindView(R.id.title_tv)
        TextView titleTv;

        public RecipesAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindToData(Recipe recipe) {
            titleTv.setText(recipe.getName());

            Uri imageUri = recipe.getImageUri();
            Picasso.with(imageIv.getContext())
                    .load(imageUri)
                    .placeholder(R.drawable.ic_recipe_placeholder)
                    .into(imageIv);

            itemView.setOnClickListener(view -> mClickHandler.onRecipeClick(recipe));
        }
    }

    @Override
    public RecipesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_item_recipe, viewGroup, false);
        return new RecipesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipesAdapterViewHolder recipesAdapterViewHolder, int position) {
        Recipe recipe = mRecipeData.get(position);
        recipesAdapterViewHolder.bindToData(recipe);
    }

    @Override
    public int getItemCount() {
        if (mRecipeData == null)
            return 0;

        return mRecipeData.size();
    }

    public void setRecipeData(List<Recipe> mRecipeData) {
        this.mRecipeData = mRecipeData;
        this.notifyDataSetChanged();
    }
}