package cs.android.task.fragment.projects.details;

import android.util.Log;

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
      boolean result = false;
      Log.e("new--->", "addFragment: "+ fragment );
      Log.e("list--->", "addFragment: "+ fragmentList);
      Log.e("list长度--->", "addFragment: "+ fragmentList.size());
      for(int i = 0;i < fragmentList.size();i++){
          Fragment oldFragment = fragmentList.get(i);
          Log.e("old--->", "addFragment: " + oldFragment );
          if(fragment.equals(oldFragment)){
              Log.e("e---->", "addFragment: 相等的" );
              result = true;
          }
      }
      if(!result){
          fragmentList.add(fragment);
      }
/*
 2019-12-25 15:23:09.006 22695-22695/cs.android.task E/new--->: addFragment: LeaderDetailCard{967148d (0ee31fe1-3819-4dfe-9f5e-826e4e65ee4a)}
      2019-12-25 15:23:09.006 22695-22695/cs.android.task E/new--->: addFragment: MembersDetailCard{b34e642 (1c255e6b-8ae5-4bae-8492-4a70ca43cfc1)}
      2019-12-25 15:23:09.006 22695-22695/cs.android.task E/old--->: addFragment: LeaderDetailCard{967148d (0ee31fe1-3819-4dfe-9f5e-826e4e65ee4a)}
      2019-12-25 15:23:09.006 22695-22695/cs.android.task E/new--->: addFragment: TimeLineFragment{cba5653 (0be24699-3b20-441a-8d64-7bc58efb3d9f)}
      2019-12-25 15:23:09.006 22695-22695/cs.android.task E/old--->: addFragment: LeaderDetailCard{967148d (0ee31fe1-3819-4dfe-9f5e-826e4e65ee4a)}
      2019-12-25 15:23:09.006 22695-22695/cs.android.task E/old--->: addFragment: MembersDetailCard{b34e642 (1c255e6b-8ae5-4bae-8492-4a70ca43cfc1)}
 */

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
