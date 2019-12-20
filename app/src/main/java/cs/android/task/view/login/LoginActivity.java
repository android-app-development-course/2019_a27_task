package cs.android.task.view.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cs.android.task.R;
import cs.android.task.view.main.MainActivity;
import cs.android.task.view.signup.SignupActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText phone;
    private EditText password;
    private Button signin;
    private TextView signup;
    private String phone_str;
    private String pwd_str;

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
                if(signUpSuccess()){
                    Toast.makeText(this,"Sign in Success",Toast.LENGTH_LONG).show();
                    Intent profile = new Intent(this, MainActivity.class);
                    startActivity(profile);
                }
                else{
                    Toast.makeText(this,"Sign in Fail",Toast.LENGTH_LONG).show();
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

    public boolean signUpSuccess(){
        if(!(" ".equals(phone_str)) && phone_str.length() != 0 && !(" ".equals(pwd_str)) && pwd_str.length() != 0){
            //接口
            return true;
        }
        return true;
    }
}
