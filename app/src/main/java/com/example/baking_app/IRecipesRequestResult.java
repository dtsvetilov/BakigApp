package com.example.baking_app;

import com.example.baking_app.data.Recipe;

import java.util.List;

public interface IRecipesRequestResult {
    void onRecipesResult(List<Recipe> result);
}
