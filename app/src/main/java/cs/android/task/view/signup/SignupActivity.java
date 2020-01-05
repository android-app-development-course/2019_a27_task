package cs.android.task.view.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cs.android.task.MyApplication;
import cs.android.task.R;
import cs.android.task.view.login.LoginActivity;
import cs.android.task.view.main.MainActivity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import task.LoginServiceGrpc;
import task.Login;
import task.ProfileOuterClass;
import task.ProfileServiceGrpc;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{
    private Button signup;
    private EditText phone;
    private EditText pwd;
    private EditText name;
    private EditText email;
    private String phoneStr;
    private String pwdStr;
    private String nameStr;
    private String emailStr;

    private static String host ;
    private static int port = 50050;
    private ManagedChannel channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        MyApplication myApplication = new MyApplication();
        host = myApplication.getHost();
        signup = findViewById(R.id.signupButton);
        signup.setOnClickListener(this);
        phone = findViewById(R.id.signupPhone);
        pwd = findViewById(R.id.signupPassword);
        name = findViewById(R.id.signupName);
        email = findViewById(R.id.signupEmail);
        pwd.setOnClickListener(this);
        name.setOnClickListener(this);
        phone.setOnClickListener(this);
        email.setOnClickListener(this);
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext().build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signupButton:
                phoneStr = phone.getText().toString();
                pwdStr = pwd.getText().toString();
                nameStr = name.getText().toString();
                emailStr = email.getText().toString();

                if(!(" ".equals(phoneStr)) && phoneStr.length() != 0
                        && !(" ".equals(pwdStr)) && pwdStr.length() != 0
                        && !(" ".equals(nameStr)) && nameStr.length() != 0
                        && !(" ".equals(emailStr)) && emailStr.length() != 0) {


                    Callable<Boolean> signup = this::SignUp;
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    Future<Boolean> future = executorService.submit(signup);

                    boolean result_regist = false;
                    try {
                        //设置超时时间
                        result_regist = future.get(5, TimeUnit.SECONDS);


                        LoginServiceGrpc.LoginServiceBlockingStub blockingStub = LoginServiceGrpc.newBlockingStub(channel);
                        Login.LoginInfo loginInfo = Login.LoginInfo.newBuilder()
                                .setPhoneNum(phoneStr)
                                .setPassword(pwdStr)
                                .build();

                        Login.Token token = blockingStub.login(loginInfo);

                        ProfileServiceGrpc.ProfileServiceBlockingStub profileBlockingStub = ProfileServiceGrpc.newBlockingStub(channel);
                        ProfileOuterClass.Profile Info = ProfileOuterClass.Profile.newBuilder()
                                .setPhoneNum(phoneStr)
                                .setEmail(emailStr)
                                .setName(nameStr)
                                .setToken(token.getToken())
                                .build();

                        profileBlockingStub.setProfile(Info);

                    } catch (TimeoutException e) {
                        Toast.makeText(this, "TimeoutException", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.d("bug----------------->", e + " ");
                        Toast.makeText(this, "ServicerException", Toast.LENGTH_LONG).show();
                    } finally {
                        channel.shutdown();
                        executorService.shutdown();
                        if (result_regist == true) {
                            Toast.makeText(this, "Sign up Success", Toast.LENGTH_LONG).show();
                            Intent profile = new Intent(this, LoginActivity.class);
                            startActivity(profile);
                        } else {
                            Toast.makeText(this, "This phone has already been registered", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else{
                    Toast.makeText(this,"Input entire messages",Toast.LENGTH_LONG).show();
                }

                break;
                default:break;
        }
    }

    public boolean SignUp(){

        LoginServiceGrpc.LoginServiceBlockingStub blockingStub = LoginServiceGrpc.newBlockingStub(channel);
        Login.LoginInfo loginInfo = Login.LoginInfo.newBuilder()
                .setPhoneNum(phoneStr)
                .setPassword(pwdStr)
                .build();
        Login.Result result = blockingStub.register(loginInfo);


        return result.getSuccess();

    }

}
