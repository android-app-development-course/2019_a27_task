package cs.android.task.fragment.projects.details;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import cs.android.task.MyApplication;
import cs.android.task.R;
import cs.android.task.entity.Project;
import cs.android.task.fragment.projects.details.files.FilesDetails;
import cs.android.task.fragment.projects.details.leader.LeaderDetailCard;
import cs.android.task.fragment.projects.details.members.MembersDetailCard;
import cs.android.task.fragment.projects.details.timeline.TimeLineFragment;
import cs.android.task.fragment.projects.details.timeline.TimelineItemAdapter;
import cs.android.task.view.main.MainActivity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import task.Login;
import task.ProfileOuterClass;
import task.ProjectOuterClass;
import task.ProjectServiceGrpc;

public class DetailsFragment extends Fragment {

    private ProjectOuterClass.Project myProject ;
    private static String host ;
    private static int port = 50050;
    private ProfileOuterClass.Profile myProfile;



    public static DetailsFragment newInstance(/*arg*/) {
    /*
    DetailsFragment fragment = new DetailsFragment();
    Bundle args = new Bundle();
    args.put(arg);
    frament.setArgs(args);
     */
    return new DetailsFragment();
}


@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.details, container, false);

    myProfile = ((MainActivity)getActivity()).getMyProfile();
    MyApplication myApplication = new MyApplication();
    host = myApplication.getHost();

    if(getArguments() != null){
        String leaderName = getArguments().getString("leaderName");
        String leaderEmail = getArguments().getString("leaderEmail");
        String leaderPhone = getArguments().getString("leaderPhone");
        String projectId = getArguments().getString("projectId");
        String projectName = getArguments().getString("projectName");

        myProject = ProjectOuterClass.Project.newBuilder()
                .setLeaderPhoneNum(leaderPhone)
                .setID(Long.valueOf(projectId))
                .setToken(myProfile.getToken())
                .setLeaderName(leaderName)
                .setLeaderEmail(leaderEmail)
                .setName(projectName)
                .build();


        Log.e("id----------first---->", "onCreateView: " + projectId );

        Log.e("我的-----》", "onCreateView: " + myProject);
       ((MainActivity)getActivity()).setMyProject(myProject);

    }
    DotsIndicator indicator = view.findViewById(R.id.dots_indicator);
    ViewPager2 viewPager = view.findViewById(R.id.view_pager);
    ProjectDetailsAdapter adapter = new ProjectDetailsAdapter(this.getFragmentManager(), this.getLifecycle());


    adapter.addFragment(LeaderDetailCard.newInstance());
    adapter.addFragment(MembersDetailCard.newInstance());
    adapter.addFragment(TimeLineFragment.newInstance());

    final ZoomOutPagerTransFormer transFormer = new ZoomOutPagerTransFormer();
    viewPager.setPageTransformer(transFormer);
    viewPager.setAdapter(adapter);
    indicator.setViewPager2(viewPager);

    return view;
}

@Override
public void onCreateOptionsMenu (@NonNull Menu menu,@NonNull MenuInflater inflater) {
    inflater.inflate(R.menu.file_menu, menu);
}

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
}

}
