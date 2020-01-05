package cs.android.task.fragment.schedule;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import cs.android.task.R;
import cs.android.task.entity.Schedule;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private List<Schedule> mScheduleList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView date;
        TextView location;
        TextView mark;
        MaterialButton doneBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.schedule_name);
            date = itemView.findViewById(R.id.schedule_date);
            location = itemView.findViewById(R.id.schedule_location);
            mark = itemView.findViewById(R.id.schedule_mark);
            doneBtn = itemView.findViewById(R.id.done_btn);
            doneBtn.setOnClickListener(this::done);
        }

        private void done(View view) {
            ScheduleAdapter.this.mScheduleList.remove(this.getAdapterPosition());
            ScheduleAdapter.this.notifyItemRemoved(this.getAdapterPosition());
            /*
            数据库操作
             */
        }

    }

    public ScheduleAdapter(List<Schedule> scheduleList) {
        mScheduleList = scheduleList;
    }

    @NonNull
    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_card, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.ViewHolder holder, int position) {

        Schedule schedule = mScheduleList.get(position);
        holder.name.setText(schedule.getName());
        holder.date.setText(schedule.getDate().toString());
        holder.location.setText(schedule.getLocation());
        holder.mark.setText(schedule.getMark());

    }

    @Override
    public int getItemCount() {
        return mScheduleList.size();
    }

}
