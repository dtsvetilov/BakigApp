package com.nanodegree.dnl.bakingapp;


import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.nanodegree.dnl.bakingapp.ui.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private final static int RECIPE_LIST_SCROLL_POSITION = 1;
    private final static int STEPS_LIST_SCROLL_POSITION = 0;
    private final static String STEP_NAME = "0)";
    private final static String STEP_DESC = "Recipe Introduction";
    private final static String RECIPE_NAME = "Brownies";

    private IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void testClickRecipeAtPosition() throws Exception {

        // Click on Recipes RecyclerView
        onView(withId(R.id.recipes_rv))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(RECIPE_LIST_SCROLL_POSITION, click()));

        // Check if ingredients are displayed
        onView(withId(R.id.ingredients))
                .check(matches(isDisplayed()));

        // Perform scroll action on Steps RecyclerView
        onView(withId(R.id.recipe_steps_rv))
                .perform(RecyclerViewActions
                        .scrollToPosition(STEPS_LIST_SCROLL_POSITION));

        // Check step name on the specified position of Steps RecyclerView
        onView(withText(STEP_NAME))
                .check(matches(isDisplayed()));

        // Perform click action on steps list
        onView(withId(R.id.recipe_steps_rv))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(STEPS_LIST_SCROLL_POSITION, click()));

        // Check if all views are displayed as expected in StepsActivity (after step item is clicked)
        //Only if step navigation is visible
        //onView(withId(R.id.previous_btn)).check(matches(isDisplayed()));
        //onView(withId(R.id.next_btn)).check(matches(isDisplayed()));
        //onView(withId(R.id.step_desc_tv)).check(matches(isDisplayed()));
        //onView((withId(R.id.step_desc_tv))).check(matches(withText(STEP_DESC)));
        onView(withId(R.id.player)).check(matches(isDisplayed()));
        onView(withId(R.id.no_media_iv)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testRecipeNameAtPosition() {

        // Perform scroll action on Recipe RecyclerView
        onView(withId(R.id.recipes_rv))
                .perform(RecyclerViewActions
                        .scrollToPosition(RECIPE_LIST_SCROLL_POSITION));

        // Check recipe name on the specified position of Recipes RecyclerView
        onView(withText(RECIPE_NAME))
                .check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
