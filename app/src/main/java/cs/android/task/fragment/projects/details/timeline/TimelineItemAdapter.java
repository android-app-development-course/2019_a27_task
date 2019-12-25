package cs.android.task.fragment.projects.details.timeline;

import androidx.annotation.NonNull;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;


import com.github.vipulasri.timelineview.TimelineView;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

import cs.android.task.MyApplication;
import cs.android.task.R;
import cs.android.task.entity.LogItem;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import task.LogOuterClass;
import task.LogServiceGrpc;
import task.Login;
import task.ProfileOuterClass;
import task.ProjectOuterClass;


public class TimelineItemAdapter
        extends RecyclerView.Adapter<TimelineItemAdapter.TimelineViewHolder> {

    private List<LogItem> logList;
    private Context context;
    public CheckBox isFinish;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日", Locale.CHINA);
    private static String host;
    private static int port = 50050;
    private ProfileOuterClass.Profile myProfile;
    private ProjectOuterClass.Project myProject;

    class TimelineViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView content;
        public TextView committer;
        public TimelineView timeline;
        public CheckBox finished;

        public CheckBox getFinished() {
            return finished;
        }

        public TimelineViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            MyApplication myApplication = new MyApplication();
            host = myApplication.getHost();

            date = itemView.findViewById(R.id.log_date);
            content = itemView.findViewById(R.id.log_content);
            committer = itemView.findViewById(R.id.log_committer);
            timeline = itemView.findViewById(R.id.timeline);
            isFinish = itemView.findViewById(R.id.isFinish);
            finished = itemView.findViewById(R.id.isFinish);
            timeline.initLine(viewType);

        }
    }

    public TimelineItemAdapter(List<LogItem> logList, Context context, ProfileOuterClass.Profile myProfile, ProjectOuterClass.Project myProject) {
        this.logList = logList;
        this.myProfile = myProfile;
        this.context = context;
        this.myProject = myProject;
    }

    @NonNull
    @Override
    public TimelineItemAdapter.TimelineViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.timeline_item, parent, false);
        return new TimelineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineViewHolder holder, int position) {
        LogItem item = logList.get(position);
        holder.date.setText(dateFormat.format(item.getDate()));
        holder.content.setText(item.getContent());
        holder.committer.setText(item.getCommiter());
        holder.finished.setChecked(item.isDone());

        isFinish.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                    .usePlaintext().build();
            LogServiceGrpc.LogServiceBlockingStub blockingStub = LogServiceGrpc.newBlockingStub(channel);
            LogItem logItem = logList.get(position);
            Log.e("done?----", "onBindViewHolder: " + logItem.isDone() );
            LogOuterClass.LogStatus logStatus = LogOuterClass.LogStatus.newBuilder()
                    .setToken(myProfile.getToken())
                    .setProjectID(myProject.getID())
                    .setDone(!logItem.isDone())
                    .setIndex(position)
                    .build();
            Log.e("done?----", "onBindViewHolder: " + logStatus.getDone() );
            blockingStub.setStatus(logStatus);

        });
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }
}
