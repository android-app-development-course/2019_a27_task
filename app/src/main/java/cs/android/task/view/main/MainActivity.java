package cs.android.task.view.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import cs.android.task.R;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView nav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                View projectsView = findViewById(R.id.layout_project);
                View profileView = findViewById(R.id.layout_profile);
                switch (item.getItemId()) {
                    case R.id.project:
                        profileView.setVisibility(View.GONE);
                        projectsView.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.more:
                        projectsView.setVisibility(View.GONE);
                        profileView.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.friend:
                        profileView.setVisibility(View.GONE);
                        projectsView.setVisibility(View.GONE);
                        return true;
                    case R.id.my:
                        profileView.setVisibility(View.GONE);
                        projectsView.setVisibility(View.GONE);
                        return true;
                    case R.id.schedule:
                        profileView.setVisibility(View.GONE);
                        projectsView.setVisibility(View.GONE);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

}
