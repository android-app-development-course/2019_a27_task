package cs.android.task.view.login;

import androidx.appcompat.app.AppCompatActivity;


import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cs.android.task.R;
import cs.android.task.view.main.MainActivity;

import cs.android.task.view.signup.SignupActivity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import task.LoginGrpc;
import task.LoginOuterClass;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText phone;
    private EditText password;
    private Button signin;
    private TextView signup;
    private String phone_str;
    private String pwd_str;
    private static String host = "10.242.2.42";
    private static int port = 50050;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signin = findViewById(R.id.signin);
        signin.setOnClickListener(this);
        signup = findViewById(R.id.goToSignup);
        signup.setOnClickListener(this);
        phone = findViewById(R.id.signin_phone);
        password = findViewById(R.id.signin_password);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signin:
                phone_str = phone.getText().toString();
                pwd_str = phone.getText().toString();
                Intent profile = new Intent(this, MainActivity.class);
                String token = Login(phone_str, pwd_str);
                profile.putExtra("phone_num", phone_str);
                profile.putExtra("token", token);
                startActivity(profile,
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

                Toast.makeText(this,"Sign in Fail",Toast.LENGTH_LONG).show();

                break;
            case R.id.goToSignup:
                Intent profile1 = new Intent(this, SignupActivity.class);
                startActivity(profile1);
                break;
            default:
                break;
        }
    }

    public String Login(String phoneNum, String passwd) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext().build();
        LoginGrpc.LoginBlockingStub blockingStub = LoginGrpc.newBlockingStub(channel);
        LoginOuterClass.LoginInfo loginInfo = LoginOuterClass.LoginInfo.newBuilder()
                .setPhoneNum(phoneNum)
                .setPassword(passwd)
                .build();
        LoginOuterClass.Token token = blockingStub.login(loginInfo);
        Log.d("login", token.getToken());
        if (!token.getToken().isEmpty()) {
            Toast.makeText(getBaseContext(), "fail to login", Toast.LENGTH_SHORT).show();
        }
        channel.shutdown();
        return token.getToken();
    }



    public void signIn() {}

}
