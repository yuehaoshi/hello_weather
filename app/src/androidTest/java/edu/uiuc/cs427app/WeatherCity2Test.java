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

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class WeatherCity2Test {
    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), WeatherPage.class);
        intent.putExtra("city", "Chicago");
        intent.putExtra("latitude", "41.878");
        intent.putExtra("longitude", "-87.630");
    }

    @Rule
    public ActivityScenarioRule<WeatherPage> activityScenarioRule = new ActivityScenarioRule<>(intent);

    @Test
    public void TestWeatherDisplay() throws InterruptedException {
        onView(withId(R.id.cityName)).check(matches(isDisplayed()));
        onView(withId(R.id.date)).check(matches(isDisplayed()));
        onView(withId(R.id.currentTemp)).check(matches(isDisplayed()));
        onView(withId(R.id.weatherDesc)).check(matches(isDisplayed()));
        onView(withId(R.id.humidity)).check(matches(isDisplayed()));
        onView(withId(R.id.windCondition)).check(matches(isDisplayed()));
        Thread.sleep(2000);
    }

    @Test
    public void TestWeatherLocation() throws InterruptedException {
        onView(withId(R.id.cityName)).check(matches(withText("City: Chicago")));
        Thread.sleep(2000);
    }

    @Test
    public void TestWeatherDate() throws InterruptedException {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy HH:mm", Locale.getDefault());
        String formattedDate = df.format(c);
        String dateStr = "Time: " + formattedDate;
        onView(withId(R.id.date)).check(matches(withText(dateStr)));
        Thread.sleep(2000);
    }

    @Test
    public void TestWeatherDetails() throws JSONException, InterruptedException {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=41.878&lon=-87.630&exclude=hourly,daily&appid=e1dca6030c2a3a4bc4d94f398c03cb10";
        // Call API and store output into "result"
        String result = weatherHTTPClient.getWeatherData(url);
        JSONObject obj = new JSONObject(result);
        JSONObject main = obj.getJSONObject("main");
        JSONObject weatherDetail = obj.getJSONArray("weather").getJSONObject(0);
        JSONObject windDesc = obj.getJSONObject("wind");

        // Locate information from parsed file and generate output string for each TextView
        String temp = "Temperature: " + main.getString("temp") + "K";
        String desc = "Weather: " + weatherDetail.getString("description");
        String hum = "Humidity: " + main.getInt("humidity") + "%";
        String windSpeed = "Wind Speed: " + windDesc.getDouble("speed") + "m/s";
        onView(withId(R.id.currentTemp)).check(matches(withText(temp)));
        onView(withId(R.id.weatherDesc)).check(matches(withText(desc)));
        onView(withId(R.id.humidity)).check(matches(withText(hum)));
        onView(withId(R.id.windCondition)).check(matches(withText(windSpeed)));
        Thread.sleep(2000);
    }
}