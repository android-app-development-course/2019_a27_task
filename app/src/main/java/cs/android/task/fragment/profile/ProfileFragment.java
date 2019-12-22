package cs.android.task.fragment.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cs.android.task.R;
import cs.android.task.view.login.LoginActivity;
import cs.android.task.view.main.MainActivity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import task.LoginGrpc;
import task.LoginOuterClass;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    private static String host = "10.242.93.99";
    private static int port = 50050;
    private View view;
    private String myToken;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        myToken = ((MainActivity)getActivity()).getMyToken();

        view.findViewById(R.id.profile_logout_button).setOnClickListener(v->{
            Callable<Boolean> signup = this::LogOut;

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<Boolean> future = executorService.submit(signup);

            boolean result_logout = false;
            try {
                //设置超时时间
                result_logout = future.get(5, TimeUnit.SECONDS);

            } catch (TimeoutException e) {
                Toast.makeText(getContext(),"TimeoutException",Toast.LENGTH_LONG).show();
            } catch(Exception e){
                Log.d("bug----------------->", e + " ");
                Toast.makeText(getContext(),"ServicerException",Toast.LENGTH_LONG).show();
            }finally {
                executorService.shutdown();
                if(result_logout == true){
                    Toast.makeText(getContext(),"Logout Success",Toast.LENGTH_LONG).show();
                    Intent profile = new Intent(getContext(), LoginActivity.class);
                    startActivity(profile);
                }

            }

        });

        return view;

    }

    public boolean LogOut(){

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext().build();
        LoginGrpc.LoginBlockingStub blockingStub = LoginGrpc.newBlockingStub(channel);
        LoginOuterClass.Token token = LoginOuterClass.Token.newBuilder().setToken(myToken).build();
        LoginOuterClass.Result result = blockingStub.logout(token);

        channel.shutdown();
        return result.getSuccess();
    }

}
