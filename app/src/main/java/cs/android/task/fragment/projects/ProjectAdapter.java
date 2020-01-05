package cs.android.task.fragment.projects;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cs.android.task.MyApplication;
import cs.android.task.R;
import cs.android.task.entity.Friend;
import cs.android.task.entity.Project;
import cs.android.task.fragment.projects.details.DetailsFragment;
import cs.android.task.view.main.MainActivity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import task.Login;
import task.ProfileOuterClass;
import task.ProjectOuterClass;
import task.ProjectServiceGrpc;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {
    private ProjectFragment projectFragment;
    private List<Project> projects;
    private SimpleDateFormat dateFormater = new SimpleDateFormat("MM月dd日", Locale.CHINA);

    private MaterialButton entBtn;
    private MaterialButton doneBtn;
    private MaterialButton delBtn;
    private String host ;
    private int port = 50050;
    private ProfileOuterClass.Profile myProfile;



    public ProjectAdapter(List<Project> projects, ProjectFragment projectFragment, ProfileOuterClass.Profile myProfile) {
        this.projects = projects;
        this.projectFragment = projectFragment;
        this.myProfile = myProfile;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView createDate;
        private TextView leaderName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.project_name);
            createDate = (TextView) itemView.findViewById(R.id.created_date);
            leaderName = (TextView) itemView.findViewById(R.id.leader_name);
            entBtn = (MaterialButton) itemView.findViewById(R.id.card_enter_btn);
            doneBtn = (MaterialButton) itemView.findViewById(R.id.card_done_btn);
            delBtn = (MaterialButton) itemView.findViewById(R.id.card_del_btn);
            MyApplication myApplication = new MyApplication();
            host = myApplication.getHost();
        }



        private void doneBtn(View view) {

            /*
            完成按钮
             */
            ProjectAdapter.this.projects.remove(this.getAdapterPosition());
            ProjectAdapter.this.notifyItemRemoved(this.getAdapterPosition());
        }

    }



    @NonNull
    @Override
    public ProjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectAdapter.ViewHolder holder, int position) {
        holder.name.setText(projects.get(position).getName());
        holder.createDate.setText(dateFormater.format(projects.get(position).getCreateDate()));
        holder.leaderName.setText(projects.get(position).getLeaderName());




        delBtn.setOnClickListener(v->{
            Project project = projects.get(position);
            Log.e("e----------------?>>>>", position +  " " );
            Log.e("id----------------?>>>>", project.getId() +  " " );


            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                    .usePlaintext().build();
            ProjectServiceGrpc.ProjectServiceBlockingStub blockingStub = ProjectServiceGrpc.newBlockingStub(channel);
            ProjectOuterClass.ProjectQuery projectQuery = ProjectOuterClass.ProjectQuery.newBuilder()
                    .setToken(myProfile.getToken())
                    .setID(project.getId())
                    .build();

            ProjectOuterClass.Project delProject = blockingStub.getProjectInfo(projectQuery);
            Login.Result result = blockingStub.deleteProject(delProject);
            channel.shutdown();
            if(result.getSuccess()){
                Toast.makeText(projectFragment.getContext(),"Del project success",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(projectFragment.getContext(),"Del project fail",Toast.LENGTH_LONG).show();
            }

            ProjectAdapter.this.projects.remove(holder.getAdapterPosition());
            ProjectAdapter.this.notifyItemRemoved(holder.getAdapterPosition());

        });

        entBtn.setOnClickListener(v->{
            Project project = projects.get(position);

            FragmentManager fm = projectFragment.getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

            DetailsFragment detailsFragment = new DetailsFragment();
            Bundle args = new Bundle();

            args.putString("leaderName", project.getLeaderName());
            args.putString("leaderPhone", project.getLeaderPhone());
            args.putString("leaderEmail", project.getLeaderEmail());
            detailsFragment.setArguments(args);

            transaction.add(R.id.fragment_layout, detailsFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

}
