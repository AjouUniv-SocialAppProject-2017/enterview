package com.example.ajou.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SignupActivity extends AppCompatActivity {
    Button signupBtn;
    EditText email;
    EditText password;
    EditText passwordCheck;
    EditText nickname;
    // 푸시 알림 radiobutton 필요
    // 직무선택 선택하는 것 구현 필요
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        signupBtn = (Button) findViewById(R.id.btn_signupComplete);
        email = (EditText) findViewById(R.id.signup_input_email);
        nickname = (EditText)findViewById(R.id.signup_input_nickname);
        password = (EditText)findViewById(R.id.signup_input_password);
        passwordCheck = (EditText) findViewById(R.id.signup_input_password_check);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력 정보 조건 check
                // check 후 로그인 화면으로 이동

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
