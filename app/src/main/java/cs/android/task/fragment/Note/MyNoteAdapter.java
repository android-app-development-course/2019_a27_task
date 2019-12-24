package cs.android.task.fragment.Note;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cs.android.task.R;
import cs.android.task.entity.Note;

public class MyNoteAdapter extends RecyclerView.Adapter<MyNoteAdapter.ViewHolder> {
    private MyNoteFragment context;
    private List<Note> noteList;

    public MyNoteAdapter(MyNoteFragment context, List<Note> noteList){
        this.context = context;
        this.noteList = noteList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card, parent, false);
        MyNoteAdapter.ViewHolder holder = new MyNoteAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.noteContext.setText(note.getContent());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView noteContext;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteContext = itemView.findViewById(R.id.note_content);
        }
    }
}
