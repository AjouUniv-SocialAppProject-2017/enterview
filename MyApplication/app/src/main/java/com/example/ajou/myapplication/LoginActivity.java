package com.example.ajou.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn;
    Button signupBtn;
    EditText input_email;
    EditText input_password;
    String email, password;

    public static String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
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
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    //171113 서버test
    void login(){
        // 로그인 구현
        try {

            email = input_email.getText().toString();
            password = input_password.getText().toString();

            loginDB lDB = new loginDB();
            lDB.execute();

        } catch (NullPointerException e) {
            Log.e("err", e.getMessage());
        }
    }


    public class loginDB extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            String param = "email=" + email + "&password=" + password ;

            Log.e("POST", param);
            try {
                /* 서버연결 */
                URL url = new URL("http://52.41.114.24/enterview/logIn.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                /* 서버 -> 안드로이드 파라메터값 전달 */
                InputStream is = null;
                BufferedReader in = null;
                String data = "";

                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();

                /* 서버에서 응답 */
                Log.e("RECV DATA", data);
                Log.e("RESULT", data);

                if (!(data.equals("0"))||Integer.parseInt(data)!=0) {

                    Log.e("RESULT", "성공적으로 처리되었습니다!"+data+"data");
                    //Toast.makeText(getApplicationContext(), "완료", Toast.LENGTH_SHORT).show();
                    userId = data;
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                } else { // 로그인 실패 , 다시 확인하라고 알림창 띄우기
                    Log.e("RESULT", "에러 발생! ERRCODE = " + data);
                    //Toast.makeText(getApplicationContext(), "오류", Toast.LENGTH_SHORT).show();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
