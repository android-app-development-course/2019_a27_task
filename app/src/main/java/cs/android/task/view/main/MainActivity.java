package cs.android.task.view.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import cs.android.task.R;
import cs.android.task.view.profile.ProfileActivity;
import cs.android.task.view.projects.ProjectActivity;


import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    BottomNavigationView nav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
    nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.my:
                    loadFragment(ProfileActivity.newInstance());
                    return true;
                case R.id.project:
                    loadFragment(ProjectActivity.newInstance());
                    return true;
                case R.id.more:
                    loadFragment(TestFragment.newInstance());
                    return true;
                case R.id.friend:
                    loadFragment(TestFragment.newInstance());
                    return true;
                default:
                    return false;
            }
        }
    });
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.frame_container, TestFragment.newInstance());
    transaction.commit();

}

private void loadFragment(Fragment fragment) {
    // load fragment
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.frame_container, fragment);
    transaction.commit();
}
}
