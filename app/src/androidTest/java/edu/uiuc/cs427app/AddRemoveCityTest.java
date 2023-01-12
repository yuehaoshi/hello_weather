package edu.uiuc.cs427app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.equalTo;
import static androidx.test.ext.truth.content.IntentSubject.assertThat;

import androidx.test.espresso.contrib.RecyclerViewActions;
import com.google.common.collect.Iterables;
import androidx.test.espresso.intent.Intents;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.Root;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import android.os.IBinder;
import android.view.WindowManager;
import android.view.View;
import org.hamcrest.Matcher;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class AddRemoveCityTest {
    // Launches Main Activity

    @Rule
    public ActivityScenarioRule<Login> activityScenarioRule
            = new ActivityScenarioRule<>(Login.class);

    @Before
    public void intentsInit() {
        // initialize Espresso Intents capturing
        Intents.init();
    }

    @After
    public void intentsTeardown() {
        // release Espresso Intents capturing
        Intents.release();
    }


    public static final String EMAIL_TO_BE_TYPED = "sr70@illinois.edu";
    public static final String PASSWORD_TO_BE_TYPED = "sr70Illini";


    private String testCity = "London";

    @Test
    public void checkAddCity() throws InterruptedException {
        onView(withId(R.id.emaila))
                .check(matches(isDisplayed()))
                .perform(typeText(EMAIL_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.passworda))
                .check(matches(isDisplayed()))
                .perform(typeText(PASSWORD_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        Thread.sleep(2000);


        assertThat(Iterables.getOnlyElement(Intents.getIntents())).hasComponentClass(
                MainActivity.class);

        //click the add location button
        onView(withId(R.id.buttonAddLocation)).perform(click());
        Thread.sleep(1000);

        //type in the city name
        onView(withId(R.id.enter_city_name)).perform(typeText(testCity), closeSoftKeyboard());
        Thread.sleep(1000);

        //click the confirm add location button
        onView(withId(R.id.addLocationButton)).perform(click());
        Thread.sleep(1000);

        //check if the city added matches the entered city name
        onView(withId(R.id.city_display)).check(matches(withText(testCity)));
        Thread.sleep(1000);

        //click the back to list button
        onView(withId(R.id.backToList)).perform(click());
        Thread.sleep(1000);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void checkRemoveCity() throws InterruptedException {
        onView(withId(R.id.emaila))
                .check(matches(isDisplayed()))
                .perform(typeText(EMAIL_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.passworda))
                .check(matches(isDisplayed()))
                .perform(typeText(PASSWORD_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        Thread.sleep(2000);


        assertThat(Iterables.getOnlyElement(Intents.getIntents())).hasComponentClass(
                MainActivity.class);

        //click the add location button
        onView(withId(R.id.buttonAddLocation)).perform(click());
        Thread.sleep(1000);

        //type in the city name
        onView(withId(R.id.enter_city_name)).perform(typeText(testCity), closeSoftKeyboard());
        Thread.sleep(1000);

        //click the confirm add location button
        onView(withId(R.id.addLocationButton)).perform(click());
        Thread.sleep(1000);

        //click the back to list button
        onView(withId(R.id.backToList)).perform(click());
        Thread.sleep(1000);

        //click on the delete button of the selected city
        onView(withId(R.id.recycler1))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(testCity)),
                        MyViewAction.clickChildViewWithId(R.id.buttonDeletion)));
        Thread.sleep(1000);

        //check of the toast message is displayed
        String delConfirmStr = testCity + " has been removed from the list.";
        onView(withText(delConfirmStr)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
        Thread.sleep(1000);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth.getInstance().signOut();
    }
}

/**
 * This function allows the user to select a child in the recycler view with an ID
 * @param
 * @return
 */
class MyViewAction {

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }
}

/**
 * This function allows the user to select a child in the recycler view with an ID
 * @param
 * @return
 */
class ToastMatcher extends TypeSafeMatcher<Root> {

    @Override
    public void describeTo(Description description) {
        description.appendText("is toast");
    }

    @Override
    public boolean matchesSafely(Root root) {
        int type = root.getWindowLayoutParams().get().type;
        if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
            IBinder windowToken = root.getDecorView().getWindowToken();
            IBinder appToken = root.getDecorView().getApplicationWindowToken();
            if (windowToken == appToken) {
                return true;
            }
        }
        return false;
    }
}