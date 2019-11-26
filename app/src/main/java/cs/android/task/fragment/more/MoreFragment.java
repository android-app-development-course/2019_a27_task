package cs.android.task.fragment.more;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import cs.android.task.R;

public class MoreFragment extends Fragment {

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_more, container, false);
    CollapsingToolbarLayout collapsingToolbarLayout =
        view.findViewById(R.id.collapsing_toolbar_layout);
    collapsingToolbarLayout.setExpandedTitleTextColor(ColorStateList.valueOf(Color.parseColor("#ffffff")));
    collapsingToolbarLayout.setContentScrimColor(Color.parseColor("#283339"));
    collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#ffffff"));

    return view;
  }
}
