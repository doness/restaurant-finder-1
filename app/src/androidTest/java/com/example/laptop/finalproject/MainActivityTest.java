package com.example.laptop.finalproject;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.WindowManager;

import com.example.laptop.finalproject.constants.Constants;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

/**
 * Test class for MainActivity, implementation testing with Espresso
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void unlockScreen() {
        final MainActivity activity = activityTestRule.getActivity();
        Runnable wakeUpDevice = new Runnable() {
            public void run() {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        };
        activity.runOnUiThread(wakeUpDevice);
    }


    //Test if all the views are displayed as expected
    @Test
    public void testViewsAreDisplayed() throws Exception {

        onView(withId(R.id.toolbarMain)).check(matches(isDisplayed()));
        onView(withId(R.id.etPostcode)).check(matches(isDisplayed()));
        onView(withId(R.id.btnFindNearby)).check(matches(isDisplayed()));

    }

    //Test if the toolbar functions as expected
    @Test
    public void testToolbar() throws Exception {

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        closeSoftKeyboard();
        Thread.sleep(1000);
        onView(withText(R.string.bulgarian)).perform(click());
        onView(withId(R.id.tvCuisine)).check(matches(withText(Constants.BG_CUISINE_LIST[0])));
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        closeSoftKeyboard();
        Thread.sleep(1000);
        onView(withText(R.string.english)).perform(click());
        onView(withId(R.id.tvCuisine)).check(matches(withText(Constants.EN_CUISINE_LIST[0])));
    }
    //Test if the location checker works
    @Test
    public void testLocationVerification() throws Exception {

        onView(withId(R.id.btnFindNearby)).perform(click());
        onView(withText(Constants.EN_TOAST_INVALID_POSTCODE)).inRoot(withDecorView(not(activityTestRule
                .getActivity()
                .getWindow()
                .getDecorView()))).check(matches(isDisplayed()));

        Thread.sleep(5000);

        onView(withId(R.id.etPostcode)).perform(typeText("ashfbidsb"), closeSoftKeyboard());
        onView(withId(R.id.btnFindNearby)).perform(click());
        onView(withText(Constants.EN_TOAST_INVALID_POSTCODE)).inRoot(withDecorView(not(activityTestRule
                .getActivity()
                .getWindow()
                .getDecorView()))).check(matches(isDisplayed()));

        Thread.sleep(5000);

        onView(withId(R.id.btnLocation)).perform(click());
        onView(withId(R.id.etPostcode)).perform(typeText("ashfbidsb"), closeSoftKeyboard());
        onView(withId(R.id.btnFindNearby)).perform(click());
        onView(withText(Constants.EN_TOAST_ONLY_ONE_INPUT)).inRoot(withDecorView(not(activityTestRule
                .getActivity()
                .getWindow()
                .getDecorView()))).check(matches(isDisplayed()));

    }

    //Test if starting the next activity works
    @Test
    public void testActivityTransition() throws Exception {

        onView(withId(R.id.etPostcode)).perform(typeText("BR1 5AE"), closeSoftKeyboard());
        onView(withId(R.id.btnFindNearby)).perform(click());

        Thread.sleep(5000);
        closeSoftKeyboard();

        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

}