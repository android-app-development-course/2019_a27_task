package cs.android.task.fragment.projects.details.members;

import android.content.Context;
import android.net.Uri;
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

public class InviteMember extends Fragment {

    private View view;
    public InviteMember() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static InviteMember newInstance() {
        InviteMember fragment = new InviteMember();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_invite_member, container, false);

        ((MaterialButton) view.findViewById(R.id.invite_ok)).setOnClickListener(v -> {
            Invite();
        });
        ((MaterialButton) view.findViewById(R.id.invite_cancel)).setOnClickListener(v -> {
            Cancel();
        });

        return view;
    }

    private void Invite() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);

        if (null != view) {
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        Toast.makeText(getContext(), "Invite Success", Toast.LENGTH_LONG).show();
        assert this.getFragmentManager() != null;
        /*
       添加内容到数据库
         */
        this.getFragmentManager().popBackStack();
    }

    private void Cancel() {
        assert this.getFragmentManager() != null;
        this.getFragmentManager().popBackStack();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
