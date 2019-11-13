package cs.android.task.view.profile;

import androidx.fragment.app.Fragment;
import cs.android.task.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProfileActivity extends Fragment {
    public static ProfileActivity newInstance() {
        return new ProfileActivity();
    }

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
}

@Override
public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
    return inflater.inflate(R.layout.activity_profile, container, false );
}
}
