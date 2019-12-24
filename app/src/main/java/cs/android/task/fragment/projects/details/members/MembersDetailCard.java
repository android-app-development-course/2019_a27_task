package cs.android.task.fragment.projects.details.members;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cs.android.task.MyApplication;
import cs.android.task.R;
import cs.android.task.entity.Friend;
import cs.android.task.entity.Member;
import cs.android.task.fragment.schedule.AddSchedule;
import cs.android.task.view.main.MainActivity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import task.FriendServiceGrpc;
import task.Login;
import task.ProfileOuterClass;
import task.ProjectOuterClass;
import task.ProjectServiceGrpc;

/** The type Members detail card. */
public class MembersDetailCard extends Fragment {

  /** The Members. */
  List<Member> memberList;

  private RecyclerView listView;
  private MemberDetailAdapter adapter;
    private Iterator<ProjectOuterClass.Member> myMember;
    private static String host;
    private static int port = 50050;
    private ProfileOuterClass.Profile myProfile;
    private ProjectOuterClass.Project myProject;

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

    MyApplication myApplication = new MyApplication();
    host = myApplication.getHost();

    myProfile = ((MainActivity)getActivity()).getMyProfile();
    myProject = ((MainActivity)getActivity()).getMyProject();

    listView = view.findViewById(R.id.member_list);
    listView.setHasFixedSize(true);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
    listView.setLayoutManager(layoutManager);
    /*
     TODO
     members list here should be set as a project's members list.
     */
    memberList = new ArrayList<>();
    initMemberList();
    adapter = new MemberDetailAdapter(memberList);
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
private void initMemberList() {
    memberList.clear();
    ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
            .usePlaintext().build();
    ProjectServiceGrpc.ProjectServiceBlockingStub stub = ProjectServiceGrpc.newBlockingStub(channel);

    ProjectOuterClass.ProjectQuery projectQuery = ProjectOuterClass.ProjectQuery.newBuilder()
            .setToken(myProfile.getToken())
            .setID(myProject.getID())
            .build();


    try {
        myMember = stub.getMembers(projectQuery);
    } catch (StatusRuntimeException e) {
        Log.e("bug???", "freshNote: " + "bug");
    } finally {

        while (myMember.hasNext()) {
            ProjectOuterClass.Member member = myMember.next();
            Member newMember = new Member();
            newMember.setName("缺少名字属性");
            newMember.setEmail("缺少邮箱属性");
            newMember.setPhoneNum(member.getPhoneNum());

            memberList.add(newMember);

        }
        channel.shutdown();
    }
}
public MemberDetailAdapter getAdapter(){
    return adapter;
}

public List<Member> getMembers(){
    return members;
}
}
