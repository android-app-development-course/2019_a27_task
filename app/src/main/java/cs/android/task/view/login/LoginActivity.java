package cs.android.task.view.login;

import androidx.appcompat.app.AppCompatActivity;


import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cs.android.task.R;
import cs.android.task.myFile.MyFile;
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
    private static String host = "10.242.93.99";
    private static int port = 50050;
    private String myToken;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Vertify();
        new Handler().postDelayed((Runnable) () -> {
            setContentView(R.layout.activity_login);
            signin = findViewById(R.id.signin);
            signin.setOnClickListener(this);
            signup = findViewById(R.id.goToSignup);
            signup.setOnClickListener(this);
            phone = findViewById(R.id.signin_phone);
            password = findViewById(R.id.signin_password);
        },100);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signin:
                phone_str = phone.getText().toString();
                pwd_str = phone.getText().toString();
                Intent profile = new Intent(this, MainActivity.class);
                startActivity(profile);


                Callable<String> login = () -> {
                    String token = Login(phone_str, pwd_str);
                    Bundle bundle = new Bundle();
                    bundle.putCharSequence("token", token);
                    profile.putExtras(bundle);

                    Log.e("ee------->", token );
                    return "success";
                };

                ExecutorService executorService = Executors.newSingleThreadExecutor();
                Future<String> future = executorService.submit(login);

                try {
                    //设置超时时间
                    future.get(5, TimeUnit.SECONDS);

                    Toast.makeText(this,"Sign in Success",Toast.LENGTH_LONG).show();
                    startActivity(profile);
                } catch (TimeoutException e) {
                    Toast.makeText(this,"TimeoutException",Toast.LENGTH_LONG).show();
                } catch(Exception e){
                    //Log.d("bug----------------->", e + " ");
                    Toast.makeText(this,"ServicerException",Toast.LENGTH_LONG).show();
                }finally {
                    executorService.shutdown();
                }

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


        Log.e("token-----", "Login: " + token.getToken() );
        writeFile(token.getToken());

        channel.shutdown();
        return token.getToken();
    }



    public void signIn() {}


    public void writeFile(String token){
        new MyFile().writeData(token);
    }

    public String readFile(){
        File file = new File("/sdcard/task/token.txt");
        String token = new MyFile().getFileContent(file);
        return token;
    }

    public void Vertify(){
        myToken = readFile();

        Toast.makeText(getBaseContext(), myToken , Toast.LENGTH_SHORT).show();


        if(myToken.length() != 0 && !" ".equals(myToken)){
            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                    .usePlaintext().build();
            LoginGrpc.LoginBlockingStub blockingStub = LoginGrpc.newBlockingStub(channel); //通道

            //把myToken 放进token,
            LoginOuterClass.Token token = LoginOuterClass.Token.newBuilder().setToken(myToken).build();

            //返回验证结果
            LoginOuterClass.Result result =  blockingStub.checkToken(token);


            Log.e("TOKEN---------------->", "Vertify: " + myToken );
            Log.e("TOKEN---------------->", "Vertify: " + myToken.length());
            Log.e("结果---------------->", "Vertify: " + result.getSuccess() );

            if(result.getSuccess()){
                Intent profile = new Intent(this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putCharSequence("token", myToken);
                profile.putExtras(bundle);
                startActivity(profile);
            }

            channel.shutdown();
        }
    }
}
