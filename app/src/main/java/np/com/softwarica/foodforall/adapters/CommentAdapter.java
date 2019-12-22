package np.com.softwarica.foodforall.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import np.com.softwarica.foodforall.R;
import np.com.softwarica.foodforall.models.Comment;
import np.com.softwarica.foodforall.utils.TimeUtils;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Holder> {

    private Context context;
    private List<Comment> list;

    public CommentAdapter(Context context, List<Comment> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Comment comment = list.get(position);
        holder.tvName.setText(comment.getName());
        holder.tvDate.setText(TimeUtils.getTimeAgo(comment.getTimestamp()));
        holder.tvComment.setText(comment.getComment());
        holder.imgProfile.setImageURI(comment.getImage());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        private TextView tvName, tvDate, tvComment;
        private SimpleDraweeView imgProfile;

        public Holder(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvComment = v.findViewById(R.id.tvComment);
            tvDate = v.findViewById(R.id.tvDate);
            imgProfile = v.findViewById(R.id.imgProfile);
        }
    }
}
