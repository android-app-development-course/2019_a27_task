package cs.android.task.fragment.schedule;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cs.android.task.R;
import cs.android.task.entity.Friend;
import cs.android.task.entity.Schedule;

/**
 * fragment
 *
 * @author dav1d
 */
public class ScheduleFragment extends Fragment {
    private List<Schedule> scheduleList = new ArrayList<>();
    private ScheduleAdapter adapter;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    public List<Schedule> getScheduleList(){
        return scheduleList;
    }

    public ScheduleAdapter getAdapter(){
        return adapter;
    }

    public static ScheduleFragment newInstance() {
        ScheduleFragment fragment = new ScheduleFragment();
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
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        initSchedules();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.schedule_list);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ScheduleAdapter(scheduleList);
        recyclerView.setAdapter(adapter);
        CollapsingToolbarLayout collapsingToolbarLayout =
                view.findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#ffffff"));
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#ffffff"));
        collapsingToolbarLayout.setContentScrimColor(Color.parseColor("#abc999"));
        collapsingToolbarLayout.setScrimAnimationDuration(300);
        view.findViewById(R.id.add_schedule).setOnClickListener(v -> {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            transaction.add(R.id.fragment_layout, AddSchedule.newInstance());
            transaction.addToBackStack(null);
            transaction.commit();
        });
        return view;
    }

    private void initSchedules() {
        Schedule schedule_1 =
                new Schedule(
                        "Android class",
                        new Date(2019, 11, 11),
                        "Building of CS",
                        "This is a very\n important class");
        Schedule schedule_2 =
                new Schedule(
                        "Software class",
                        new Date(2019, 11, 11),
                        "Building of CS",
                        "This is a very very important class of this term");
        scheduleList.add(schedule_1);
        scheduleList.add(schedule_2);
    }

    @Override
    public void  onResume() {
        Log.e("e---------.>", "onResume: " );
        adapter.notifyDataSetChanged();
        super.onResume();
    }



}
