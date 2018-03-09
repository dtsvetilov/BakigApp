package com.nanodegree.dnl.bakingapp.networking;


import com.nanodegree.dnl.bakingapp.data.Recipe;

import java.util.List;

public interface IDataReceived {
    void onDataReceived(List<Recipe> recipes);
}
