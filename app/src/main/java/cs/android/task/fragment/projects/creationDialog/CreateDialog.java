package cs.android.task.fragment.projects.creationDialog;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.Chip;

import java.util.ArrayList;
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

public class CreateDialog extends Fragment {

private List<Chip> chipsList = new ArrayList<>();
private ChipsInput chipsInput;
private Bundle bundle;
private static String host ;
private static int port = 50050;
private ProfileOuterClass.Profile myProfile;
private EditText projectName;

    public static CreateDialog newInstance(@NonNull Bundle bundle) {
    CreateDialog dialog = new CreateDialog();
    dialog.setArguments(bundle);
    dialog.bundle = bundle;
    return dialog;
}

public CreateDialog () {}

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
}

@Override
public View onCreateView(
        LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.project_create_dialog, container, false);
    chipsInput = (ChipsInput) view.findViewById(R.id.select_members);

    myProfile = ((MainActivity)getActivity()).getMyProfile();
    MyApplication myApplication = new MyApplication();
    host = myApplication.getHost();
    projectName = view.findViewById(R.id.new_project_name);

    ArrayList<String> names = bundle.getStringArrayList("names");
    ArrayList<String> phoneNums = bundle.getStringArrayList("phone_nums");

    for (int i = 0; i < names.size(); i++) {
        chipsList.add(new Chip(R.drawable.smile, names.get(i), phoneNums.get(i)));
    }
    chipsInput.setFilterableList(chipsList);

    ((MaterialButton)view.findViewById(R.id.ok)).setOnClickListener(v->{
        CreateProject();
    });
    ((MaterialButton)view.findViewById(R.id.cancel)).setOnClickListener(v->{
        bundle.putBoolean("ok",false);
        this.getFragmentManager().popBackStack();
    });
    bundle.putString("name",(( EditText )view.findViewById(R.id.new_project_name)).getText().toString());
    return view;
}


public void CreateProject(){
        String projectNameStr = projectName.getText().toString();
        if(!" ".equals(projectNameStr) && projectNameStr.length() != 0){

            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                    .usePlaintext().build();

            ProjectServiceGrpc.ProjectServiceBlockingStub blockingStub = ProjectServiceGrpc.newBlockingStub(channel);
            ProjectOuterClass.Project projectInfo = ProjectOuterClass.Project.newBuilder()
                    .setName(projectNameStr)
                    .setLeaderPhoneNum(myProfile.getPhoneNum())
                    .setToken(myProfile.getToken())
                    .build();



            Login.Result result = blockingStub.createProject(projectInfo);
            channel.shutdown();

            if(result.getSuccess()){


                Toast.makeText(getContext(),"Create project success",Toast.LENGTH_LONG).show();
                bundle.putBoolean("ok", true);
                this.getFragmentManager().popBackStack();

            }
            else{
                Toast.makeText(getContext(),"Create project fail",Toast.LENGTH_LONG).show();
            }

        }
        else{
            Toast.makeText(getContext(),"Input entire messages",Toast.LENGTH_LONG).show();
        }

}

}

