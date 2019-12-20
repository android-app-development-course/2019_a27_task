package cs.android.task.view.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cs.android.task.R;
import cs.android.task.view.login.LoginActivity;
import cs.android.task.view.main.MainActivity;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{
    private Button signup;
    private EditText phone;
    private EditText pwd;
    private EditText againPwd;
    private EditText email;

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


                if(signUpSuccess()){
                    Toast.makeText(this,"Sign up Success",Toast.LENGTH_LONG).show();
                    Intent profile = new Intent(this, LoginActivity.class);
                    startActivity(profile);
                }
                else{
                    Toast.makeText(this,"Sign up Fail",Toast.LENGTH_LONG).show();
                }

                break;
                default:break;
        }
    }

    public boolean signUpSuccess(){
        String phoneStr = phone.getText().toString();
        String pwdStr = pwd.getText().toString();
        String againPwdStr = againPwd.getText().toString();
        String emailStr = email.getText().toString();


        if(!(" ".equals(phoneStr)) && phoneStr.length() != 0
                && !(" ".equals(pwdStr)) && pwdStr.length() != 0
                && !(" ".equals(againPwdStr)) && againPwdStr.length() != 0
                && !(" ".equals(emailStr)) && emailStr.length() != 0
                && pwdStr.equals(againPwdStr)){


            //注册接口 存入数据库

            return true;
        }

        else return false;
    }
}
