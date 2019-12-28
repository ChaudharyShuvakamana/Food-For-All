package np.com.softwarica.foodforall;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.database.FirebaseDatabase;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
