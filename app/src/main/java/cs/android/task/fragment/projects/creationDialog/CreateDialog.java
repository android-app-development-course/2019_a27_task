package cs.android.task.fragment.projects.creationDialog;

import com.google.android.material.button.MaterialButton;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import cs.android.task.view.main.MainActivity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import task.Login;

import task.LoginServiceGrpc;
import task.ProfileOuterClass;

import task.ProjectOuterClass;
import task.ProjectServiceGrpc;
import cs.android.task.entity.Project;
import cs.android.task.fragment.projects.ProjectFragment;
import cs.android.task.view.main.MainActivity;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class CreateDialog extends Fragment {


    private List<Chip> chipsList = new ArrayList<>();
    private ChipsInput chipsInput;
    private Bundle bundle;
    private static String host;
    private static int port = 50050;
    private ProfileOuterClass.Profile myProfile;
    private EditText projectName;
    private String projectNameStr;
    private Login.Result result;

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


        MyApplication myApplication = new MyApplication();
        host = myApplication.getHost();

        myProfile = ((MainActivity)getActivity()).getMyProfile();
        Log.e("e---------->", "onCreateView: " + myProfile );
        projectName = view.findViewById(R.id.new_project_name);

        ((MaterialButton) view.findViewById(R.id.ok)).setOnClickListener(v -> {
            if (CreateProject()) {
                ProjectFragment projectFragment = ((MainActivity) getActivity()).getProjectFragment();
                Project newPorject = new Project();
                newPorject.setCreateDate(new Date());
                newPorject.setLeaderName(myProfile.getName());
                newPorject.setName(projectNameStr);
                newPorject.setId(Long.valueOf(result.getErrorMsg()));
                newPorject.setLeaderPhone(myProfile.getPhoneNum());
                newPorject.setLeaderName(myProfile.getPhoneNum());
                newPorject.setLeaderEmail(myProfile.getEmail());
                projectFragment.getProjectList().add(newPorject);
                projectFragment.getAdapter().notifyItemInserted(projectFragment.getAdapter().getItemCount());
                Toast.makeText(getContext(), "Create project success", Toast.LENGTH_LONG).show();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);

                if (null != view) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                this.getFragmentManager().popBackStack();
            } else {
                Toast.makeText(getContext(), "Create project fail", Toast.LENGTH_LONG).show();
                this.getFragmentManager().popBackStack();
            }
        });

        ((MaterialButton) view.findViewById(R.id.cancel)).setOnClickListener(v -> {
            bundle.putBoolean("ok", false);
            this.getFragmentManager().popBackStack();
        });

        return view;
    }


    public boolean CreateProject() {
        projectNameStr = projectName.getText().toString();
        if (!" ".equals(projectNameStr) && projectNameStr.length() != 0) {

            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                    .usePlaintext().build();

            ProjectServiceGrpc.ProjectServiceBlockingStub blockingStub = ProjectServiceGrpc.newBlockingStub(channel);
            ProjectOuterClass.Project projectInfo = ProjectOuterClass.Project.newBuilder()
                    .setName(projectNameStr)
                    .setLeaderPhoneNum(myProfile.getPhoneNum())
                    .setToken(myProfile.getToken())
                    .setCreateDate(System.currentTimeMillis())
                    .build();

            result = blockingStub.createProject(projectInfo);

            Log.e("result:::----->", "CreateProject: " + result.getErrorMsg() );
            channel.shutdown();
            return result.getSuccess();

        }
        return false;
    }

}
