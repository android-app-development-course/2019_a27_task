package cs.android.task.fragment.friend;


/*
 * @author sam
 * @date 19-11-25 下午3:46
 */

import android.content.Context;
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

import cs.android.task.MyApplication;
import cs.android.task.R;
import cs.android.task.entity.Friend;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import task.FriendServiceGrpc;
import task.Login;
import task.ProfileOuterClass;


public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private List<Friend> mFriendList;
    private Context context;
    private String host ;
    private int port = 50050;
    private ProfileOuterClass.Profile myProfile;


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView introduction;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.friend_name);
            introduction = itemView.findViewById(R.id.friend_introduction);
            image = itemView.findViewById(R.id.friend_image);

            MyApplication myApplication = new MyApplication();
            host = myApplication.getHost();

        }

    }


    public FriendAdapter(List<Friend> friendList, Context context, ProfileOuterClass.Profile myProfile) {
        mFriendList = friendList;
        this.context = context;
        this.myProfile = myProfile;
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
        holder.introduction.setText(friend.getPhoneNumber());
        holder.image.setImageBitmap(friend.getImage());



        holder.itemView.setOnClickListener(v -> {
            final BottomSheetDialog dialog = new BottomSheetDialog(context);
            View view = LayoutInflater.from(context).inflate(R.layout.friend_bottom_sheet_list, null);

            dialog.setContentView(view);
            dialog.show();

            view.findViewById(R.id.friend_delete).setOnClickListener(vv->{

                delete(dialog, holder.getAdapterPosition());

            });

        });



    }

    public void delete(BottomSheetDialog dialog, int position){

        Friend delFriend = mFriendList.get(position);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext().build();

        FriendServiceGrpc.FriendServiceBlockingStub blockingStub = FriendServiceGrpc.newBlockingStub(channel);
        ProfileOuterClass.Profile profile = ProfileOuterClass.Profile.newBuilder()
                .setToken(myProfile.getToken())
                .setPhoneNum(delFriend.getPhoneNumber())
                .build();

        Login.Result result = blockingStub.deleteFriend(profile);

        if(result.getSuccess()){

            dialog.hide();
        }
        mFriendList.remove(position);
        notifyItemRemoved(position);

        Toast.makeText(context,"Delete success",Toast.LENGTH_SHORT).show();


    }

    @Override
    public int getItemCount() {
        return mFriendList.size();
    }

}
