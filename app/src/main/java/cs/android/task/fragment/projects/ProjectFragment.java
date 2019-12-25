package cs.android.task.fragment.projects;


import android.animation.Animator;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import cs.android.task.MyApplication;
import cs.android.task.R;
import cs.android.task.entity.Friend;
import cs.android.task.entity.Project;


import cs.android.task.fragment.projects.creationDialog.CreateDialog;

import cs.android.task.fragment.projects.details.DetailsFragment;
import cs.android.task.fragment.projects.details.members.MembersDetailCard;
import cs.android.task.fragment.projects.details.timeline.TimeLineFragment;
import cs.android.task.view.main.MainActivity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import task.FriendServiceGrpc;
import task.Login;
import task.ProfileOuterClass;
import task.ProjectOuterClass;
import task.ProjectServiceGrpc;

public class ProjectFragment extends Fragment {

    // TODO: Rename and change types of parameters
    private List<Project> projectList;
    private RecyclerView recyclerView;
    private ProjectAdapter adapter;
    private View view;
    private static String host ;
    private static int port = 50050;
    private ProfileOuterClass.Profile myProfile;
    private Iterator<ProjectOuterClass.Project> myProject;


    public ProjectFragment() {
        // Required empty public constructor
    }


    public static ProjectFragment newInstance() {
        ProjectFragment fragment = new ProjectFragment();
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
        view = inflater.inflate(R.layout.fragment_project, container, false);
        myProfile = ((MainActivity)getActivity()).getMyProfile();
        MyApplication myApplication = new MyApplication();
        host = myApplication.getHost();
        CollapsingToolbarLayout collapsingToolbarLayout =
                view.findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#ffffff"));
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#ffffff"));
        collapsingToolbarLayout.setContentScrimColor(Color.parseColor("#e16b6b"));
        projectList = new ArrayList<>();
        initProjectList();

        recyclerView = view.findViewById(R.id.projects_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProjectAdapter(projectList, this, myProfile);
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.add)
                .setOnClickListener(this::setAdd);
        adapter.notifyItemRangeInserted(0, 2);
        adapter.notifyDataSetChanged();



        return view;
    }



    public void initProjectList() {
        projectList.clear();
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext().build();
        ProjectServiceGrpc.ProjectServiceBlockingStub stub = ProjectServiceGrpc.newBlockingStub(channel);

        Login.Token token = Login.Token.newBuilder().setToken(myProfile.getToken()).build();


        try {
            myProject = stub.getUserProject(token);
        } catch (StatusRuntimeException e) {
            Log.e("bug???", "freshNote: " + "bug");
        } finally {

            while (myProject.hasNext()) {
                ProjectOuterClass.Project project = myProject.next();
                Log.e("project全部信息--->", "initProjectList: " + project );
                Project newProject = new Project();
                newProject.setName(project.getName());
                newProject.setLeaderPhone(project.getLeaderPhoneNum());
                newProject.setCreateDate(new Date(project.getCreateDate()));
                newProject.setId(project.getID());
                newProject.setLeaderName(project.getLeaderName());
                newProject.setLeaderEmail(project.getLeaderEmail());

                projectList.add(newProject);

            }
            channel.shutdown();
        }


    }


    public void setAdd(View view) {
        Bundle bundle = new Bundle();

        CreateDialog dialog = CreateDialog.newInstance(bundle);
        FragmentManager fm = getFragmentManager();
        assert fm != null;
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.add(R.id.fragment_layout, dialog);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public List<Project> getProjectList(){
        return projectList;
    }

    public ProjectAdapter getAdapter(){
        return adapter;
    }
}
