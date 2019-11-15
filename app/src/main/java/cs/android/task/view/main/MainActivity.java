package cs.android.task.view.main;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import cs.android.task.R;
import cs.android.task.fragment.projects.ProjectFragment;
import cs.android.task.fragment.schedule.ScheduleFragment;
import cs.android.task.view.Util;

public class MainActivity extends AppCompatActivity
    implements ScheduleFragment.OnFragmentInteractionListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    loadFragment(new ProjectFragment());
    Util.immerseStatusBar(this);
    setupNavBar();
  }

  private void setupNavBar() {
    BottomNavigationView nav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
    nav.setOnNavigationItemSelectedListener(
        new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
              // use newInstance(), so that you can pass args.
              case R.id.project:
                loadFragment(ProjectFragment.newInstance());
                return true;
              case R.id.more:
                return true;
              case R.id.friend:
                return true;
              case R.id.my:
                return true;
              case R.id.schedule:
                loadFragment(ScheduleFragment.newInstance());
                return true;
              default:
                return false;
            }
          }
        });
  }

  private void loadFragment(Fragment fragment) {
    // load fragment
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.fragment_layout, fragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

  @Override
  public void onFragmentInteraction(Uri uri) {}
}
