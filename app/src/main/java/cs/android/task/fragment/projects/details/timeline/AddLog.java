package cs.android.task.fragment.projects.details.timeline;

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

import java.util.Date;

import cs.android.task.MyApplication;
import cs.android.task.R;
import cs.android.task.entity.LogItem;
import cs.android.task.entity.Member;
import cs.android.task.view.main.MainActivity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import task.LogOuterClass;
import task.LogServiceGrpc;
import task.Login;
import task.ProfileOuterClass;
import task.ProjectOuterClass;
import task.ProjectServiceGrpc;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class AddLog extends Fragment {

    private View view;

    private static String host;
    private static int port = 50050;
    private ProfileOuterClass.Profile myProfile;
    private ProjectOuterClass.Project myProject;
    private EditText logContent;
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


        MyApplication myApplication = new MyApplication();
        host = myApplication.getHost();

        myProfile = ((MainActivity) getActivity()).getMyProfile();
        myProject = ((MainActivity) getActivity()).getMyProject();
        logContent = view.findViewById(R.id.add_content);
        ((MaterialButton) view.findViewById(R.id.add_ok)).setOnClickListener(v -> {
            add();
        });
        ((MaterialButton) view.findViewById(R.id.add_cancel)).setOnClickListener(v -> {
            cancel();
        });
        return view;
    }

    private void add() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext().build();

        LogServiceGrpc.LogServiceBlockingStub blockingStub = LogServiceGrpc.newBlockingStub(channel);
        LogOuterClass.Log logInfo = LogOuterClass.Log.newBuilder()
                .setToken(myProfile.getToken())
                .setProjectID(myProject.getID())
                .setContent(logContent.getText().toString())
                .setDate(System.currentTimeMillis())
                .setName(myProfile.getName())
                .setDone(false)
                .build();


        Login.Result result = blockingStub.addLog(logInfo);
        if (result.getSuccess()){

            Toast.makeText(getContext(), "Create log success",Toast.LENGTH_LONG).show();
        }
        Log.e("result:::----->", "CreateProject: " + result.getErrorMsg() );
        channel.shutdown();

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);

        if (null != view) {
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        assert this.getFragmentManager() != null;

        this.getFragmentManager().popBackStack();

    }

    private void cancel() {
        assert this.getFragmentManager() != null;
        this.getFragmentManager().popBackStack();
    }


}
