package cs.android.task.fragment.schedule;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import cs.android.task.R;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * @author dav1d
 */
public class AddSchedule extends Fragment {
    private View view;


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
