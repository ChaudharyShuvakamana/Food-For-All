package np.com.softwarica.foodforall;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import np.com.softwarica.foodforall.activities.LoginActivity;

import static org.junit.Assert.assertNotNull;

public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule =new  ActivityTestRule<LoginActivity>(LoginActivity.class);
    public LoginActivity loginActivity = null;

    @Before
    public void setUp() throws Exception {
    loginActivity = activityTestRule.getActivity();
    }

    @Test
    public  void ComponentTest(){
        assertNotNull(loginActivity.etEmail);
        assertNotNull(loginActivity.etPassword);
    }

    @After
    public void tearDown() throws Exception {
    }
}