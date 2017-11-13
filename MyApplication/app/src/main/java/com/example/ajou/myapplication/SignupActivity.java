package com.example.ajou.myapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

// 회원가입 activity
public class SignupActivity extends AppCompatActivity {
    Button signupBtn;
    EditText email;
    EditText password;
    EditText passwordCheck;
    EditText nickname;

    String sEmail, sPasswd, sConfPasswd, sNickName;
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
                clickSign(v);
            }
        });
    }

    // 회원가입 버튼 클릭 이벤트
    public void clickSign(View v){

        sEmail = email.getText().toString();
        sPasswd = password.getText().toString();
        sConfPasswd = passwordCheck.getText().toString();
        sNickName = nickname.getText().toString();

        // 비밀번호와 확인이 일치할 경우
        if(sPasswd.equals(sConfPasswd))
        {
            InsertData task = new InsertData();
            task.execute(sEmail, sPasswd, sNickName);

            this.finish();
        }
        // 비밀번호 불일치
        else
        {

        }
    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SignupActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
        }


        @Override
        protected String doInBackground(String... params) {

            String email = (String)params[0];
            String pw = (String)params[1];
            String nickname = (String)params[2];

            String serverURL  = "http://52.41.114.24/enterview/signUp.php";
            String postParameters = "email=" + email + "&pw=" + pw + "&nickname=" +nickname;

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                //httpURLConnection.setRequestProperty("content-type", "application/json");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{

                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();


            } catch (Exception e) {

                Log.d("안된다고", "InsertData: Error ", e);
                return new String("Error: " + e.getMessage());
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
