package com.nanodegree.dnl.bakingapp.data;

import org.parceler.Parcel;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Parcel
@NoArgsConstructor
public class Step {
    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;
}
