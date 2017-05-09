package com.example.laptop.finalproject;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.laptop.finalproject.constants.Constants;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Test class for MainActivity, implementation testing with Espresso
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    //Test if all the views are displayed as expected
    @Test
    public void testViewsAreDisplayed() throws Exception {

        onView(withId(R.id.toolbarMain)).check(matches(isDisplayed()));
        onView(withId(R.id.etPostcode)).check(matches(isDisplayed()));
        onView(withId(R.id.tvOr)).check(matches(isDisplayed()));
        onView(withId(R.id.swUseMyLocation)).check(matches(isDisplayed()));
        onView(withId(R.id.tvCuisine)).check(matches(isDisplayed()));
        onView(withId(R.id.spCuisine)).check(matches(isDisplayed()));
        onView(withId(R.id.tvCategory)).check(matches(isDisplayed()));
        onView(withId(R.id.spCategory)).check(matches(isDisplayed()));
        onView(withId(R.id.tvPrice)).check(matches(isDisplayed()));
        onView(withId(R.id.spPrice)).check(matches(isDisplayed()));
        onView(withId(R.id.tvRating)).check(matches(isDisplayed()));
        onView(withId(R.id.spRating)).check(matches(isDisplayed()));
        onView(withId(R.id.btnFindNearby)).check(matches(isDisplayed()));
        onView(withId(R.id.tvFilters)).check(matches(isDisplayed()));

    }

    //Test if the toolbar functions as expected
    @Test
    public void testToolbar() throws Exception {

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.bulgarian)).perform(click());
        onView(withId(R.id.tvOr)).check(matches(withText(Constants.BG_OR)));
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.english)).perform(click());
        onView(withId(R.id.tvOr)).check(matches(withText(Constants.EN_OR)));
    }
}