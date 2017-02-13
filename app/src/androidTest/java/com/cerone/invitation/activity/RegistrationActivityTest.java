package com.cerone.invitation.activity;

import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.cerone.invitation.R;
import com.cerone.invitation.activities.RegistrationActivity;
import com.cerone.invitation.helpers.InvtAppPreferences;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by adarsht on 02/02/17.
 */

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RegistrationActivityTest extends BaseActivityTest {

    @Rule
    public ActivityTestRule<RegistrationActivity> mActivityTestRule = new ActivityTestRule<>(RegistrationActivity.class);

    @Test
    public void validateInputHappyFlow() {
        InvtAppPreferences.setPref(mActivityTestRule.getActivity().getApplicationContext());
        onView(withId(R.id.editUserName))
                .perform(typeText("Adarsh.T"), closeSoftKeyboard());
        onView(withId(R.id.register)).perform(click());
        onView(withId(R.id.editMobileNumber))
                .perform(typeText("9949257729"), closeSoftKeyboard());
        onView(withId(R.id.register)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.editMobileNumber))
                .perform(typeText("9949257729"), closeSoftKeyboard());
        onView(withId(R.id.signIn_button)).perform(click());
        SystemClock.sleep(10000);
    }
}