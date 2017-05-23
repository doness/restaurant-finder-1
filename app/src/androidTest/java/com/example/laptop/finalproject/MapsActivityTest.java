package com.example.laptop.finalproject;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MapsActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testMapActivity() throws Exception {

        onView(withId(R.id.etPostcode)).perform(typeText("SE1 3QX"), closeSoftKeyboard());
        onView(withId(R.id.btnFindNearby)).perform(click());

        Thread.sleep(8000);

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        closeSoftKeyboard();
        Thread.sleep(1000);
        onView(withText(R.string.toggle_list_view)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.rvRestaurantList)).check(matches(isDisplayed()));
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.toggle_list_view)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.map)).check(matches(isDisplayed()));

        Thread.sleep(1000);

        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("Chicken Cottage"));

        marker.click();
        Thread.sleep(1000);
        marker.click();
        Thread.sleep(1000);
        onView(withId(R.id.tvCostText)).check(matches(isDisplayed()));
        Thread.sleep(1000);

        UiObject button1 = device.findObject(new UiSelector().textContains("Reviews"));
        button1.click();
        Thread.sleep(1000);
        onView(withId(R.id.rvUserReviews)).check(matches(isDisplayed()));
        Thread.sleep(1000);

        onView(withId(R.id.rvUserReviews)).perform(scrollToPosition(2));
        Thread.sleep(2000);

        onView(withText("Meryem")).check(matches(isDisplayed()));
        Thread.sleep(1000);
        UiObject button2 = device.findObject(new UiSelector().textContains("Menu"));
        button2.click();
        Thread.sleep(2000);

        onView(withId(R.id.wvMenu)).check(matches(isDisplayed()));
        Thread.sleep(1000);
    }
}
