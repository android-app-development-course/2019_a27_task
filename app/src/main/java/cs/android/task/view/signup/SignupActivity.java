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

import cs.android.task.R;
import cs.android.task.view.login.LoginActivity;
import cs.android.task.view.main.MainActivity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import task.LoginGrpc;
import task.LoginOuterClass;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{
    private Button signup;
    private EditText phone;
    private EditText pwd;
    private EditText againPwd;
    private EditText email;
    private static String host = "10.242.93.99";
    private static int port = 50050;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signup = findViewById(R.id.signupButton);
        signup.setOnClickListener(this);
        phone = findViewById(R.id.signupPhone);
        pwd = findViewById(R.id.signupPassword);
        againPwd = findViewById(R.id.signupAgainPassword);
        email = findViewById(R.id.signupEmail);
        pwd.setOnClickListener(this);
        againPwd.setOnClickListener(this);
        phone.setOnClickListener(this);
        email.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signupButton:


                Callable<Boolean> signup = this::SignUp;

                ExecutorService executorService = Executors.newSingleThreadExecutor();
                Future<Boolean> future = executorService.submit(signup);

                boolean result_regist = false;
                try {
                    //设置超时时间
                    result_regist = future.get(5, TimeUnit.SECONDS);

                } catch (TimeoutException e) {
                    Toast.makeText(this,"TimeoutException",Toast.LENGTH_LONG).show();
                } catch(Exception e){
                    Log.d("bug----------------->", e + " ");
                    Toast.makeText(this,"ServicerException",Toast.LENGTH_LONG).show();
                }finally {
                    executorService.shutdown();
                    if(result_regist == true){
                        Toast.makeText(this,"Sign up Success",Toast.LENGTH_LONG).show();
                        Intent profile = new Intent(this, LoginActivity.class);
                        startActivity(profile);
                    }
                    else{
                        Toast.makeText(this,"This phone has already been registered",Toast.LENGTH_LONG).show();
                    }
                }

                break;
                default:break;
        }
    }

    public boolean SignUp(){
        String phoneStr = phone.getText().toString();
        String pwdStr = pwd.getText().toString();
        String againPwdStr = againPwd.getText().toString();
        String emailStr = email.getText().toString();

        if(!(" ".equals(phoneStr)) && phoneStr.length() != 0
                && !(" ".equals(pwdStr)) && pwdStr.length() != 0
                && !(" ".equals(againPwdStr)) && againPwdStr.length() != 0
                && !(" ".equals(emailStr)) && emailStr.length() != 0
                && pwdStr.equals(againPwdStr)) {

            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                    .usePlaintext().build();
            LoginGrpc.LoginBlockingStub blockingStub = LoginGrpc.newBlockingStub(channel);
            LoginOuterClass.LoginInfo loginInfo = LoginOuterClass.LoginInfo.newBuilder()
                    .setPhoneNum(phoneStr)
                    .setPassword(pwdStr)
                    .build();
            LoginOuterClass.Result result = blockingStub.register(loginInfo);



            channel.shutdown();
            return result.getSuccess();
        }
        return false;
    }

}
