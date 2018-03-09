package com.nanodegree.dnl.bakingapp.ui;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.nanodegree.dnl.bakingapp.R;
import com.nanodegree.dnl.bakingapp.data.Ingredient;
import com.nanodegree.dnl.bakingapp.data.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WidgetActivity extends AppCompatActivity {

    public static final String WIDGET_RECIPE = "recipe_widget";
    public static final String WIDGET_INGREDIENTS = "widget_ingredients";

    final Context mContext = this;

    private int mAppWidgetId;
    public List<Recipe> mRecipeList;
    private String[] mWidgetRecipe;
    int mPrevRecipeId;

    @BindView(R.id.recipe_options_rg)
    RadioGroup mRadioGroupRecipe;
    @BindView(R.id.choose_recipe_btn)
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.activity_widget);
        ButterKnife.bind(this);

        if (BakingApp.getAllRecipes() == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        mRecipeList = BakingApp.getAllRecipes();
        mWidgetRecipe = new String[3];

        processIntent();
        displayRecipeList();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                processWidgetRecipe();
            }
        });
    }


    public void displayRecipeList() {
        RadioGroup.LayoutParams mLayoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RadioButton[] radioButtonsArray = new RadioButton[mRecipeList.size()];

        int i = 0;
        for (Recipe recipe : mRecipeList) {
            radioButtonsArray[i] = new RadioButton(this);
            mRadioGroupRecipe.addView(radioButtonsArray[i]);
            radioButtonsArray[i].setText(recipe.getName());
            radioButtonsArray[i].setTag(recipe.getId());
            mLayoutParams.setMargins(20, 20, 20, 20);
            radioButtonsArray[i].setLayoutParams(mLayoutParams);
            radioButtonsArray[i].setPadding(30, 0, 0, 0);

            if (mPrevRecipeId != 0) {
                if (mPrevRecipeId == recipe.getId()) {
                    radioButtonsArray[i].setChecked(true);
                }
            }
        }
    }


    public void processWidgetRecipe() {
        int selectId = mRadioGroupRecipe.getCheckedRadioButtonId();
        RadioButton mRadioButton = findViewById(selectId);

        if (mRadioButton != null) {
            mWidgetRecipe[0] = mRadioButton.getTag().toString();

            mWidgetRecipe[1] = mRadioButton.getText().toString();

            int id = Integer.valueOf(mWidgetRecipe[0]) - 1;
            List<Ingredient> mIngredients = mRecipeList.get(id).getIngredients();

            StringBuilder ingredientDisplayString = new StringBuilder();

            for (Ingredient ingredient : mIngredients) {
                ingredientDisplayString.append(String.format("%s %s %s", ingredient.getIngredient(),
                        Double.toString(ingredient.getQuantity()), ingredient.getMeasure().toLowerCase()));
            }
            mWidgetRecipe[2] = ingredientDisplayString.toString();

            BakingAppWidgetService.startActionUpdateWidget(mContext, mWidgetRecipe);
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();

        } else {
            mWidgetRecipe[0] = "0";
            mWidgetRecipe[1] = "";
            mWidgetRecipe[2] = this.getString(R.string.appwidget_text);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processIntent();
    }


    private void processIntent() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String[] previousRecipe = intent.getExtras().getStringArray(WIDGET_RECIPE);
            mPrevRecipeId = (previousRecipe != null) ? Integer.valueOf(previousRecipe[0]) : 0;
        }
    }
}
