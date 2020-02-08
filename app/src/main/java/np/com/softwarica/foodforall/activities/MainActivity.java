package np.com.softwarica.foodforall.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import np.com.softwarica.foodforall.R;
import np.com.softwarica.foodforall.fragments.AboutFragment;
import np.com.softwarica.foodforall.fragments.HomeFragment;
import np.com.softwarica.foodforall.fragments.MyDishesFragment;
import np.com.softwarica.foodforall.fragments.MyProfileFragment;
import np.com.softwarica.foodforall.models.User;
import np.com.softwarica.foodforall.utils.FirebaseUtils;
import np.com.softwarica.foodforall.utils.ShakeDetector;
import np.com.softwarica.foodforall.utils.SharedUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private ImageView imgMenu;
    private SimpleDraweeView imgProfile;
    private TextView tvTitle, tvFullName, tvEmail;
    private SensorManager manager;
    private Sensor sensor;
    private ShakeDetector mShakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        imgMenu = findViewById(R.id.imgMenu);
        imgProfile = findViewById(R.id.imgProfile);
        tvTitle = findViewById(R.id.tvTitle);
        tvFullName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);

        imgMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        findViewById(R.id.mnuHome).setOnClickListener(this);
        findViewById(R.id.mnuMyProfile).setOnClickListener(this);
        findViewById(R.id.mnuMyDishes).setOnClickListener(this);
        findViewById(R.id.mnuAboutUs).setOnClickListener(this);
        findViewById(R.id.mnuLogout).setOnClickListener(this);

        loadFragment(new HomeFragment(), "Home");

        loadProfile();

        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(count -> logout());
    }

    private void loadProfile() {
        FirebaseUtils.database.child("users").child(FirebaseUtils.userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                imgProfile.setImageURI(user.getImage());
                tvFullName.setText(user.getFullname());
                tvEmail.setText(user.getEmail());
                SharedUtils.setString(MainActivity.this, "name", user.getFullname());
                SharedUtils.setString(MainActivity.this, "email", user.getEmail());
                SharedUtils.setString(MainActivity.this, "image", user.getImage());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mnuHome:
                loadFragment(new HomeFragment(), "Home");
                break;
            case R.id.mnuMyProfile:
                loadFragment(new MyProfileFragment(), "My Profile");
                break;
            case R.id.mnuMyDishes:
                loadFragment(new MyDishesFragment(), "My Dish");
                break;
            case R.id.mnuAboutUs:
                loadFragment(new AboutFragment(), "About Us");
                break;
            case R.id.mnuLogout:
                logout();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void loadFragment(Fragment fragment, String title) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
        tvTitle.setText(title);
    }

    public void logout() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_for_result, null, false);
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(false);

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvMessage = view.findViewById(R.id.tvMessage);
        Button btnOk = view.findViewById(R.id.btnOK);
        Button btnCancle = view.findViewById(R.id.btnCancle);
        tvTitle.setText("Are you sure ?");
        tvMessage.setText("You want to logout from your account?");

        btnOk.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
        });
        btnCancle.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        manager.registerListener(mShakeDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
        manager.unregisterListener(mShakeDetector);
    }
}
