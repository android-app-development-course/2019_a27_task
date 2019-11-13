package cs.android.task.view.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import cs.android.task.R;

public class TestFragment extends Fragment {
public static TestFragment newInstance() {
    TestFragment fragment = new TestFragment();
    return fragment;
}
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
}
@Override
public View onCreateView(LayoutInflater inflater,ViewGroup container,
                         Bundle savedInstanceState) {
    return inflater.inflate(R.layout.test_layout, container, false);
}

}
