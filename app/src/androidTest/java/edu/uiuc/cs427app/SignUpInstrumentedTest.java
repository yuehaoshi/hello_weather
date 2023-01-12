package edu.uiuc.cs427app;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.ext.truth.content.IntentSubject.assertThat;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

import androidx.test.espresso.intent.Intents;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Iterables;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

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

public class SignUpInstrumentedTest {
    public static final String NAME_TO_BE_TYPED = "ABC";
    public static final String EMAIL_TO_BE_TYPED = "abc@gmail.com";
    public static final String PASSWORD_TO_BE_TYPED = "Illini@abc";
    public static final String PHONE_TO_BE_TYPED = "4479021605";

    public static final String NAME_TO_BE_FAILED = "Serena Raju";
    public static final String EMAIL_TO_BE_FAILED = "sr70@illinois.edu";
    public static final String PASSWORD_TO_BE_FAILED = "Illini@sr70";
    public static final String PHONE_TO_BE_FAILED = "4479021609";
    FirebaseAuth fAuth;
    private View toastView;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test, and close it
     * after test completes. This is a replacement for {@link androidx.test.rule.ActivityTestRule}.
     */
    @Rule public ActivityScenarioRule<Register> activityScenarioRule
            = new ActivityScenarioRule<>(Register.class);

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
    public void signUpTest() throws InterruptedException {
        // Type text and then press the button.
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
        }
        onView(withId(R.id.name))
                .check(matches(isDisplayed()))
                .perform(typeText(NAME_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.email))
                .check(matches(isDisplayed()))
                .perform(typeText(EMAIL_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.password))
                .check(matches(isDisplayed()))
                .perform(typeText(PASSWORD_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.phone))
                .check(matches(isDisplayed()))
                .perform(typeText(PHONE_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.register)).perform(click());

        Thread.sleep(2000);

        assertThat(Iterables.getOnlyElement(Intents.getIntents())).hasComponentClass(
                MainActivity.class);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null){
            // Name, email address, password and phone
            String name = user.getDisplayName();
            String email = user.getEmail();

            assertThat(name, equalTo("ABC"));
            assertThat(email, equalTo("abc@gmail.com"));

        }

        assert user != null;
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("MainActivity", "User account deleted.");
                        }
                    }
                });

    }

}
