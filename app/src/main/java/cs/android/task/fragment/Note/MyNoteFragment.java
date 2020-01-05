package cs.android.task.fragment.Note;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.DateKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import cs.android.task.MyApplication;
import cs.android.task.R;
import cs.android.task.entity.Member;
import cs.android.task.entity.Note;
import cs.android.task.view.main.MainActivity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import task.Login;
import task.Message;
import task.MessageServiceGrpc;
import task.ProfileOuterClass;

public class MyNoteFragment extends Fragment {
    private View view;
    private List<Note> noteList;
    private static String host;
    private static int port = 50050;
    private ProfileOuterClass.Profile myProfile;
    private Iterator<Message.Msg> myMessage;

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
        note.setDate(new Date());
        note.setCommiter("lms");
        noteList.add(note);
        view = inflater.inflate(R.layout.fragment_my_note, container, false);
        MyApplication myApplication = new MyApplication();
        host = myApplication.getHost();

        myProfile = ((MainActivity)getActivity()).getMyProfile();

        freshNote();




        MyNoteAdapter myNoteAdapter = new MyNoteAdapter(MyNoteFragment.this, noteList);
        CollapsingToolbarLayout collapsingToolbarLayout =
                view.findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#ffffff"));
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#000000"));
        collapsingToolbarLayout.setContentScrimColor(Color.parseColor("#2196f3"));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.note_list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myNoteAdapter);


        return view;
    }


    public void freshNote(){

        Log.e("e----->", "freshNote: " + host  + " " + port);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext().build();
        MessageServiceGrpc.MessageServiceBlockingStub stub = MessageServiceGrpc.newBlockingStub(channel);

        Login.Token token = Login.Token.newBuilder().setToken(myProfile.getToken()).build();


        try{
            myMessage = stub.askMessage(token);
        }catch (StatusRuntimeException e){
            Log.e("bug???", "freshNote: "+ "bug");
        }finally {
            while (myMessage.hasNext()){
                Message.Msg msg =myMessage.next();
                Note note = new Note();
                note.setContent(msg.getContent());
                note.setCommiter(msg.getCommitBy());
                note.setDate(new Date(msg.getDate()));
                noteList.add(note);
            }
            channel.shutdown();
        }

    }

}
