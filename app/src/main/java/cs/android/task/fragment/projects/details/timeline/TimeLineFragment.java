package cs.android.task.fragment.projects.details.timeline;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import cs.android.task.MyApplication;
import cs.android.task.R;
import cs.android.task.entity.LogItem;
import cs.android.task.entity.Member;
import cs.android.task.view.main.MainActivity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import task.LogOuterClass;
import task.LogServiceGrpc;
import task.ProfileOuterClass;
import task.ProjectOuterClass;
import task.ProjectServiceGrpc;

public class TimeLineFragment extends Fragment {

    private RecyclerView timelineRecycleView;
    private List<LogItem> logList = new ArrayList<>();
    private FloatingActionButton addTask;
    private TimelineItemAdapter adapter;
    private Iterator<LogOuterClass.Log> myLog;
    private static String host;
    private static int port = 50050;
    private ProfileOuterClass.Profile myProfile;
    private ProjectOuterClass.Project myProject;

    public TimeLineFragment() {
    }


    public static TimeLineFragment newInstance() {
        return new TimeLineFragment();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timeline_card, container, false);
        MyApplication myApplication = new MyApplication();
        host = myApplication.getHost();

        myProfile = ((MainActivity) getActivity()).getMyProfile();
        myProject = ((MainActivity) getActivity()).getMyProject();

        timelineRecycleView = view.findViewById(R.id.timeline_recyclerview);
        adapter = new TimelineItemAdapter(logList);
        timelineRecycleView.setLayoutManager(
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        timelineRecycleView.setAdapter(adapter);
        //initTestLog();
        timelineRecycleView.getAdapter().notifyDataSetChanged();
        addTask = view.findViewById(R.id.addTask);
        addTask.setOnClickListener(this::addLog);
        return view;
    }

    private void addLog(View view) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.add(R.id.fragment_layout, AddLog.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();


    }

    private void initTestLog() {

        logList.clear();
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext().build();
        LogServiceGrpc.LogServiceBlockingStub stub = LogServiceGrpc.newBlockingStub(channel);

        ProjectOuterClass.ProjectQuery projectQuery = ProjectOuterClass.ProjectQuery.newBuilder()
                .setToken(myProfile.getToken())
                .setID(myProject.getID())
                .build();


        try {
            myLog = stub.pullLogs(projectQuery);
        } catch (StatusRuntimeException e) {
            Log.e("bug???", "freshNote: " + "bug");
        } finally {

            while (myLog.hasNext()) {
                LogOuterClass.Log log = myLog.next();
                LogItem newLog = new LogItem();
                newLog.setContent(log.getContent());
                newLog.setDate(new Date(log.getDate()));
                newLog.setCommiter(log.getName());

                logList.add(newLog);

            }
            channel.shutdown();
        }
    }

    public TimelineItemAdapter getAdapter() {
        return adapter;
    }

    public List<LogItem> getLogList() {
        return logList;
    }

}
