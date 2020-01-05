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

import cs.android.task.MyApplication;
import cs.android.task.R;
import cs.android.task.myFile.MyFile;
import cs.android.task.view.main.MainActivity;

import cs.android.task.view.signup.SignupActivity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import task.Login;
import task.LoginServiceGrpc;
import task.ProfileOuterClass;
import task.ProfileServiceGrpc;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText phone;
    private EditText password;
    private Button signin;
    private TextView signup;
    private String phone_str;
    private String pwd_str;
    private static String host ;
    private static int port = 50050;
    private String myToken;
    private String myPhone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication myApplication = new MyApplication();
        host = myApplication.getHost();

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
//                Intent profile = new Intent(this, MainActivity.class);
//                startActivity(profile);

                phone_str = phone.getText().toString();
                pwd_str = password.getText().toString();

                if(phone_str.length() != 0 && !" ".equals(phone_str) && phone_str.length() != 0 && !" ".equals(phone_str)){
                    Callable<String> login = () -> Login(phone_str, pwd_str);

                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    Future<String> future = executorService.submit(login);

                    try {
                        //设置超时时间
                        myToken = future.get(5, TimeUnit.SECONDS);

                        if(!" ".equals(myToken) && myToken.length() != 0){
                            Intent profile = new Intent(this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putCharSequence("token", myToken);
                            bundle.putCharSequence("phone", phone_str);
                            profile.putExtras(bundle);
                            Toast.makeText(this,"Sign in Success",Toast.LENGTH_LONG).show();
                            startActivity(profile);
                        }
                        else{
                            Toast.makeText(this,"Password Error",Toast.LENGTH_LONG).show();
                        }
                    } catch (TimeoutException e) {
                        Toast.makeText(this,"TimeoutException",Toast.LENGTH_LONG).show();
                    } catch(Exception e){
                        Log.e("bug----------------->", e + " ");
                        Toast.makeText(this,"This phone hasn't been registered",Toast.LENGTH_LONG).show();
                    }finally {
                        executorService.shutdown();
                    }
                }
                else{
                    Toast.makeText(this,"Input entire messages",Toast.LENGTH_LONG).show();
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
        LoginServiceGrpc.LoginServiceBlockingStub blockingStub = LoginServiceGrpc.newBlockingStub(channel);
        Login.LoginInfo loginInfo = Login.LoginInfo.newBuilder()
                .setPhoneNum(phoneNum)
                .setPassword(passwd)
                .build();
        Login.Token token = blockingStub.login(loginInfo);


        Log.e("token-----", "Login: " + token.getToken() );

        writeFile(1,token.getToken());
        writeFile(2,phoneNum);

        channel.shutdown();
        return token.getToken();
    }





    public void writeFile(int index, String token){
        new MyFile().writeData(index, token);
    }

    public String readFile(int index){
        String[] fileArray = {"token.txt", "phone.txt"};
        String filename = "/sdcard/task/" + fileArray[index - 1];
        File file = new File(filename);
        String str = new MyFile().getFileContent(file);
        return str;
    }

    public void Vertify(){
        myToken = readFile(1);
        myPhone = readFile(2);

        Log.e("token -------->", "Vertify: " + myToken );
        Log.e("token -------->", "Vertify: " + myPhone );
        Toast.makeText(getBaseContext(), myToken , Toast.LENGTH_SHORT).show();

        if(myToken.length() != 0 && !" ".equals(myToken)){
            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                    .usePlaintext().build();
            LoginServiceGrpc.LoginServiceBlockingStub blockingStub = LoginServiceGrpc.newBlockingStub(channel); //通道

            //把myToken 放进token,
            Login.Token token = Login.Token.newBuilder().setToken(myToken).build();

            //返回验证结果
            Callable<Login.Result> check = () -> blockingStub.checkToken(token);

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<Login.Result> future = executorService.submit(check);

            try {
                //设置超时时间
                Login.Result result = future.get(5, TimeUnit.SECONDS);

                if(result.getSuccess()){
                    Intent profile = new Intent(this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putCharSequence("token", myToken);
                    bundle.putCharSequence("phone", myPhone);
                    profile.putExtras(bundle);


                    startActivity(profile);
                }
            } catch (TimeoutException e) {
                Toast.makeText(this,"TimeoutException",Toast.LENGTH_LONG).show();
            } catch(Exception e){
                Toast.makeText(this,"ServicerException",Toast.LENGTH_LONG).show();
            }finally {
                executorService.shutdown();
                channel.shutdown();
            }

        }
    }
}
