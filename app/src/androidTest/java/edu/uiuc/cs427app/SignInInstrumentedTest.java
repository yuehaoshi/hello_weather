package edu.uiuc.cs427app;


import android.app.Activity;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.annotation.NonNull;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import static androidx.test.ext.truth.content.IntentSubject.assertThat;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

import androidx.test.espresso.intent.Intents;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Iterables;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Basic tests showcasing simple view matchers and actions like {@link ViewMatchers#withId},
 * {@link ViewActions#click} and {@link ViewActions#typeText}.
 * <p>
 * Note that there is no need to tell Espresso that a view is in a different {@link Activity}.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest

public class SignInInstrumentedTest {

    public static final String EMAIL_TO_BE_TYPED = "sr70@illinois.edu";
    public static final String PASSWORD_TO_BE_TYPED = "sr70Illini";

    public static final String EMAIL_TO_BE_FAILED = "sr70@illinois.edu";
    public static final String PASSWORD_TO_BE_FAILED = "sr70@Illini";



    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test, and close it
     * after test completes. This is a replacement for {@link androidx.test.rule.ActivityTestRule}.
     */
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

    @Test
    public void signInTest() throws InterruptedException {

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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Name, email address, password and phone
            String name = user.getDisplayName();
            String email = user.getEmail();

            assertThat(name, equalTo("sr70"));
            assertThat(email, equalTo("sr70@illinois.edu"));


        }
        FirebaseAuth.getInstance().signOut();

    }




}