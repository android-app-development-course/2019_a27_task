package cs.android.task.fragment.projects.details.files;

import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import cs.android.task.R;
import cs.android.task.entity.File;

public class FileDetailsAdapter extends RecyclerView.Adapter<FileDetailsAdapter.FileViewHolder> {
private List<File> fileList;
private SimpleDateFormat dateFormater = new SimpleDateFormat("MM月dd日",Locale.CHINESE);
private static Map<FileTypes, Drawable> iconMap = new HashMap<>();
private SparseBooleanArray expandState = new SparseBooleanArray();
private Context context;


public FileDetailsAdapter(@NonNull List<File> fileList) {
    this.fileList = fileList;
    for (int i = 0; i < fileList.size(); i++)
        expandState.append(i, false);
}

@NonNull
@Override
public FileViewHolder onCreateViewHolder (@NonNull ViewGroup parent,int viewType) {
    context = parent.getContext();
    View view = LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.file_item, parent, false);
    iconMap.put(FileTypes.AUDIO, context.getResources().getDrawable(R.drawable.audio_file, null));
    iconMap.put(FileTypes.IMAGE, context.getResources().getDrawable(R.drawable.image_file, null));
    iconMap.put(FileTypes.VIDEO, context.getResources().getDrawable(R.drawable.image_file, null));
    iconMap.put(FileTypes.TEXT, context.getResources().getDrawable(R.drawable.text_file, null) );
    iconMap.put(FileTypes.NONETYPE, context.getResources().getDrawable(R.drawable.nonetype_file, null) );
    return new FileViewHolder(view);
}

@Override
public void onBindViewHolder (@NonNull FileViewHolder holder,int position) {
    holder.setIsRecyclable(false);

    // init item
    File file = fileList.get(position);
    holder.owner.setText(file.getOwner());
    holder.lastUpdate.setText(dateFormater.format(file.getLastUpdate()));
    holder.name.setText(file.getName());
    holder.typeIcon.setImageDrawable( iconMap.get(FileTypes.getFileType(file.getName())) );

    //check if view is expanded
    final boolean isExpanded = expandState.get(position);
    holder.expandableLayout.setVisibility(isExpanded?View.VISIBLE:View.GONE);

    holder.expandBtn.setRotation(expandState.get(position) ? 180f : 0f);
    holder.expandBtn.setOnClickListener(v-> {
        onClickButton(holder.expandableLayout, holder.expandBtn,  position);
    });

}

@Override
public int getItemCount () {
    return fileList.size();
}

public class FileViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView lastUpdate;
    TextView owner;
    ImageView typeIcon;
    private LinearLayout expandableLayout;
    private ImageButton expandBtn;

    public FileViewHolder (@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.file_name);
        lastUpdate = itemView.findViewById(R.id.file_last_update);
        owner = itemView.findViewById(R.id.file_owner);
        typeIcon = itemView.findViewById(R.id.file_type_icon);
        expandableLayout = itemView.findViewById(R.id.expandable_layout);
        expandBtn = itemView.findViewById(R.id.expand_btn);

        CardView cardview = itemView.findViewById(R.id.file_card);
        LayoutTransition anim = new LayoutTransition();
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(null,
                "scaleY",
                0, expandableLayout.getHeight());
        ObjectAnimator fade = ObjectAnimator.ofFloat(null,
                "alpha",
                0, 1);
        animatorSet.play(fade).with(scaleY);
        anim.setDuration(300);
        anim.enableTransitionType(LayoutTransition.CHANGING);
        anim.setAnimator(LayoutTransition.CHANGING, animatorSet);
        cardview.setLayoutTransition(anim);
    }
}

private void onClickButton(final LinearLayout expandableLayout, final ImageButton btn, final  int i) {

    //Simply set View to Gone if not expanded
    //Not necessary but I put simple rotation on button layout
    if (expandableLayout.getVisibility() == View.VISIBLE){
        createRotateAnimator(btn, 180f, 0f).start();
        expandableLayout.setVisibility(View.GONE);
        expandState.put(i, false);
    } else {
        createRotateAnimator(btn, 0f, 180f).start();
        expandableLayout.setVisibility(View.VISIBLE);
        expandState.put(i, true);
    }
}

//Code to rotate button
private ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
    ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
    animator.setDuration(300);
    animator.setInterpolator(new LinearInterpolator());
    return animator;
}

}
