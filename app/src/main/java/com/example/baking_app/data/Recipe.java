package com.example.baking_app.data;

import android.net.Uri;

import org.parceler.Parcel;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Parcel
public class Recipe {
    private String id;
    private String servings;
    private String name;
    private String image;

    private List<Ingredient> ingredients;
    private List<Step> steps;

    public Uri getImageUri() {
        Uri uri = Uri.parse(image);
        return uri;
    }
}

