package edu.uiuc.cs427app;

        import static androidx.test.espresso.Espresso.onView;
        import static androidx.test.espresso.action.ViewActions.click;
        import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
        import static androidx.test.espresso.action.ViewActions.typeText;
        import static androidx.test.espresso.assertion.ViewAssertions.matches;
        import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
        import static androidx.test.espresso.matcher.ViewMatchers.withId;
        import static androidx.test.espresso.matcher.ViewMatchers.withText;
        import static androidx.test.ext.truth.content.IntentSubject.assertThat;

        import android.content.Context;

        import androidx.test.espresso.intent.Intents;
        import androidx.test.ext.junit.rules.ActivityScenarioRule;
        import androidx.test.platform.app.InstrumentationRegistry;
        import androidx.test.ext.junit.runners.AndroidJUnit4;
        import androidx.test.rule.ActivityTestRule;

        import org.junit.After;
        import org.junit.Assert;
        import org.junit.Before;
        import org.junit.Rule;
        import org.junit.Test;
        import org.junit.runner.RunWith;

        import static org.hamcrest.CoreMatchers.equalTo;
        import static org.junit.Assert.*;

        import com.google.common.collect.Iterables;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LogOutInstrumentedTest {


    @Rule
    public ActivityScenarioRule<Register> activityScenarioRule
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

    /**
     * This test logs in a test user and then the tester clicks logs out. The test passes if
     * the app goes back to the login screen.
     * @throws InterruptedException
     */
    @Test
    public void userLogoutTest() throws InterruptedException {

        // Context of the app under test.
        // Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
        }
        Thread.sleep(2000);
        onView(withId(R.id.to_login)).perform(click());

        Thread.sleep(2000);

        onView(withId(R.id.emaila))
                .perform(typeText("sr70@illinois.edu"), closeSoftKeyboard());
        Thread.sleep(2000);

        onView(withId(R.id.passworda))
                .perform(typeText("sr70Illini"), closeSoftKeyboard());
        Thread.sleep(2000);

        onView(withId(R.id.login)).perform(click());
        // tester logged in
        Thread.sleep(2000);

        // tester clicks logout
        onView(withId(R.id.button)).perform(click());
        Thread.sleep(200);
        // tester should be redirected to login page
        onView(withId(R.id.log_text)).check(matches(withText("Log-in")));
    }


}
