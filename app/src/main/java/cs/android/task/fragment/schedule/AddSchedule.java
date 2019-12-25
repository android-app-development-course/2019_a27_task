package cs.android.task.fragment.schedule;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.Date;

import cs.android.task.R;
import cs.android.task.entity.Schedule;
import cs.android.task.view.main.MainActivity;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * @author dav1d
 */
public class AddSchedule extends Fragment {
    private View view;
    private EditText name;
    private EditText location;
    private EditText detail;


    public AddSchedule() {
        // Required empty public constructor
    }

    public static AddSchedule newInstance() {
        AddSchedule fragment = new AddSchedule();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_schedule_add, container, false);

        name = view.findViewById(R.id.add_schedule_name);
        location = view.findViewById(R.id.add_location_name);
        detail = view.findViewById(R.id.add_detail);
        ((MaterialButton) view.findViewById(R.id.add_ok)).setOnClickListener(v -> {
            addSchedule();
        });
        ((MaterialButton) view.findViewById(R.id.add_cancel)).setOnClickListener(v -> {
            cancel();
        });

        return view;
    }


    public void addSchedule() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
        if (null != view) {
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        ScheduleFragment scheduleFragment = ((MainActivity) getActivity()).getScheduleFragment();
        Schedule schedule = new Schedule(name.getText().toString(), new Date(), location.getText().toString(), detail.getText().toString());
        scheduleFragment.getScheduleList().add(schedule);
        scheduleFragment.getAdapter().notifyItemInserted(scheduleFragment.getAdapter().getItemCount());
        Toast.makeText(getContext(), "Add Schedule Success", Toast.LENGTH_LONG).show();
        assert this.getFragmentManager() != null;
        /*
       添加内容到数据库
         */
        this.getFragmentManager().popBackStack();
    }

    public void cancel() {
        assert this.getFragmentManager() != null;
        /*
        从数据库中删除
         */
        this.getFragmentManager().popBackStack();
    }


}
