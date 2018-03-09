package com.nanodegree.dnl.bakingapp.data;

import org.parceler.Parcel;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Parcel
@NoArgsConstructor
public class Ingredient {
    private double quantity;
    private String measure;
    private String ingredient;
}
