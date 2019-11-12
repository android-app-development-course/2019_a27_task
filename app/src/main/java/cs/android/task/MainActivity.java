package cs.android.task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
                View ls = findViewById(R.id.layout_select);
                switch (item.getItemId()) {
                    case R.id.project:
                    case R.id.more:
                    case R.id.friend:
                        ls.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.my:
                        ls.setVisibility(View.GONE);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

}
