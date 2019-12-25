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

import cs.android.task.MyApplication;
import cs.android.task.R;
import cs.android.task.entity.Member;
import cs.android.task.fragment.projects.details.timeline.TimeLineFragment;
import cs.android.task.view.main.MainActivity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import task.Login;
import task.ProfileOuterClass;
import task.ProjectOuterClass;
import task.ProjectServiceGrpc;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class InviteMember extends Fragment {

    private View view;
    private static String host;
    private static int port = 50050;
    private ProfileOuterClass.Profile myProfile;
    private ProjectOuterClass.Project myProject;

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

        MyApplication myApplication = new MyApplication();
        host = myApplication.getHost();

        myProfile = ((MainActivity)getActivity()).getMyProfile();
        myProject = ((MainActivity)getActivity()).getMyProject();

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

        /*
        member 应该为邀请的手机号的信息
        根据手机号从数据库里
         */

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext().build();
        ProjectServiceGrpc.ProjectServiceBlockingStub blockingStub = ProjectServiceGrpc.newBlockingStub(channel);
        ProjectOuterClass.Member member = ProjectOuterClass.Member.newBuilder()
                .setMemberName("No name")
                .setPhoneNum(memberPhone)
                .setProjectID(myProject.getID())
                .setToken(myProject.getToken())
                .build();

        Login.Result result = blockingStub.inviteMember(member);
        if(result.getSuccess()){
            ((MainActivity)getActivity()).sendMessage("Invite you");
            if (null != view) {
                assert imm != null;
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            Toast.makeText(getContext(), "Invite Success", Toast.LENGTH_LONG).show();
            assert this.getFragmentManager() != null;

            this.getFragmentManager().popBackStack();
        }
        else{
            Toast.makeText(getContext(), "Invite Fail", Toast.LENGTH_LONG).show();
        }



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
