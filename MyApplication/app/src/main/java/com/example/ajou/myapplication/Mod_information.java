package com.example.ajou.myapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

/**
 * Created by ajou on 2017-12-12.
 */

public class Mod_information extends AppCompatActivity {
    Button modBtn;
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
    int Notiflag;

    String param_email;
    String param_nickname;
    String param_notification;
    String param_major;
    String param_usrIdx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mod_information);

        Intent intent = getIntent();
        param_email = intent.getExtras().getString("param_email");
        param_nickname = intent.getExtras().getString("param_nickname");
        param_notification = intent.getExtras().getString("param_notification");
        param_major = intent.getExtras().getString("param_major");
        param_usrIdx = intent.getExtras().getString("param_usrIdx");

        password = (EditText) findViewById(R.id.mod_input_password);
        passwordCheck = (EditText) findViewById(R.id.mod_input_password_check);
        modBtn = (Button) findViewById(R.id.btn_modComplete);
        Notigroup = (RadioGroup) findViewById(R.id.mod_Notigroup);
        majorSpinner = (Spinner)findViewById(R.id.mod_major_select);
        email = (EditText) findViewById(R.id.mod_input_email);
        email.setText(param_email);
        nickname = (EditText) findViewById(R.id.mod_input_nickname);
        nickname.setText(param_nickname);
        // major spinner 제어
        final ArrayAdapter majorAdapter = ArrayAdapter.createFromResource(this,
                R.array.mod_major_select, android.R.layout.simple_spinner_item);
        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        majorSpinner.setAdapter(majorAdapter);
        //초기화
        if(param_major.equals("사무")){
            majorSpinner.setSelection(0);
        }else if(param_major.equals("마케팅")){
            majorSpinner.setSelection(1);
        }else if(param_major.equals("서비스")){
            majorSpinner.setSelection(2);
        }else if(param_major.equals("IT")){
            majorSpinner.setSelection(3);
        }else if(param_major.equals("컨설팅")){
            majorSpinner.setSelection(4);
        }else if(param_major.equals("전기")){
            majorSpinner.setSelection(5);
        }else if(param_major.equals("기계")){
            majorSpinner.setSelection(6);
        }else if(param_major.equals("보안")){
            majorSpinner.setSelection(7);
        }else if(param_major.equals("제조업")){
            majorSpinner.setSelection(8);
        }else if(param_major.equals("건설업")){
            majorSpinner.setSelection(9);
        }else if(param_major.equals("생산제조")){
            majorSpinner.setSelection(10);
        }

    // 선택된 major 색 변경
        majorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(param_notification.equals("수신")) {
            Notigroup.check(R.id.mod_acceptNoti);
            Notiflag = 1;
        }else if(param_notification.equals("비수신")){
            Notigroup.check(R.id.mod_denyNoti);
            Notiflag = 2;
        }

        Notigroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.mod_acceptNoti){
                    Notiflag = 1;
                    sNotification = "수신";
                    //Toast.makeText(getApplicationContext(),Notiflag,Toast.LENGTH_SHORT).show();
                }else if(checkedId == R.id.mod_denyNoti){
                    Notiflag = 2;
                    sNotification = "비수신";
                   // Toast.makeText(getApplicationContext(),Notiflag,Toast.LENGTH_SHORT).show();
                }else{
                    Notiflag = 0;
                }
            }
        });
        modBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sEmail = email.getText().toString();
                sPasswd = password.getText().toString();
                sConfPasswd = passwordCheck.getText().toString();
                sNickName = nickname.getText().toString();
                sMajor = majorSpinner.getSelectedItem().toString();
                if(sEmail.equals(s) || sEmail.equals(s2)){
                    AlertDialog.Builder alert = new AlertDialog.Builder(Mod_information.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.setMessage("E-mail을 입력해 주세요");
                    alert.show();
                }else if(sNickName.equals(s) || sNickName.equals(s2)){
                    AlertDialog.Builder alert = new AlertDialog.Builder(Mod_information.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.setMessage("Nickname을 입력해 주세요");
                    alert.show();
                } else if(sPasswd.equals(s) || sPasswd.equals(s2)){
                    AlertDialog.Builder alert = new AlertDialog.Builder(Mod_information.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.setMessage("Password를 입력해 주세요");
                    alert.show();
                }else if(sConfPasswd.equals(s) || sConfPasswd.equals(s2)){
                    AlertDialog.Builder alert = new AlertDialog.Builder(Mod_information.this);
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
                        AlertDialog.Builder alert = new AlertDialog.Builder(Mod_information.this);
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
            Mod_information.InsertData task = new Mod_information.InsertData();
            task.execute(param_usrIdx,sEmail, sPasswd, sNickName,sNotification,sMajor );

            this.finish();
        }
        // 비밀번호 불일치
        else
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(Mod_information.this);
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

            progressDialog = ProgressDialog.show(Mod_information.this,
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

            String usrIdx = (String)params[0];
            String email = (String)params[1];
            String pw = (String)params[2];
            String nickname = (String)params[3];
            String notification = (String) params[4];
            String major = (String) params[5];

            String serverURL  = "http://52.41.114.24/enterview/mod_information.php";
            String postParameters = "&usrIdx="+usrIdx+"&email=" + email + "&pw=" + pw + "&nickname=" +nickname
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
                Log.d("확인", sb.toString());
                return sb.toString();


            } catch (Exception e) {

                Log.d("안된다고", "InsertData: Error ", e);
                return new String("Error: " + e.getMessage());
            }

        }
    }
}
