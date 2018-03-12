package com.nanodegree.dnl.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.nanodegree.dnl.bakingapp.ui.WidgetActivity;

public class BakingAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, String[] recipe) {

        Intent intent = new Intent(context, WidgetActivity.class);
        intent.putExtra(WidgetActivity.WIDGET_RECIPE, recipe);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        if (!recipe[0].equals("0")) {
            views.setTextViewText(R.id.recipe_name, recipe[1]);
            views.setTextViewText(R.id.ingredients_text, recipe[2]);
        } else {
            views.setTextViewText(R.id.recipe_name, context.getString(R.string.app_name));
            views.setTextViewText(R.id.ingredients_text, context.getString(R.string.appwidget_text));
        }

        views.setOnClickPendingIntent(R.id.ingredients_text, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        String[] recipe = new String[]{"0", "", context.getString(R.string.appwidget_text)};
        updateWidget(context, appWidgetManager, appWidgetIds, recipe);
    }

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, String[] recipe) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

