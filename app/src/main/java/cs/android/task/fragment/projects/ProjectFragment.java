package cs.android.task.fragment.projects;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cs.android.task.R;
import cs.android.task.entity.Project;
import cs.android.task.fragment.projects.creationDialog.CreateDialog;
import cs.android.task.fragment.projects.details.DetailsFragment;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the
 * the {@link ProjectFragment#newInstance} factory method to create an instance of this fragment.
 */
public class ProjectFragment extends Fragment {
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

  // TODO: Rename and change types of parameters
  private List<Project> projectList;
  private RecyclerView recyclerView;
  private ProjectAdapter adapter;

  public ProjectFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of this fragment using the provided
   * parameters.
   *
   * @return A new instance of fragment ProjectFragment.
   */
  // TODO: Rename and change types and number of parameters
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
    View view = inflater.inflate(R.layout.fragment_project, container, false);
    projectList = new ArrayList<>();
    recyclerView = view.findViewById(R.id.projects_list);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(layoutManager);
    adapter = new ProjectAdapter(projectList);
    recyclerView.setAdapter(adapter);
    initProjectList();
    setupEvent(view);
    adapter.notifyItemRangeInserted(0, 2);
    adapter.notifyDataSetChanged();
    return view;
  }

  public void setupEvent(View view) {
    // setup ENTER btn - show details
    view.findViewById(R.id.enter_btn)
        .setOnClickListener(
            v -> {
              FragmentManager fm = getFragmentManager();
              FragmentTransaction transaction = fm.beginTransaction();
              transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
              transaction.add(R.id.fragment_layout, DetailsFragment.newInstance());
              transaction.addToBackStack(null);
              transaction.commit();
            });
    view.findViewById(R.id.add)
            .setOnClickListener(this::setAdd);
  }

  private void initProjectList() {
    for (int i = 0; i < 10; i++) {
      Project project = new Project();
      project.setCreateDate(new Date());
      project.setLeaderName("Leader Name");
      project.setName("Project " + i);
      projectList.add(project);
    }
  }

  public void setAdd(View view) {
    Bundle bundle = new Bundle();
    ArrayList<String> names = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      names.add("member " + i);
    }
    ArrayList<String> phoneNum = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      phoneNum.add(i + "");
    }
    bundle.putStringArrayList("names", names);
    bundle.putStringArrayList("phone_nums", phoneNum);

    CreateDialog dialog = CreateDialog.newInstance(bundle);
    FragmentManager fm = getFragmentManager();
    FragmentTransaction transaction = fm.beginTransaction();
    transaction.setCustomAnimations(R.anim.grow_in, R.anim.grow_out);
    transaction.add(R.id.fragment_layout, dialog);
    transaction.addToBackStack(null);
    transaction.commit();
  }
}
