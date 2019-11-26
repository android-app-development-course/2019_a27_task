package cs.android.task.fragment.friend;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;

import cs.android.task.R;
import cs.android.task.entity.Friend;
import cs.android.task.entity.Schedule;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the
 * {@link FriendFragment.OnFragmentInteractionListener} interface to handle interaction events. Use
 * the {@link FriendFragment#newInstance} factory method to create an instance of this fragment.
 */
public class FriendFragment extends Fragment {
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

  RecyclerView recyclerView;
  private List<Friend> FriendList = new ArrayList<>();

  public FriendFragment() {}

  // TODO: Rename and change types and number of parameters
  public static FriendFragment newInstance() {
    FriendFragment fragment = new FriendFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_friend, container, false);
    //        把自定义的RecycleView变量和activity_main.xml中的id绑定
    recyclerView = (RecyclerView) view.findViewById(R.id.friend_list);

    //        设置RecycleView的布局方式，这里是线性布局，默认垂直

    recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

    //        实例化自定义适配器
    initFriends();
    FriendAdapter friendAdapter = new FriendAdapter(FriendList, getContext());

    //        把适配器添加到RecycleView中
    recyclerView.setAdapter(friendAdapter);
    CollapsingToolbarLayout collapsingToolbarLayout =
        view.findViewById(R.id.collapsing_toolbar_layout);
    collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#ffffff"));
    collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#ffffff"));
    collapsingToolbarLayout.setContentScrimColor(Color.parseColor("#e16b6b"));

    return view;
  }

  public void initFriends() {
    Drawable drawable = getResources().getDrawable(R.mipmap.leader_bg); // 获取drawable
    BitmapDrawable bd = (BitmapDrawable) drawable;
    Friend friend1 = new Friend("David", "A Sexy Boy just coindance", bd.getBitmap());
    Friend friend2 = new Friend("Sam", "A Good Student not kil u", bd.getBitmap());
    Friend friend3 = new Friend("Cbj", "A Bad Boy yep yas la beat", bd.getBitmap());
    FriendList.add(friend1);
    FriendList.add(friend2);
    FriendList.add(friend3);
  }
}
