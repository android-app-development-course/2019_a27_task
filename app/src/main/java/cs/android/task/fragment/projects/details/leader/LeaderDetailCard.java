package cs.android.task.fragment.projects.details.leader;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cs.android.task.MyApplication;
import cs.android.task.R;
import cs.android.task.entity.Project;
import cs.android.task.fragment.projects.details.DetailsFragment;
import cs.android.task.view.main.MainActivity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import task.ProfileOuterClass;
import task.ProjectOuterClass;
import task.ProjectServiceGrpc;

public class LeaderDetailCard extends Fragment {
    private View view;
    private TextView name;
    private TextView phone;
    private TextView email;
    private ProjectOuterClass.Project myProject;
    private static String host;
    private static int port = 50050;
    private ProfileOuterClass.Profile myProfile;


    public LeaderDetailCard() {

    }

    public static LeaderDetailCard newInstance() {
        Bundle args = new Bundle();
        LeaderDetailCard leader = new LeaderDetailCard();
        leader.setArguments(args);

        return leader;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.leader_card, container, false);

        MyApplication myApplication = new MyApplication();
        host = myApplication.getHost();

        myProfile = ((MainActivity) getActivity()).getMyProfile();
        myProject = ((MainActivity) getActivity()).getMyProject();

        name = view.findViewById(R.id.leader_detail_name);
        phone = view.findViewById(R.id.leader_phone_num);
        email = view.findViewById(R.id.leader_email);

        if (myProject != null) {
            name.setText(myProject.getLeaderName());
            phone.setText(myProject.getLeaderPhoneNum());
            email.setText(myProject.getLeaderEmail());
        }

        return view;
    }

}
