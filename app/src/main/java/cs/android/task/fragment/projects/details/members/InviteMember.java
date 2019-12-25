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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cs.android.task.MyApplication;
import cs.android.task.R;
import cs.android.task.entity.Member;
import cs.android.task.fragment.projects.details.timeline.TimeLineFragment;
import cs.android.task.view.main.MainActivity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import task.Login;
import task.ProfileOuterClass;
import task.ProfileServiceGrpc;
import task.ProjectOuterClass;
import task.ProjectServiceGrpc;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class InviteMember extends Fragment {

    private View view;
    private static String host;
    private static int port = 50050;
    private ProfileOuterClass.Profile myProfile;
    private ProjectOuterClass.Project myProject;
    private EditText phone;
    private String memberPhone;

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
            phone = view.findViewById(R.id.invite_member_phone);
            memberPhone = phone.getText().toString();
            if(memberPhone.length() != 0 && !" ".equals(memberPhone)) {
                Callable<Boolean> add = this::Invite;

                ExecutorService executorService = Executors.newSingleThreadExecutor();
                Future<Boolean> future = executorService.submit(add);
                try {
                    //设置超时时间
                    boolean result = future.get(1, TimeUnit.SECONDS);

                    if (result) {
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
                        ((MainActivity)getActivity()).sendMessage("123:Invite you");

                        //((MainActivity)getActivity()).getProjectFragment().get

                        if (null != view) {
                            assert imm != null;
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }

                        Toast.makeText(getContext(), "Invite Success", Toast.LENGTH_LONG).show();
                        assert this.getFragmentManager() != null;
                        this.getFragmentManager().popBackStack();
                        ((MainActivity)getActivity()).getMembersDetailCard().initMemberList();

                    } else {
                        Toast.makeText(getContext(), "Invite Fail", Toast.LENGTH_LONG).show();
                    }
                } catch (TimeoutException e) {
                    Toast.makeText(getContext(), "TimeoutException", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e("bug----------------->", e + " ");
                    Toast.makeText(getContext(), "Can't find user", Toast.LENGTH_LONG).show();
                } finally {
                    executorService.shutdown();
                }

            }


        });
        ((MaterialButton) view.findViewById(R.id.invite_cancel)).setOnClickListener(v -> {
            Cancel();
        });

        return view;
    }

    private Boolean Invite() {

        ManagedChannel channel1 = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext().build();
        ProfileServiceGrpc.ProfileServiceBlockingStub profileBlockingStub = ProfileServiceGrpc.newBlockingStub(channel1);
        ProfileOuterClass.ProfileQuery profileQuery = ProfileOuterClass.ProfileQuery.newBuilder()
                .setToken(myProfile.getToken())
                .setPhoneNum(memberPhone)
                .build();



         channel1 = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext().build();
        ProfileOuterClass.Profile memberProfile = profileBlockingStub.getProfile(profileQuery);


        Log.e("邀请的---》", "Invite: " + memberProfile );

        ProjectServiceGrpc.ProjectServiceBlockingStub blockingStub = ProjectServiceGrpc.newBlockingStub(channel1);
        ProjectOuterClass.Member member = ProjectOuterClass.Member.newBuilder()
                .setName(memberProfile.getName())
                .setPhoneNum(memberPhone)
                .setProjectID(myProject.getID())
                .setToken(myProfile.getToken())
                .setEmail(memberProfile.getEmail())
                .build();

        Log.e("邀请的sd---》", "Invite: " + member.getName() );
        Login.Result result = blockingStub.inviteMember(member);
        channel1.shutdown();
        return result.getSuccess();


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
