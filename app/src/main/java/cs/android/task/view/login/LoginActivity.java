package cs.android.task.view.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cs.android.task.R;
import cs.android.task.view.main.MainActivity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import task.LoginGrpc;
import task.LoginOuterClass;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
  private EditText username;
  private EditText password;
  private Button signin;
  private TextView signup;
  private String uname;
  private String pwd;

  private static String host = "10.242.2.42";
  private static int port = 50050;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    setWidget();
    signin.setOnClickListener(this);
  }

  public void setWidget() {
    username = findViewById(R.id.username);
    password = findViewById(R.id.password);
    signin = findViewById(R.id.signin);
    signup = findViewById(R.id.signup);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.signin:
        Intent profile = new Intent(this, MainActivity.class);
        String token = Login(username.getText().toString(), password.getText().toString());
        profile.putExtra("phone_num", username.getText());
        profile.putExtra("token", token);
        startActivity(profile,
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        break;
      default:
        break;
    }
  }

  /**
   * Login send request for login, and return a token
   * A token and username(phone number) must be send to main activity.
   * You should validate phone number and password before you send request.
   *
   * @param phoneNum the phone num
   * @param passwd the passwd
   * @return token - string
   */
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
