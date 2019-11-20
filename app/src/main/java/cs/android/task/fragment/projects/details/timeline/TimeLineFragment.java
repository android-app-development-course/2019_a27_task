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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cs.android.task.R;
import cs.android.task.entity.LogItem;

public class TimeLineFragment extends Fragment {

private RecyclerView timelineRecycleView;
private List<LogItem> logItems = new ArrayList<>();

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
public void onViewCreated (@NonNull View view,@Nullable Bundle savedInstanceState) {
    super.onViewCreated(view,savedInstanceState);
}

@Override
public View onCreateView(LayoutInflater inflater,ViewGroup container,
                         Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.timeline_card, container, false);
    timelineRecycleView = view.findViewById(R.id.timeline_recyclerview);
    TimelineItemAdapter adapter = new TimelineItemAdapter(logItems);
    timelineRecycleView.setLayoutManager(
            new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    timelineRecycleView.setAdapter(adapter);
    initTestLog(10);
    timelineRecycleView.getAdapter().notifyDataSetChanged();
    return view;
}

private void initTestLog(int size) {
    for (int i = 0; i < size; i++) {
        LogItem logItem = new LogItem();
        logItem.setDate(new Date());
        logItem.setContent("this is content" + String.valueOf(i));
        logItem.setSummary("this is summary" + String.valueOf(i));
        logItems.add(logItem);
    }
}


}
