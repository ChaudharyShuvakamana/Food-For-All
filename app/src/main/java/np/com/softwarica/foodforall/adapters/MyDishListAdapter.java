package np.com.softwarica.foodforall.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import np.com.softwarica.foodforall.R;
import np.com.softwarica.foodforall.activities.AddDishActivity;
import np.com.softwarica.foodforall.models.Dish;

public class MyDishListAdapter extends RecyclerView.Adapter<MyDishListAdapter.Holder> {

    private Context context;
    private List<Dish> list;

    public MyDishListAdapter(Context context, List<Dish> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_dish, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Dish dish = list.get(position);
        holder.tvDishName.setText(dish.getName());
        holder.tvDuration.setText(dish.getDuration());
        holder.tvIngredient.setText(dish.getIngredance());
        holder.tvOrigin.setText(dish.getOrigin());
        holder.imgDish.setImageURI(dish.getImage());
        holder.itemView.setOnClickListener(v -> chooseOption(dish));
    }

    public void chooseOption(Dish dish) {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
        myAlertDialog.setTitle("Choose Option");
        myAlertDialog.setMessage("Please choose option");

        myAlertDialog.setPositiveButton("Edit", (arg0, arg1) -> {
            Intent intent = new Intent(context, AddDishActivity.class);
            intent.putExtra("dish", dish);
            context.startActivity(intent);
        });

        myAlertDialog.setNegativeButton("Delete", (arg0, arg1) -> {
            FirebaseDatabase.getInstance().getReference().child("dishes").child(dish.getKey()).removeValue();
            Toast.makeText(context, "Recipe Deleted Successfully!!!", Toast.LENGTH_SHORT).show();
        });
        myAlertDialog.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        private TextView tvDishName, tvOrigin, tvDuration, tvIngredient;
        ;
        private SimpleDraweeView imgDish;

        public Holder(@NonNull View v) {
            super(v);
            tvIngredient = v.findViewById(R.id.tvIngredient);
            tvOrigin = v.findViewById(R.id.tvOrigin);
            tvDishName = v.findViewById(R.id.tvDishName);
            tvDuration = v.findViewById(R.id.tvDuration);
            imgDish = v.findViewById(R.id.imgDish);
        }
    }
}
