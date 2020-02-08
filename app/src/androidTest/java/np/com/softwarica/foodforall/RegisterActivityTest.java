package np.com.softwarica.foodforall;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import np.com.softwarica.foodforall.activities.RegisterActivity;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class RegisterActivityTest {
    @Rule
    public ActivityTestRule<RegisterActivity> activity_resisterActivityTestRule = new ActivityTestRule<>(RegisterActivity.class);
    //    public ActivityTestRule<Activity_login> activityTestRule = new ActivityTestRule<>(Activity_login.class);
//
    @Test
    public void registervalidation() {
        onView(withId(R.id.etFullName)).perform(typeText( "shuvakamana chaudhary" ));
        closeSoftKeyboard();
        onView(withId(R.id.etEmail)).perform(typeText( "test@gmail.com" ));
        closeSoftKeyboard();
        onView(withId(R.id.etPassword)).perform(typeText( "password" ));
        closeSoftKeyboard();
        onView(withId(R.id.etConfirmPassword)).perform(typeText( "password" ));
        closeSoftKeyboard();
        onView(withId(R.id.btnResister)).perform(click());
    }
}
