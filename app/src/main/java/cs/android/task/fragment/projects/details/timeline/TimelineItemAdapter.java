package cs.android.task.fragment.projects.details.timeline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.github.vipulasri.timelineview.TimelineView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import cs.android.task.R;
import cs.android.task.entity.LogItem;


public class TimelineItemAdapter
        extends RecyclerView.Adapter<TimelineItemAdapter.TimelineViewHolder> {

private List<LogItem> logItems;
private SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日", Locale.CHINA);

class TimelineViewHolder extends RecyclerView.ViewHolder {
    public TextView date;
    public TextView content;
    public TimelineView timeline;

    public TimelineViewHolder (@NonNull View itemView, int viewType) {
        super(itemView);
        date = itemView.findViewById(R.id.log_date);
        content = itemView.findViewById(R.id.log_content);
        timeline = itemView.findViewById(R.id.timeline);
        timeline.initLine(viewType);
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
