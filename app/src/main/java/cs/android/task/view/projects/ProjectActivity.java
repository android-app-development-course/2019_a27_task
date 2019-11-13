package cs.android.task.view.projects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cs.android.task.R;
import cs.android.task.entity.Project;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ProjectActivity extends AppCompatActivity {

    private List<Project> projectList;
@Override
protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_projects);

    this.projectList = initProjectList();
    RecyclerView recyclerView = findViewById(R.id.projects_list);
    ProjectAdapter adapter = new ProjectAdapter(projectList);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);
}

private List<Project> initProjectList() {
    Project project_1 = new Project();
    project_1.setCreateDate(new Date());
    project_1.setLeaderName("Leader Name");
    project_1.setName("Project one");

    Project project_2 = new Project();
    project_2.setCreateDate(new Date());
    project_2.setLeaderName("Leader Name");
    project_2.setName("Project two");

    return new ArrayList<>(Arrays.asList(project_1, project_2));
}
}
