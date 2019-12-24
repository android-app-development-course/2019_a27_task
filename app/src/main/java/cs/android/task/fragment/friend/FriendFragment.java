package cs.android.task.fragment.friend;


import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import cs.android.task.MyApplication;
import cs.android.task.R;
import cs.android.task.entity.Friend;
import cs.android.task.entity.Note;
import cs.android.task.view.main.MainActivity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import task.FriendServiceGrpc;
import task.Login;
import task.Message;
import task.MessageServiceGrpc;
import task.ProfileOuterClass;


/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the
 * {@link FriendFragment.OnFragmentInteractionListener} interface to handle interaction events. Use
 * the {@link FriendFragment#newInstance} factory method to create an instance of this fragment.
 */
public class FriendFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private RecyclerView recyclerView;
    private List<Friend> friendList = new ArrayList<>();
    private FriendAdapter friendAdapter;
    private View view;
    private Iterator<ProfileOuterClass.Profile> myFriend;
    private static String host;
    private static int port = 50050;
    private ProfileOuterClass.Profile myProfile;
    private BitmapDrawable bd;




    public FriendFragment(){

    }

    // TODO: Rename and change types and number of parameters
    public static FriendFragment newInstance() {
        FriendFragment fragment = new FriendFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void initFriends() {
        friendList.clear();
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext().build();
        FriendServiceGrpc.FriendServiceBlockingStub stub = FriendServiceGrpc.newBlockingStub(channel);

        Login.Token token = Login.Token.newBuilder().setToken(myProfile.getToken()).build();


        try {
            myFriend = stub.getFriends(token);
        } catch (StatusRuntimeException e) {
            Log.e("bug???", "freshNote: " + "bug");
        } finally {

            while (myFriend.hasNext()) {
                ProfileOuterClass.Profile friend = myFriend.next();
                Friend newFriend = new Friend();
                newFriend.setName(friend.getName());
                newFriend.setImage(bd.getBitmap());
                newFriend.setPhoneNumber(friend.getPhoneNum());
                friendList.add(newFriend);

            }
            channel.shutdown();
        }

    }




  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
      view = inflater.inflate(R.layout.fragment_friend, container, false);
    //        把自定义的RecycleView变量和activity_main.xml中的id绑定
      bd = (BitmapDrawable)getResources().getDrawable(R.mipmap.santaclaus);
      MyApplication myApplication = new MyApplication();
      host = myApplication.getHost();

      myProfile = ((MainActivity)getActivity()).getMyProfile();



      recyclerView = (RecyclerView) view.findViewById(R.id.friend_list);
      recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));


    //        实例化自定义适配器
    initFriends();
    friendAdapter = new FriendAdapter(friendList, getContext(), myProfile);


    //        把适配器添加到RecycleView中
    recyclerView.setAdapter(friendAdapter);
    CollapsingToolbarLayout collapsingToolbarLayout =
        view.findViewById(R.id.collapsing_toolbar_layout);
    collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#ffffff"));
    collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#ffffff"));
    collapsingToolbarLayout.setContentScrimColor(Color.parseColor("#e16b6b"));


   view.findViewById(R.id.add_Friend_Button).setOnClickListener(v->{
        FragmentManager fm = getFragmentManager();

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        AddFriendFragment addFriendFragment = new AddFriendFragment();

        transaction.add(R.id.fragment_layout, addFriendFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    });


    return view;
  }

  @Override
  public void  onResume() {
      Log.e("e---------.>", "onResume: " );
      Log.e("e---------.>", "onResume: " + friendList.size() );
      friendAdapter.notifyDataSetChanged();
      super.onResume();
  }


    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (view != null && !hidden) {
            Log.e("e----->", "onHiddenChanged: " );
        }
    }





}
