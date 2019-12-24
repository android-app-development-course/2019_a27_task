package cs.android.task.fragment.projects.details.timeline;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import cs.android.task.R;
import cs.android.task.entity.LogItem;
import cs.android.task.entity.Member;
import cs.android.task.fragment.projects.details.members.InviteMember;

public class TimeLineFragment extends Fragment {

    private RecyclerView timelineRecycleView;
    private List<LogItem> logItems = new ArrayList<>();
    private FloatingActionButton addTask;
    private TimelineItemAdapter adapter;

    public TimeLineFragment() {
    }

    public void addLog(LogItem logItem) {
        logItems.add(logItem);
        timelineRecycleView.getAdapter().notifyDataSetChanged();
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
        timelineRecycleView = view.findViewById(R.id.timeline_recyclerview);
        adapter = new TimelineItemAdapter(logItems);
        timelineRecycleView.setLayoutManager(
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        timelineRecycleView.setAdapter(adapter);
        initTestLog(10);
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

    private void initTestLog(int size) {
        Member member = new Member();
        member.setName("LMS");
        member.setEmail("iamlms@qq.com");
        member.setPhoneNum("1234567890");
        for (int i = 0; i < size; i++) {
            LogItem logItem = new LogItem();
            logItem.setDate(new Date());
            logItem.setContent("this is content" + String.valueOf(i));
            logItem.setCommiter(member);
            logItems.add(logItem);
        }
    }

    public TimelineItemAdapter getAdapter(){
        return adapter;
    }

    public List<LogItem> getLogItems(){
        return logItems;
    }

}
