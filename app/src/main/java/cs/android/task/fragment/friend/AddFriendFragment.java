package cs.android.task.fragment.friend;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.pchmn.materialchips.ChipsInput;

import java.util.ArrayList;
import java.util.List;

import cs.android.task.MyApplication;
import cs.android.task.R;
import cs.android.task.entity.Friend;
import cs.android.task.view.main.MainActivity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import task.FriendServiceGrpc;
import task.Login;
import task.ProfileOuterClass;
import task.ProjectOuterClass;
import task.ProjectServiceGrpc;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddFriendFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddFriendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFriendFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    // TODO: Rename and change types of parameters

    private View view;
    private static String host;
    private static int port = 50050;
    private ProfileOuterClass.Profile myProfile;
    private EditText phone;
    private String phoneStr;


    public AddFriendFragment() {
        // Required empty public constructor
    }

    public static AddFriendFragment newInstance() {
        AddFriendFragment fragment = new AddFriendFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_friend, container, false);
        phone = view.findViewById(R.id.add_friend_phone);
        MyApplication myApplication = new MyApplication();
        host = myApplication.getHost();

        myProfile = ((MainActivity)getActivity()).getMyProfile();
        Log.e("e---------->", "onCreateView: " + myProfile );



        ((MaterialButton)view.findViewById(R.id.add_ok)).setOnClickListener(v->{
            phoneStr = phone.getText().toString();
            Log.e("e------------------>", "onCreateView: " + phoneStr );
            if(phoneStr.length() != 0 && !" ".equals(phoneStr)){
                if(addFriend()){
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);

                    if (null != view) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    ((MainActivity) getActivity()).getFriendFragment().initFriends();
                    Toast.makeText(getContext(),"Add Friend Success",Toast.LENGTH_LONG).show();
                    this.getFragmentManager().popBackStack();

                }
                else{
                    Toast.makeText(getContext(), "Add friend fail", Toast.LENGTH_LONG).show();
                }


            }
            else{
                Toast.makeText(getContext(), "Input entire messages", Toast.LENGTH_LONG).show();
            }

        });
        ((MaterialButton)view.findViewById(R.id.add_cancel)).setOnClickListener(v->{
            cancel();
        });

        return view;

    }


    /*
    添加好友，跳转回去好友界面
     */
    public boolean addFriend(){

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext().build();

        FriendServiceGrpc.FriendServiceBlockingStub blockingStub = FriendServiceGrpc.newBlockingStub(channel);
        ProfileOuterClass.Profile friend = ProfileOuterClass.Profile.newBuilder()
                .setToken(myProfile.getToken())
                .setPhoneNum(phoneStr)
                .build();

        Login.Result result = blockingStub.addFriend(friend);

       return result.getSuccess();


    }

    public void cancel(){

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);

        if (null != view) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        this.getFragmentManager().popBackStack();
    }


}
