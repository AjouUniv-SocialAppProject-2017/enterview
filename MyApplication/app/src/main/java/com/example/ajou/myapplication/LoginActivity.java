package com.example.ajou.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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

        if(permissionCamera == PackageManager.PERMISSION_DENIED || permissionReadStorage == PackageManager.PERMISSION_DENIED
                || permissionWriteStorage == PackageManager.PERMISSION_DENIED || permissionAudio == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE
                    ,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO}, REQUEST);
        } else {
            //Toast.makeText(getApplicationContext(),"camera permission authorized",Toast.LENGTH_SHORT).show();
            // resultText.setText("camera permission authorized");
        }

        loginBtn = (Button) findViewById(R.id.btn_login);
        signupBtn = (Button) findViewById(R.id.btn_signup);
        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText)findViewById(R.id.input_password);
        // login button 클릭 시 이벤트
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                email = input_email.getText().toString();
                password = input_password.getText().toString();
                if(email.equals(s) || email.equals(s2)){
                    AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.setMessage("E-mail을 입력해 주세요");
                    alert.show();
                }else if(password.equals(s) || password.equals(s2)){
                    AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.setMessage("Password를 입력해 주세요");
                    alert.show();
                }
/*
                //데이터 받아오기
                GetData task = new GetData();
                task.execute("http://52.41.114.24/enterview/login.php");
                */

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



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    if (permission.equals(Manifest.permission.CAMERA)) {
                        if(grantResult == PackageManager.PERMISSION_GRANTED) {
                            //resultText.setText("camera permission authorized");
                        } else {
                            //resultText.setText("camera permission denied");
                        }
                    }
                    if (permission.equals(Manifest.permission.RECORD_AUDIO)) {
                        if(grantResult == PackageManager.PERMISSION_GRANTED) {
                            //resultText.setText("recording audio permission authorized");
                        } else {
                            //resultText.setText("recording audio permission denied");
                        }
                    }
                    if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        if(grantResult == PackageManager.PERMISSION_GRANTED) {
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
    void login(){
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

        }

        @Override
        protected String doInBackground(String... strings) {

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
                mJsonString =data;
                String array ;
                array = mJsonString.substring(1,mJsonString.length());
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
                    }

                } catch (JSONException e) {
                    Log.e("나와", e.toString());
                }
                /* 서버에서 응답 */
                Log.e("RECV DATA", data);
                Log.e("RESULT", data);

                if (!(data.equals("0"))||Integer.parseInt(data)!=0) {

                    Log.e("RESULT", "성공적으로 처리되었습니다!"+data+"data");
                    //Toast.makeText(getApplicationContext(), "완료", Toast.LENGTH_SHORT).show();
                    userId = data;
                    String cookieTemp = conn.getHeaderField("Set-Cookie");

                    if(cookieTemp != null){
                        cookies = cookieTemp;
                       // conn.setRequestProperty("Cookie",cookieTemp);
                        Log.e("RESULT", "쿠키 :"+cookies);
                    }
                    /*
                    AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.setMessage("님 환영합니다");
                    alert.show();
                    */
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                    intent.putExtra("param_email",param_email);
                    intent.putExtra("param_nickname",param_nickname);
                    intent.putExtra("param_notification",param_notification);
                    intent.putExtra("param_major",param_major);
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
        private void showResult() {

        }
    }
/*
    public class GetData extends AsyncTask<String, Void, String> {

        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            mJsonString = result;
            showResult();
        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();
                return sb.toString().trim();


            } catch (Exception e) {
                errorString = e.toString();
                return null;
            }

        }
    }
    private void showResult() {
    try {

        JSONObject jsonObject = new JSONObject(mJsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("webnautes");

        ArrayList<Board_item> board_items = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject item = jsonArray.getJSONObject(i);
            param_email = item.getString("email");
            param_password = item.getString("password");
            param_nickname = item.getString("nickname");
            param_notification = item.getString("notification");
            param_major = item.getString("major");

        }

        //boardView.setLayoutManager(layoutManager_board);
        //Adapter_board = new Adapter_boardList(getActivity(), board_items, 1);
        // Adapter_proud.notifyDataSetChanged();
        //boardView.setAdapter(Adapter_board);

    } catch (JSONException e) {

    }
}
    */

}
