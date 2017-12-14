package com.example.ajou.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Activity mainActivity = this;

    private static final int REQUEST_MICROPHONE = 3;
    private static final int REQUEST_EXTERNAL_STORAGE = 2;
    private static final int REQUEST = 1;
    static String mJsonString;
    public static String[] listId = new String[100];

    Button loginBtn;
    Button signupBtn;
    EditText input_email;
    EditText input_password;
    String email, password;
    String cookies;
    String s = null;
    String s2 = "";

    String param_email;
    String param_password;
    String param_nickname;
    String param_notification;
    String param_major;
    String param_usrIdx;

    public static String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        // permission check
        int permissionCamera = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        int permissionReadStorage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWriteStorage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionAudio = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);

        if (permissionCamera == PackageManager.PERMISSION_DENIED || permissionReadStorage == PackageManager.PERMISSION_DENIED
                || permissionWriteStorage == PackageManager.PERMISSION_DENIED || permissionAudio == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, REQUEST);
        } else {
            //Toast.makeText(getApplicationContext(),"camera permission authorized",Toast.LENGTH_SHORT).show();
            // resultText.setText("camera permission authorized");
        }

        loginBtn = (Button) findViewById(R.id.btn_login);
        signupBtn = (Button) findViewById(R.id.btn_signup);
        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText) findViewById(R.id.input_password);
        // login button 클릭 시 이벤트
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                email = input_email.getText().toString();
                password = input_password.getText().toString();
                if (email.equals(s) || email.equals(s2)) {
                   ShowDialog("Email을 입력해주세요",1);
                } else if (password.equals(s) || password.equals(s2)) {
                    ShowDialog("Password를 입력해 주세요",1);
                }else{
                    login();
                }


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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        ShowDialog("앱을 종료하시겠습니까?",0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    if (permission.equals(Manifest.permission.CAMERA)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                            //resultText.setText("camera permission authorized");
                        } else {
                            //resultText.setText("camera permission denied");
                        }
                    }
                    if (permission.equals(Manifest.permission.RECORD_AUDIO)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                            //resultText.setText("recording audio permission authorized");
                        } else {
                            //resultText.setText("recording audio permission denied");
                        }
                    }
                    if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                            //resultText.setText("read/write storage permission authorized");
                        } else {
                            //resultText.setText("read/write storage permission denied");
                        }
                    }
                    if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                            //resultText.setText("read/write storage permission authorized");
                        } else {
                            //resultText.setText("read/write storage permission denied");
                        }
                    }
                }
                break;

        }
    }

    //171113 서버test
    void login() {
        // 로그인 구현
        try {

            loginDB lDB = new loginDB();
            lDB.execute();

        } catch (NullPointerException e) {
            Log.e("err", e.getMessage());
        }
    }

    public class loginDB extends AsyncTask<String, Integer, String> {

        String errorString = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result.equals("success")){
                input_email.setText("");
                input_password.setText("");

            }else{
                ShowDialog("아이디 또는 비밀번호가 일치하지 않습니다",1);
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            /* 인풋 파라메터값 생성 */
            String param = "email=" + email + "&password=" + password;

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
                mJsonString = data;
                String array;
                array = mJsonString.substring(1, mJsonString.length());
                Log.e("나와", array);

                try {

                    JSONObject jsonObject = new JSONObject(array);
                    JSONArray jsonArray = jsonObject.getJSONArray("webnautes");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        param_email = item.getString("email");
                        param_password = item.getString("password");
                        param_nickname = item.getString("nickname");
                        param_notification = item.getString("notification");
                        param_major = item.getString("major");
                        param_usrIdx = item.getString("usrIdx");
                    }

                } catch (JSONException e) {
                    Log.e("나와", e.toString());
                }
                /* 서버에서 응답 */
                Log.e("RECV DATA", data);
                Log.e("RESULT", data);
                String result = data.substring(0, 1);
                Log.e("RESULT", result);
                if (!(result.equals("0")) || Integer.parseInt(result) != 0) {

                    Log.e("RESULT", "성공적으로 처리되었습니다!" + data + "data");
                    //Toast.makeText(getApplicationContext(), "완료", Toast.LENGTH_SHORT).show();
                    userId = data;
                    String cookieTemp = conn.getHeaderField("Set-Cookie");

                    if (cookieTemp != null) {
                        cookies = cookieTemp;
                        // conn.setRequestProperty("Cookie",cookieTemp);
                        Log.e("RESULT", "쿠키 :" + cookies);
                    }

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                    intent.putExtra("param_email", param_email);
                    intent.putExtra("param_nickname", param_nickname);
                    intent.putExtra("param_notification", param_notification);
                    intent.putExtra("param_major", param_major);
                    intent.putExtra("param_usrIdx", param_usrIdx);

                    startActivity(intent);

                    return "success";

                } else { // 로그인 실패 , 다시 확인하라고 알림창 띄우기
                    Log.e("RESULT", "에러 발생! ERRCODE = " + data);
                    //ShowDialog("아이디 또는 비밀번호가 일치하지 않습니다",1);
                    return "fail";
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        private void showResult() {

        }
    }

    private void ShowDialog(final String contents, int type) {
        // two button
        if (type == 0) {
            final String content = contents;
            LayoutInflater dialog = LayoutInflater.from(this);
            final View dialogLayout = dialog.inflate(R.layout.dialog, null);
            final Dialog myDialog = new Dialog(this);

            myDialog.setContentView(dialogLayout);
            myDialog.show();

            Button btn_ok = (Button) dialogLayout.findViewById(R.id.btn_ok);
            Button btn_cancel = (Button) dialogLayout.findViewById(R.id.btn_cancel);
            TextView contentsView = (TextView) dialogLayout.findViewById(R.id.dialog_contents);
            contentsView.setText(contents);

            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.cancel();
                }
            });
        }else if(type == 1){
            final LayoutInflater dialog = LayoutInflater.from(this);
            final View dialogLayout = dialog.inflate(R.layout.dialog2, null);
            final Dialog myDialog = new Dialog(this);

            myDialog.setContentView(dialogLayout);
            myDialog.show();

            Button btn_ok = (Button) dialogLayout.findViewById(R.id.btn_one_ok);
            Button btn_cancel = (Button) dialogLayout.findViewById(R.id.btn_cancel);
            TextView contentsView = (TextView) dialogLayout.findViewById(R.id.dialog_contents);
            contentsView.setText(contents);

            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                }
            });
        }
    }
}