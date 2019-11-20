package cs.android.task.fragment.projects.details.members;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cs.android.task.R;
import cs.android.task.entity.Member;

/** The type Members detail card. */
public class MembersDetailCard extends Fragment {

  /** The Members. */
  List<Member> members;

  /** The List view. */
  RecyclerView listView;

  /** The Collapsing toolbar layout. */
  CollapsingToolbarLayout collapsingToolbarLayout;

  /** Instantiates a new Members detail card. */
  public MembersDetailCard() {}

  /**
   * New instance members_item detail card.
   *
   * @return the members_item detail card
   */
  public static MembersDetailCard newInstance() {
    Bundle args = new Bundle();
    MembersDetailCard leader = new MembersDetailCard();
    leader.setArguments(args);
    return leader;
}

@Override
public void onCreate (@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
}

@Nullable
@Override
public View onCreateView (@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.members_item, container, false);
    /*
    TODO
    setup view args here
     */

    listView = view.findViewById(R.id.member_list);
    listView.setHasFixedSize(true);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
    listView.setLayoutManager(layoutManager);
    members = new ArrayList<>();
    setList();
    listView.setAdapter(new MemberDetailAdapter(members));
    return view;
}

/*
    set up member list here
 */
private void setList() {
    ArrayList<Member> test_members = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
        Member m = new Member();
        m.setPhoneNum("123456789");
        m.setName("member " + i);
        m.setEmail("thisisemail@gmail.com");
        test_members.add(m);
    }
    members.addAll(test_members);
}
}
