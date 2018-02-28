package com.example.baking_app.data;

import org.parceler.Parcel;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Parcel
public class Step {
    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;
}