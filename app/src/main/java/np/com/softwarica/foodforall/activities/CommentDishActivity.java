package np.com.softwarica.foodforall.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import np.com.softwarica.foodforall.R;
import np.com.softwarica.foodforall.adapters.CommentAdapter;
import np.com.softwarica.foodforall.models.Comment;
import np.com.softwarica.foodforall.models.Dish;
import np.com.softwarica.foodforall.utils.SharedUtils;

public class CommentDishActivity extends AppCompatActivity {

    private TextView tvLike, tvComment;
    private EditText etComment;
    private ImageView btnSend;
    private RecyclerView recyclerView;
    private List<Comment> commentList;
    private CommentAdapter adapter;
    private Dish dish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_dish);

        dish = (Dish) getIntent().getSerializableExtra("dish");

        tvLike = findViewById(R.id.tvLike);
        tvComment = findViewById(R.id.tvComment);
        etComment = findViewById(R.id.etComment);
        btnSend = findViewById(R.id.btnSend);
        recyclerView = findViewById(R.id.recyclerView);
        findViewById(R.id.imgBack).setOnClickListener(v -> super.onBackPressed());

        commentList = new ArrayList<>();
        if(dish.getCommentList() != null) {
            commentList.addAll(dish.getCommentList());
        }
        adapter = new CommentAdapter(this, commentList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        if (dish.getLikeList() != null) {
            tvLike.setText(dish.getLikeList().size() + " Likes");
        } else {
            tvLike.setText("0 Like");
        }
        if (dish.getCommentList() != null) {
            tvComment.setText(dish.getCommentList().size() + " Comments");
        } else {
            tvComment.setText("0 Comments");
        }

        btnSend.setOnClickListener(v -> comment());
    }

    private void comment() {
        if (etComment.getText().toString().equals("")) {
            return;
        }
        String myComment = etComment.getText().toString().trim();
        Comment comment = new Comment(SharedUtils.getString(this, "name"), SharedUtils.getString(this, "image"), myComment, String.valueOf(System.currentTimeMillis()));
        FirebaseDatabase.getInstance().getReference().child("dishes").child(dish.getKey()).child("comments").push().setValue(comment);
        commentList.add(comment);
        adapter.notifyDataSetChanged();
        etComment.setText("");
    }
}
