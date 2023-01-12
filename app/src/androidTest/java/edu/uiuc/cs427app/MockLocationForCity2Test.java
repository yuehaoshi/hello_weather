package edu.uiuc.cs427app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MockLocationForCity2Test {
    Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MapPage.class)
            .putExtra("city", "Chicago")
            .putExtra("latitude", "41.88")
            .putExtra("longitude", "-87.63");

    @Rule
    public ActivityScenarioRule<MapPage> activityScenarioRule =
            new ActivityScenarioRule<>(intent);

    @Test
    // Test the map is displayed on the map page.
    public void TestMockingChampaignDisplaysTheMapForChampaign() throws InterruptedException {
        onView(withId(R.id.map)).check(matches(isDisplayed()));
        Thread.sleep(2000);

        intent.removeExtra("city");
        intent.removeExtra("latitude");
        intent.removeExtra("longitude");

        intent.putExtra("city", "Champaign")
                .putExtra("latitude", "40.12")
                .putExtra("longitude", "-88.24");
        activityScenarioRule.getScenario().recreate();
        onView(withId(R.id.map)).check(matches(isDisplayed()));
        Thread.sleep(2000);
    }

    @Test
    // Test city description is displayed on the map page.
    public void TestMockingChampaignDisplaysCityDescriptionForChampaign() throws InterruptedException {
        onView(withId(R.id.city_name_lat_lon)).check(matches(isDisplayed()));
        Thread.sleep(2000);
        onView(withId(R.id.city_name_lat_lon))
                .check(matches(withText("  Chicago(41.88°N,-87.63°E)")));
        Thread.sleep(2000);

        intent.removeExtra("city");
        intent.removeExtra("latitude");
        intent.removeExtra("longitude");

        intent.putExtra("city", "Champaign")
                .putExtra("latitude", "40.12")
                .putExtra("longitude", "-88.24");
        activityScenarioRule.getScenario().recreate();
        onView(withId(R.id.city_name_lat_lon)).check(matches(isDisplayed()));
        Thread.sleep(2000);
    }
}