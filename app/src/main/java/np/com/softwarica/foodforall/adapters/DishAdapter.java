package np.com.softwarica.foodforall.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import np.com.softwarica.foodforall.R;
import np.com.softwarica.foodforall.activities.CommentDishActivity;
import np.com.softwarica.foodforall.activities.DishDetailActivity;
import np.com.softwarica.foodforall.models.Dish;
import np.com.softwarica.foodforall.models.Like;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.Holder> {

    private Context context;
    private List<Dish> list;

    public DishAdapter(Context context, List<Dish> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dish, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Dish dish = list.get(position);
        holder.tvFullName.setText(dish.getFullName());
        holder.imgProfile.setImageURI(dish.getUserImage());
        if (dish.getLikeList() != null) {
            holder.tvLike.setText(dish.getLikeList().size() + " Likes");
        } else {
            holder.tvLike.setText("0 Like");
        }
        if (dish.getCommentList() != null) {
            holder.tvComment.setText(dish.getCommentList().size() + " Comments");
        } else {
            holder.tvComment.setText("0 Comments");
        }
        holder.imgDish.setImageURI(dish.getImage());

        if(isAlreadyLiked(dish.getLikeList())){
            holder.iconLike.setImageResource(R.drawable.ic_liked);
        }else{
            holder.iconLike.setImageResource(R.drawable.ic_like);
        }

        holder.viewLike.setOnClickListener(v -> {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if (isAlreadyLiked(dish.getLikeList())) {
                FirebaseDatabase.getInstance().getReference().child("dishes").child(dish.getKey()).child("likes").child(uid).removeValue();
            } else {
                FirebaseDatabase.getInstance().getReference().child("dishes").child(dish.getKey()).child("likes").child(uid).setValue(new Like(FirebaseAuth.getInstance().getCurrentUser().getUid()));
            }
        });
        holder.viewComment.setOnClickListener(v -> {
            Intent intent = new Intent(context, CommentDishActivity.class);
            intent.putExtra("dish", dish);
            context.startActivity(intent);
        });
        holder.imgDish.setOnClickListener(v->{
            Intent intent = new Intent(context, DishDetailActivity.class);
            intent.putExtra("dish", dish);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private boolean isAlreadyLiked(List<Like> likes) {
        if(likes == null) return false;
        for (Like like : likes) {
            if (like.getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                return true;
            }
        }
        return false;
    }

    class Holder extends RecyclerView.ViewHolder {

        private TextView tvLike, tvComment, tvFullName;
        private ImageView iconLike;
        private SimpleDraweeView imgDish, imgProfile;
        private View viewLike, viewComment;

        public Holder(@NonNull View v) {
            super(v);
            tvFullName = v.findViewById(R.id.tvFullName);
            tvLike = v.findViewById(R.id.tvLike);
            tvComment = v.findViewById(R.id.tvComment);
            iconLike = v.findViewById(R.id.iconLike);
            imgDish = v.findViewById(R.id.imgDish);
            imgProfile = v.findViewById(R.id.imgProfile);
            viewLike = v.findViewById(R.id.viewLike);
            viewComment = v.findViewById(R.id.viewComment);
        }
    }
}
