package np.com.softwarica.foodforall.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import np.com.softwarica.foodforall.R;
import np.com.softwarica.foodforall.activities.AddDishActivity;
import np.com.softwarica.foodforall.adapters.MyDishListAdapter;
import np.com.softwarica.foodforall.models.Comment;
import np.com.softwarica.foodforall.models.Dish;
import np.com.softwarica.foodforall.models.Like;

public class MyDishesFragment extends Fragment {

    private FloatingActionButton fabAdd;
    private SwipeRefreshLayout reload;
    private RecyclerView recyclerView;
    private List<Dish> dishList;
    private MyDishListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_dishes, container, false);

        reload = v.findViewById(R.id.reload);
        recyclerView = v.findViewById(R.id.recyclerView);
        fabAdd = v.findViewById(R.id.fabAddDishes);

        dishList = new ArrayList<>();
        adapter = new MyDishListAdapter(getActivity(), dishList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        reload.post(this::loadDishes);
        reload.setOnRefreshListener(this::loadDishes);

        fabAdd.setOnClickListener(view -> getActivity().startActivity(new Intent(getActivity(), AddDishActivity.class)));

        return v;
    }


    private void loadDishes() {
        reload.setRefreshing(true);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("dishes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reload.setRefreshing(false);
                dishList.clear();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Dish dish = snap.getValue(Dish.class);
                    if (dish.getUid().equals(uid)) {
                        if (snap.child("likes").exists()) {
                            List<Like> likes = new ArrayList<>();
                            for (DataSnapshot snapshot : snap.child("likes").getChildren()) {
                                likes.add(snapshot.getValue(Like.class));
                            }
                            dish.setLikeList(likes);
                        }
                        if (snap.child("comments").exists()) {
                            List<Comment> comments = new ArrayList<>();
                            for (DataSnapshot snapshot : snap.child("comments").getChildren()) {
                                comments.add(snapshot.getValue(Comment.class));
                            }
                            dish.setCommentList(comments);
                        }
                        dish.setKey(snap.getKey());
                        dishList.add(dish);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                reload.setRefreshing(false);
            }
        });
    }
}