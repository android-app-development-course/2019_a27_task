package cs.android.task.fragment.projects.details;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ProjectDetailsAdapter extends FragmentStateAdapter {

private List<Fragment> fragmentList = new ArrayList<>();

public ProjectDetailsAdapter (@NonNull FragmentManager fragmentManager,@NonNull Lifecycle lifecycle) {
    super(fragmentManager,lifecycle);
}

  /**
   * Add fragment.
   *
   * @param fragment the fragment
   */
  public void addFragment(Fragment fragment) {
    fragmentList.add(fragment);
}

@Override
public int getItemCount () {
    return fragmentList.size();
}

@NonNull
@Override
public Fragment createFragment (int position) {
    return fragmentList.get(position);
}

}
