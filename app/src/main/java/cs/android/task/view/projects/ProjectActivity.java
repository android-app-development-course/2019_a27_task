package cs.android.task.view.projects;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cs.android.task.R;
import cs.android.task.entity.Project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectActivity extends Fragment {

private List<Project> projectList;
private RecyclerView recyclerView;
private ProjectAdapter adapter;


public static ProjectActivity newInstance(/* args... */) {
    ProjectActivity fragment = new ProjectActivity();
    Bundle args = new Bundle();
    // put args.. in args bundle
    fragment.setArguments(args);
    return fragment;
}

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
}

@Override
public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_projects, container, false);
    projectList = new ArrayList<>();
    recyclerView = view.findViewById(R.id.projects_list);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(layoutManager);
    adapter = new ProjectAdapter(projectList);
    recyclerView.setAdapter(adapter);
    initProjectList();
    adapter.notifyItemRangeInserted(0, 2);
    adapter.notifyDataSetChanged();
    return view;
}

private void initProjectList() {
    Project project_1 = new Project();
    project_1.setCreateDate(new Date());
    project_1.setLeaderName("Leader Name");
    project_1.setName("Project one");

    Project project_2 = new Project();
    project_2.setCreateDate(new Date());
    project_2.setLeaderName("Leader Name");
    project_2.setName("Project two");

    projectList.add(project_1);
    projectList.add(project_2);
}
}
