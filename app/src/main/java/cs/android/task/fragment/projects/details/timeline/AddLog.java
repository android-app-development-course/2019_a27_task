package cs.android.task.fragment.projects.details.timeline;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import java.util.Date;

import cs.android.task.R;
import cs.android.task.entity.LogItem;
import cs.android.task.entity.Member;

public class AddLog extends Fragment {

    private View view;

    public AddLog() {
        // Required empty public constructor
    }

    public static AddLog newInstance() {
        AddLog fragment = new AddLog();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_log, container, false);
        ((MaterialButton) view.findViewById(R.id.add_ok)).setOnClickListener(v -> {
            add();
        });
        ((MaterialButton) view.findViewById(R.id.add_cancel)).setOnClickListener(v -> {
            cancel();
        });
        return view;
    }

    private void add() {
        Member member = new Member();
        member.setName("David");
        member.setEmail("sss@qq.com");
        member.setPhoneNum("23567890");
        LogItem logItem = new LogItem();
        logItem.setDate(new Date());
        logItem.setContent("halo sb");
        logItem.setCommiter(member);
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        TimeLineFragment timeLineFragment =  (TimeLineFragment) fragmentManager.getFragments().get(fragmentManager.getFragments().size() - 2);
        timeLineFragment.getLogItems().add(logItem);
        timeLineFragment.getAdapter().notifyItemInserted(timeLineFragment.getAdapter().getItemCount());
        assert this.getFragmentManager() != null;
        this.getFragmentManager().popBackStack();
    }

    private void cancel() {
        assert this.getFragmentManager() != null;
        this.getFragmentManager().popBackStack();
    }


}
