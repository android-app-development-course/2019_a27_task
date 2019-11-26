package cs.android.task.fragment.friend;


/*
 * @author sam
 * @date 19-11-25 下午3:46
 */



import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import cs.android.task.R;
import cs.android.task.entity.Friend;



public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private List<Friend> mFriendList;
    private Context context;
    private BottomSheetDialog bsd1;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView introduction;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.friend_name);
            introduction = itemView.findViewById(R.id.friend_introduction);
            image = itemView.findViewById(R.id.friend_image);
        }

    }

    public FriendAdapter(List<Friend> friendList, Context context) {
        mFriendList = friendList;
        this.context = context;
    }

    @NonNull
    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_row, parent, false);
        FriendAdapter.ViewHolder holder = new FriendAdapter.ViewHolder(view);
        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull FriendAdapter.ViewHolder holder, int position) {
        Friend friend = mFriendList.get(position);
        holder.name.setText(friend.getName());
        holder.introduction.setText(friend.getIntroduction());
        holder.image.setImageBitmap(friend.getImage());



        holder.itemView.setOnClickListener(v -> {
            final BottomSheetDialog dialog = new BottomSheetDialog(context);
            View view = LayoutInflater.from(context).inflate(R.layout.friend_bottom_sheet_list, null);
            dialog.setContentView(view);
            dialog.show();

        });

    }

    @Override
    public int getItemCount() {
        return mFriendList.size();
    }

}
