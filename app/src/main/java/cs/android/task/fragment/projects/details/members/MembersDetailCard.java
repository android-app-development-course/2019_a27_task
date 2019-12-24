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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cs.android.task.R;
import cs.android.task.entity.Member;
import cs.android.task.fragment.schedule.AddSchedule;

/** The type Members detail card. */
public class MembersDetailCard extends Fragment {

  /** The Members. */
  List<Member> members;

  /** The List view. */
  RecyclerView listView;
  MemberDetailAdapter adapter;

  /** The Collapsing toolbar layout. */
  CollapsingToolbarLayout collapsingToolbarLayout;

  /** Instantiates a new Members detail card. */
  public MembersDetailCard() {}

  /**
   * New instance members_card detail card.
   *
   * @return the members_card detail card
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
    View view = inflater.inflate(R.layout.members_card, container, false);
    /*
    TODO
    setup view args here
     */

    listView = view.findViewById(R.id.member_list);
    listView.setHasFixedSize(true);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
    listView.setLayoutManager(layoutManager);
    /*
     TODO
     members list here should be set as a project's members list.
     */
    members = new ArrayList<>();
    setList();
    adapter = new MemberDetailAdapter(members);
    listView.setAdapter(adapter);
    view.findViewById(R.id.inviteMember).setOnClickListener(v -> {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.add(R.id.fragment_layout, InviteMember.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    });
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
public MemberDetailAdapter getAdapter(){
    return adapter;
}

public List<Member> getMembers(){
    return members;
}
}
