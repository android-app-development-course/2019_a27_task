package cs.android.task.fragment.projects.details.members;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
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

  /** Instantiates a new Members detail card. */
  public MembersDetailCard() {}

  /**
   * New instance members detail card.
   *
   * @return the members detail card
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
    View view = inflater.inflate(R.layout.members, container, false);
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
    Member member_1 = new Member();
    member_1.setEmail("email_address@gmail.com");
    member_1.setName("member one");
    member_1.setPhoneNum("1234567890");

    Member member_2 = new Member();
    member_2.setEmail("email_address2@gmail.com");
    member_2.setName("member two");
    member_2.setPhoneNum("1234567890");

    members.add(member_1);
    members.add(member_2);
}
}
