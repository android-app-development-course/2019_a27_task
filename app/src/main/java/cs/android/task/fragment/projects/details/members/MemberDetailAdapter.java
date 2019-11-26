package cs.android.task.fragment.projects.details.members;


import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Context;

import androidx.cardview.widget.CardView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cs.android.task.R;
import cs.android.task.entity.Member;

public class MemberDetailAdapter extends RecyclerView.Adapter<MemberDetailAdapter.MemberViewHolder> {

private List<Member> members;
private SparseBooleanArray expandState = new SparseBooleanArray();
private Context context;

public class MemberViewHolder extends RecyclerView.ViewHolder {
    private TextView name, phoneNum, email;
    private ImageView avatar;
    private LinearLayout expandableLayout;
    private ImageButton expandBtn;
    public MemberViewHolder (@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.member_name);
        phoneNum = itemView.findViewById(R.id.member_phone);
        email = itemView.findViewById(R.id.member_mail);
        avatar = itemView.findViewById(R.id.member_avatar);
        expandableLayout = itemView.findViewById(R.id.expandable_layout);
        expandBtn = itemView.findViewById(R.id.expand_btn);

        CardView cardview = itemView.findViewById(R.id.member_card);
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



MemberDetailAdapter(@NonNull List<Member> members) {
    this.members = members;
    for (int i = 0; i < members.size(); i++)
        expandState.append(i, false);
}

@NonNull
@Override
public MemberViewHolder onCreateViewHolder (@NonNull ViewGroup parent,int viewType) {
    this.context = parent.getContext();
    View view = LayoutInflater.from(this.context).inflate(R.layout.member_item, parent, false);
    return new MemberViewHolder(view);
}

@Override
public void onBindViewHolder (@NonNull MemberViewHolder holder,int position) {

    holder.setIsRecyclable(false);

    holder.name.setText(members.get(position).getName());
    holder.email.setText(members.get(position).getEmail());
    holder.phoneNum.setText(members.get(position).getPhoneNum());

    /*
    TODO
    get and set member avatar here;
     */
    holder.avatar.setImageDrawable(this.context.getDrawable(R.mipmap.leader_bg));

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
    return members.size();
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
