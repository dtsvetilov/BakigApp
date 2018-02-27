package com.example.baking_app;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.baking_app.data.Recipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

public class NetworkUtils {
    private static final String RECIPES_ENDPOINT = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static void fetchRecipes(Context context, final IRecipesRequestResult recipesRequestResult) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, RECIPES_ENDPOINT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                List<Recipe> recipes = Arrays.asList(gson.fromJson(response, Recipe[].class));

                recipesRequestResult.onRecipesResult(recipes);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("NetworkUtils", error.toString());

                recipesRequestResult.onRecipesResult(null);
            }
        });

        requestQueue.add(request);
    }
}
