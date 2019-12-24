package cs.android.task.fragment.projects.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import cs.android.task.R;
import cs.android.task.fragment.projects.details.files.FilesDetails;
import cs.android.task.fragment.projects.details.leader.LeaderDetailCard;
import cs.android.task.fragment.projects.details.members.MembersDetailCard;
import cs.android.task.fragment.projects.details.timeline.TimeLineFragment;
import cs.android.task.fragment.projects.details.timeline.TimelineItemAdapter;

public class DetailsFragment extends Fragment {

public static DetailsFragment newInstance(/*arg*/) {
    /*
    DetailsFragment fragment = new DetailsFragment();
    Bundle args = new Bundle();
    args.put(arg);
    frament.setArgs(args);
     */
    return new DetailsFragment();
}


public View onCreateView(LayoutInflater inflater,ViewGroup container,
                         Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.details, container, false);
    DotsIndicator indicator = view.findViewById(R.id.dots_indicator);
    ViewPager2 viewPager = view.findViewById(R.id.view_pager);
    ProjectDetailsAdapter adapter = new ProjectDetailsAdapter(this.getFragmentManager(), this.getLifecycle());
    /*
    add fragments here;
     */
    adapter.addFragment(LeaderDetailCard.newInstance());
    adapter.addFragment(MembersDetailCard.newInstance());
//    adapter.addFragment(FilesDetails.newInstance());
    adapter.addFragment(TimeLineFragment.newInstance());

    final ZoomOutPagerTransFormer transFormer = new ZoomOutPagerTransFormer();
    viewPager.setPageTransformer(transFormer);
    viewPager.setAdapter(adapter);
    indicator.setViewPager2(viewPager);

    return view;
}

@Override
public void onCreateOptionsMenu (@NonNull Menu menu,@NonNull MenuInflater inflater) {
    inflater.inflate(R.menu.file_menu, menu);
}

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
}

}
