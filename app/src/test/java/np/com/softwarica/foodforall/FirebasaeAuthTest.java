package np.com.softwarica.foodforall;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.when;

public class FirebasaeAuthTest {

    @Test
    public  void FirebaseSigninTest(){

        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        final Task<AuthResult> mockedResult = Mockito.mock(Task.class);
        when(mockAuth.signInWithEmailAndPassword("dangernose@gmail.com","S&#@()$@!"))
                .thenReturn(mockedResult);
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Task<AuthResult> task = invocation.getArgument(0,Task.class);
                Assert.assertEquals(true,task.isComplete());
                return null;
            }
        });
    }

//    ApiClient apiClient = Mockito.mock(ApiClient.class);
//    final Call<Result> mockedLogin = Mockito.mock(Call.class);
//    when(apiClient.Login("admin","admin")).thenReturn(mockedLogin);
//
//        Mockito.doAnswer(new Answer() {
//        @Override
//        public Object answer(InvocationOnMock invocation) throws Throwable {
//            Callback<Result> callback = invocation.getArgument(0,Callback.class);
//            callback.onResponse(mockedLogin, Response.success(new Result()));
}
