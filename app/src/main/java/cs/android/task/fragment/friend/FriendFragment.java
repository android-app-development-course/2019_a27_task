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
import java.util.List;

import cs.android.task.R;
import cs.android.task.entity.Friend;




/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the
 * {@link FriendFragment.OnFragmentInteractionListener} interface to handle interaction events. Use
 * the {@link FriendFragment#newInstance} factory method to create an instance of this fragment.
 */
public class FriendFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private RecyclerView recyclerView;
    private List<Friend> FriendList = new ArrayList<>();
    private FriendAdapter friendAdapter;
    private View view;

    public void addFriendToList(Friend friend){
        FriendList.add(friend);
    }



    public List<Friend> getFriendList(){
        return  FriendList;
    }

    public FriendFragment(){

    }

    // TODO: Rename and change types and number of parameters
    public static FriendFragment newInstance() {
        FriendFragment fragment = new FriendFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void initFriends(){
        Log.e("刷新？----》", "initFriends: 1"  );
        BitmapDrawable bd1 = (BitmapDrawable) getResources().getDrawable(R.mipmap.yuner);
        BitmapDrawable bd2 = (BitmapDrawable) getResources().getDrawable(R.mipmap.girl);
        BitmapDrawable bd3 = (BitmapDrawable) getResources().getDrawable(R.mipmap.limei);
        BitmapDrawable bd4 = (BitmapDrawable) getResources().getDrawable(R.mipmap.iu);
        BitmapDrawable bd5 = (BitmapDrawable) getResources().getDrawable(R.mipmap.xiuzhi);
        Friend friend1 = new Friend("David", "A Sexy Boy just coindance", bd1.getBitmap());
        Friend friend2 = new Friend("Sam", "A Good Student not kil u", bd2.getBitmap());
        Friend friend3 = new Friend("Cbj", "A Bad Boy yep yas la beat", bd3.getBitmap());
        Friend friend4 = new Friend("Punk", "do what i love do", bd4.getBitmap());
        Friend friend5 = new Friend("Jack", "meet who i could meet", bd5.getBitmap());
        FriendList.add(friend1);
        FriendList.add(friend2);
        FriendList.add(friend3);
        FriendList.add(friend4);
        FriendList.add(friend5);
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

    recyclerView = (RecyclerView) view.findViewById(R.id.friend_list);

    //        设置RecycleView的布局方式，这里是线性布局，默认垂直

    recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));



    //        实例化自定义适配器
    initFriends();
    friendAdapter = new FriendAdapter(FriendList, getContext());


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
      Log.e("e---------.>", "onResume: " + FriendList.size() );
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
