package com.nanodegree.dnl.bakingapp.data;

import org.parceler.Parcel;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Parcel
@NoArgsConstructor
public class Recipe{
    private int id;
    private String name;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private int servings;
    private String image;
}
