package np.com.softwarica.foodforall.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.drawee.view.SimpleDraweeView;

import np.com.softwarica.foodforall.R;
import np.com.softwarica.foodforall.models.Dish;

public class DishDetailActivity extends AppCompatActivity {

    private SimpleDraweeView imgDish;
    private TextView tvDishName, tvOrigin, tvDuration, tvIngredient, tvInstruction;
    private Dish dish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);

        imgDish = findViewById(R.id.imgDish);
        tvDishName = findViewById(R.id.tvDishName);
        tvOrigin = findViewById(R.id.tvOrigin);
        tvDuration = findViewById(R.id.tvDuration);
        tvIngredient = findViewById(R.id.tvIngredient);
        tvInstruction = findViewById(R.id.tvInstruction);

        dish = (Dish) getIntent().getSerializableExtra("dish");

        imgDish.setImageURI(dish.getImage());
        tvDishName.setText(dish.getName());
        tvOrigin.setText(dish.getOrigin());
        tvDuration.setText(dish.getDuration());
        tvIngredient.setText(dish.getIngredance());
        tvInstruction.setText(dish.getInstruction());

        findViewById(R.id.imgBack).setOnClickListener(v -> super.onBackPressed());
    }
}
