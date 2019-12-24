package cs.android.task.fragment.Note;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cs.android.task.R;
import cs.android.task.entity.Member;
import cs.android.task.entity.Note;

public class MyNoteFragment extends Fragment {
    private View view;
    private List<Note> noteList;



    public MyNoteFragment() {
        // Required empty public constructor
    }

    public static MyNoteFragment newInstance() {
        MyNoteFragment fragment = new MyNoteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        noteList = new ArrayList<>();
        Note note = new Note();
        note.setContent("hello");
        Member member = new Member();
        member.setName("LMS");
        member.setEmail("iamlms@qq.com");
        member.setPhoneNum("1234567890");
        note.setDate(new Date());
        note.setCommiter(member);
        noteList.add(note);
        view = inflater.inflate(R.layout.fragment_my_note, container, false);
        MyNoteAdapter myNoteAdapter = new MyNoteAdapter(MyNoteFragment.this, noteList);
        CollapsingToolbarLayout collapsingToolbarLayout =
                view.findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#ffffff"));
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#ffffff"));
        collapsingToolbarLayout.setContentScrimColor(Color.parseColor("#e16b6b"));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.note_list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myNoteAdapter);
        return view;
    }


}
