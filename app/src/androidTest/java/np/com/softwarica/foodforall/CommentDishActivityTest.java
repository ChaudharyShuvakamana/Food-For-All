package np.com.softwarica.foodforall;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import np.com.softwarica.foodforall.activities.CommentDishActivity;

import static org.junit.Assert.assertNotNull;

public class CommentDishActivityTest {
    @Rule
    public ActivityTestRule<CommentDishActivity> activityTestRule =new  ActivityTestRule<CommentDishActivity>(CommentDishActivity.class);
    public CommentDishActivity commentDishActivity = null;

    @Before
    public void setUp() throws Exception {
        commentDishActivity = activityTestRule.getActivity();
    }

    @Test
    public  void ComponentTest(){
        assertNotNull(commentDishActivity);
        assertNotNull(commentDishActivity);
    }

    @After
    public void tearDown() throws Exception {
    }
}