package com.example.ajou.myapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    RadioGroup Notigroup;
    Spinner majorSpinner;

    String sEmail, sPasswd, sConfPasswd, sNickName , sNotification, sMajor ;
    String s = null;
    String s2 = "";
    // 0이면 선택 x , 1이면 accept noti, 2이면 deny noti
    int Notiflag = 0;

    // 푸시 알림 radiobutton 필요
    // 직무선택 선택하는 것 구현 필요
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        signupBtn = (Button) findViewById(R.id.btn_signupComplete);
        email = (EditText) findViewById(R.id.signup_input_email);
        nickname = (EditText) findViewById(R.id.signup_input_nickname);
        password = (EditText) findViewById(R.id.signup_input_password);
        passwordCheck = (EditText) findViewById(R.id.signup_input_password_check);
        Notigroup = (RadioGroup) findViewById(R.id.Notigroup);
        majorSpinner = (Spinner)findViewById(R.id.major_select);

        // major spinner 제어
        final ArrayAdapter majorAdapter = ArrayAdapter.createFromResource(this,
                R.array.major_select, android.R.layout.simple_spinner_item);
        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        majorSpinner.setAdapter(majorAdapter);


        Notigroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.acceptNoti){
                    Notiflag = 1;
                    sNotification = "수신";
                }else if(checkedId == R.id.denyNoti){
                    Notiflag = 2;
                    sNotification = "비수신";
                }else{
                    Notiflag = 0;
                }
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sEmail = email.getText().toString();
                sPasswd = password.getText().toString();
                sConfPasswd = passwordCheck.getText().toString();
                sNickName = nickname.getText().toString();
                sMajor = majorSpinner.getSelectedItem().toString();
                if(sEmail.equals(s) || sEmail.equals(s2)){
                    AlertDialog.Builder alert = new AlertDialog.Builder(SignupActivity.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.setMessage("E-mail을 입력해 주세요");
                    alert.show();
                }else if(sNickName.equals(s) || sNickName.equals(s2)){
                    AlertDialog.Builder alert = new AlertDialog.Builder(SignupActivity.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.setMessage("Nickname을 입력해 주세요");
                    alert.show();
                } else if(sPasswd.equals(s) || sPasswd.equals(s2)){
                    AlertDialog.Builder alert = new AlertDialog.Builder(SignupActivity.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.setMessage("Password를 입력해 주세요");
                    alert.show();
                }else if(sConfPasswd.equals(s) || sConfPasswd.equals(s2)){
                    AlertDialog.Builder alert = new AlertDialog.Builder(SignupActivity.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.setMessage("Password를 다시 한 번 입력해 주세요");
                    alert.show();
                }else{
                    if( Notiflag == 0){
                        AlertDialog.Builder alert = new AlertDialog.Builder(SignupActivity.this);
                        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alert.setMessage("알람 수신 여부를 체크해 주세요");
                        alert.show();
                    }else {
                        clickSign(v);
                    }
                }

            }
        });
    }

    // 회원가입 버튼 클릭 이벤트
    public void clickSign(View v) {
        // 비밀번호와 확인이 일치할 경우
        if(sPasswd.equals(sConfPasswd))
        {
            InsertData task = new InsertData();
            task.execute(sEmail, sPasswd, sNickName,sNotification,sMajor );

            this.finish();
        }
        // 비밀번호 불일치
        else
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(SignupActivity.this);
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.setMessage("Password가 일치 하지 않습니다");
            alert.show();
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

        /*
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
        }
        */
        // 회원가입 정보 보내기
        @Override
        protected String doInBackground(String... params) {

            String email = (String)params[0];
            String pw = (String)params[1];
            String nickname = (String)params[2];
            String notification = (String) params[3];
            String major = (String) params[4];

            String serverURL  = "http://52.41.114.24/enterview/signUp.php";
            String postParameters = "&email=" + email + "&pw=" + pw + "&nickname=" +nickname
                    + "&notification=" +notification + "&major=" + major ;

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

