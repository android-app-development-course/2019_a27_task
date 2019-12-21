package cs.android.task.view.main;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import android.animation.TimeInterpolator;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.Window;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import cs.android.task.R;





import cs.android.task.fragment.friend.FriendFragment;
import cs.android.task.fragment.profile.ProfileFragment;
import cs.android.task.fragment.projects.ProjectFragment;
import cs.android.task.fragment.schedule.ScheduleFragment;
import cs.android.task.view.Util;

public class MainActivity extends AppCompatActivity {
  private String myToken;


  public String getMyToken(){
    return myToken;
  }

  public void setMyToken(String myToken){
    this.myToken = myToken;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


    // 开启动画
    getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
    getWindow().setEnterTransition((new Fade()).setDuration(300));

    setContentView(R.layout.activity_main);


    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();
    String token = bundle.getString("token");
    setMyToken(token);

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

              case R.id.friend:
                loadFragment(FriendFragment.newInstance());
                return true;
              case R.id.my:
                loadFragment(new ProfileFragment());
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
}
