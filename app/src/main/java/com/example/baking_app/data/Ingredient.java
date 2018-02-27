package com.example.baking_app.data;

import org.parceler.Parcel;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Parcel
public class Ingredient {
    private String id;
    private String measure;
    private String ingredient;
    private float quantity;
}
