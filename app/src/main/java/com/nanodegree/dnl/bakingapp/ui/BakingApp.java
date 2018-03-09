package com.nanodegree.dnl.bakingapp.ui;

import android.app.Application;

import com.nanodegree.dnl.bakingapp.data.Recipe;

import java.util.ArrayList;
import java.util.List;


public class BakingApp extends Application {
    private static ArrayList<Recipe> recipes = new ArrayList<>();

    public static void clearAllRecipes() {
        recipes.clear();
    }

    public static void addMultipleRecipes(List<Recipe> recipesToAdd) {
        recipes.addAll(recipesToAdd);
    }

    public static List<Recipe> getAllRecipes() {
        return recipes;
    }
}
