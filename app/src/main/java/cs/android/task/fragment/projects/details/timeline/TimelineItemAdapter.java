package cs.android.task.fragment.projects.details.timeline;

import androidx.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;


import com.github.vipulasri.timelineview.TimelineView;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;
import cs.android.task.R;
import cs.android.task.entity.LogItem;


public class TimelineItemAdapter
        extends RecyclerView.Adapter<TimelineItemAdapter.TimelineViewHolder> {

private List<LogItem> logItems;
private SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日", Locale.CHINA);

class TimelineViewHolder extends RecyclerView.ViewHolder {
    public TextView date;
    public TextView content;
    public TextView committer;
    public TimelineView timeline;
    public CheckBox isFinish;

    public TimelineViewHolder (@NonNull View itemView, int viewType) {
        super(itemView);
        date = itemView.findViewById(R.id.log_date);
        content = itemView.findViewById(R.id.log_content);
        committer = itemView.findViewById(R.id.log_committer);
        timeline = itemView.findViewById(R.id.timeline);
        isFinish = itemView.findViewById(R.id.isFinish);
        timeline.initLine(viewType);
        isFinish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Log.e("tag->", "点击完成");
                    /*
                    已完成
                     */
                }else {
                    Log.e("tag->", "点击未完成");
                    /*
                    未完成
                     */
                }

            }
        });
    }
}

public TimelineItemAdapter(List<LogItem> logItems) {
    this.logItems = logItems;
}

@NonNull
@Override
public TimelineItemAdapter.TimelineViewHolder
onCreateViewHolder (@NonNull ViewGroup parent,int viewType) {
    View view = LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.timeline_item, parent, false);
    return new TimelineViewHolder(view, viewType);
}

@Override
public void onBindViewHolder (@NonNull TimelineViewHolder holder,int position) {
    LogItem item = logItems.get(position);
    holder.date.setText(dateFormat.format(item.getDate()));
    holder.content.setText(item.getContent());
    holder.committer.setText(item.getCommiter().getName());
}

@Override
public int getItemCount () {
    return logItems.size();
}

@Override
public int getItemViewType(int position) {
    return TimelineView.getTimeLineViewType(position, getItemCount());
}
}
