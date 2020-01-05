package cs.android.task.fragment.projects.details.members;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.QoS;

import cs.android.task.R;
import cs.android.task.entity.Member;
import cs.android.task.fragment.projects.details.timeline.TimeLineFragment;
import cs.android.task.view.main.MainActivity;

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
        FragmentManager fragmentManager = getFragmentManager();
        MembersDetailCard membersDetailCard =  (MembersDetailCard) fragmentManager.getFragments().get(fragmentManager.getFragments().size() - 2);
        EditText phone = view.findViewById(R.id.invite_member_phone);
        String memberPhone = phone.getText().toString();
        Member member = new Member();
        /*
        member 应该为邀请的手机号的信息
        根据手机号从数据库里
         */
        member.setName("test");
        member.setEmail("test");
        member.setPhoneNum("test");
        membersDetailCard.getMembers().add(member);
        membersDetailCard.getAdapter().notifyItemInserted(membersDetailCard.getAdapter().getItemCount());
        ((MainActivity)getActivity()).sendMessage("Invite you");
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
