package cs.android.task.fragment.projects.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cs.android.task.R;

public class LeaderDetailCard extends Fragment {

public LeaderDetailCard() {

}

public static LeaderDetailCard newInstance() {
    Bundle args = new Bundle();
    LeaderDetailCard leader = new LeaderDetailCard();
    leader.setArguments(args);
    return leader;
}

@Override
public void onCreate (@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
}

@Nullable
@Override
public View onCreateView (@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.leader_card, container, false);
    /*
    TODO
    setup view args here
     */
    return view;
}

}
