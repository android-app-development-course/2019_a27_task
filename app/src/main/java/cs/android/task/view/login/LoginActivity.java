package cs.android.task.view.login;

import androidx.appcompat.app.AppCompatActivity;
import cs.android.task.R;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText phone;
    private Button login;
    private TextView normalLogin;
    private TextView moreWay;
    private String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phone = findViewById(R.id.phone);
        login = findViewById(R.id.login);
        normalLogin = findViewById(R.id.normalLogin);
        moreWay = findViewById(R.id.moreWay);

        phone.setOnClickListener(this);
        login.setOnClickListener(this);
        normalLogin.setOnClickListener(this);
        moreWay.setOnClickListener(this);

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                number = phone.getText().toString();
                if(!"".equals(number)){
                    login.setBackgroundColor(Color.BLUE);
                }else{
                    login.setBackgroundColor(Color.parseColor("#969393"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
