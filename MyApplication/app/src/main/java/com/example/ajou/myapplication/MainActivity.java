package com.example.ajou.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button loginBtn;
    Button signupBtn;
    EditText input_email;
    EditText input_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginBtn = (Button) findViewById(R.id.btn_login);
        signupBtn = (Button) findViewById(R.id.btn_signup);
        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText)findViewById(R.id.input_password);
        // login button 클릭 시 이벤트
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    login();
            }
        });
        // signup button 클릭 시 이벤트
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    void login(){
        // 로그인 구현
        String email;
        String password;

        email = input_email.getText().toString();
        password = input_password.getText().toString();
        Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
        startActivity(intent);
        // 등록된 회원 정보와 비교 후 로그인 성공 실패 여부 결정
    }
}
