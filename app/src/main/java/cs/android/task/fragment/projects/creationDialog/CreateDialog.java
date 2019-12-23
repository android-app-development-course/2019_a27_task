package cs.android.task.fragment.projects.creationDialog;

import com.google.android.material.button.MaterialButton;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.Chip;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import cs.android.task.MyApplication;
import cs.android.task.R;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import task.Login;
import task.ProjectOuterClass;
import task.ProjectServiceGrpc;
import cs.android.task.entity.Project;
import cs.android.task.fragment.projects.ProjectFragment;
import cs.android.task.view.main.MainActivity;

public class CreateDialog extends Fragment {

    private Bundle bundle;
    private static String host;
    private int port = 50050;

    public static CreateDialog newInstance(@NonNull Bundle bundle) {
        CreateDialog dialog = new CreateDialog();
        dialog.setArguments(bundle);
        dialog.bundle = bundle;
        return dialog;
    }

    public CreateDialog() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.project_create_dialog, container, false);
        EditText projectName = (EditText) view.findViewById(R.id.new_project_name);

        MyApplication myApplication = new MyApplication();
        host = myApplication.getHost();
        bundle.putString("projectName", projectName.getText().toString());


        ((MaterialButton) view.findViewById(R.id.ok)).setOnClickListener(v -> {
            bundle.putBoolean("ok", true);
            if (createProject()) {
                ProjectFragment projectFragment = ((MainActivity) getActivity()).getProjectFragment();

                Project newPorject = new Project();
                newPorject.setCreateDate(new Date());
                String name = "nowLeader";/*当前用户姓名*/
                newPorject.setLeaderName(name);
                newPorject.setName(projectName.getText().toString());
                projectFragment.getProjectList().add(newPorject);
                projectFragment.getAdapter().notifyItemInserted(projectFragment.getAdapter().getItemCount());
                Toast.makeText(getContext(), "Create project success", Toast.LENGTH_LONG).show();
                bundle.putBoolean("ok", true);
                this.getFragmentManager().popBackStack();
            } else {
                Toast.makeText(getContext(), "Create project fail", Toast.LENGTH_LONG).show();
                bundle.putBoolean("ok", true);
                this.getFragmentManager().popBackStack();
            }
        });
        ((MaterialButton) view.findViewById(R.id.cancel)).setOnClickListener(v -> {
            bundle.putBoolean("ok", false);
            this.getFragmentManager().popBackStack();
        });
        bundle.putString("name", ((EditText) view.findViewById(R.id.new_project_name)).getText().toString());
        return view;
    }


    public boolean createProject() {

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext().build();
        ProjectServiceGrpc.ProjectServiceBlockingStub blockingStub = ProjectServiceGrpc.newBlockingStub(channel);
        ProjectOuterClass.Project projectInfo = ProjectOuterClass.Project.newBuilder()
                .setName("lms")
                .setLeaderPhoneNum("d")
                .build();

        Login.Result result = blockingStub.createProject(projectInfo);
        channel.shutdown();
        return result.getSuccess();


    }

}

