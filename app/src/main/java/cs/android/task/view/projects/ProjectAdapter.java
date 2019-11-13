package cs.android.task.view.projects;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cs.android.task.R;
import cs.android.task.entity.Project;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView createDate;
        private TextView leaderName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.project_name);
            createDate = (TextView)itemView.findViewById(R.id.created_date);
            leaderName = (TextView)itemView.findViewById(R.id.leader_name);
        }
    }

    private List<Project> projects;
    private static SimpleDateFormat dateFormater = new SimpleDateFormat("MM月dd日",Locale.CHINA);

    public ProjectAdapter(@NonNull List<Project> projects) {
        this.projects = projects;
    }

    @NonNull
    @Override
    public ProjectAdapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent,int viewType) {
        View view = LayoutInflater.from( parent.getContext())
                .inflate(R.layout.project_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder (@NonNull ProjectAdapter.ViewHolder holder,int position) {
        holder.name.setText(projects.get(position).getName());
        holder.createDate.setText(dateFormater.format(projects.get(position).getCreateDate()));
        holder.leaderName.setText(projects.get(position).getLeaderName());
    }

    @Override
    public int getItemCount () {
        return projects.size();
    }

}
