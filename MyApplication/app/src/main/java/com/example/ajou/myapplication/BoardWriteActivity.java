package com.example.ajou.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by thfad_000 on 2017-05-08.
 */
public class BoardWriteActivity extends AppCompatActivity {

    TextView sub;
    EditText desc;
    private LoginActivity log;

    String s_desc,s_sub,s_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);

        log = new LoginActivity();

        sub = (TextView) findViewById(R.id.sub);
        desc = (EditText) findViewById(R.id.desc);

    }

    // 업로드 버튼
    public void questionWrite_upload(View v){

        s_sub = sub.getText().toString();
        s_desc = desc.getText().toString();

        //수정필요
        //s_user = log.userId;
        s_user="1";

        InsertData task = new InsertData();
        task.execute(s_sub,s_desc,s_user);

        Log.d("이걸봐", "" + s_desc+" "+s_user);

        this.finish();
    }


    // 뒤로가기 버튼
    public void write_cancel(View v){
        this.finish();
    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(BoardWriteActivity.this,
                    "Please Wait", null, true, true);
        }


/*        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
        }*/


        @Override
        protected String doInBackground(String... params) {


            String brdSubject = (String) params[0];
            String brdContents = (String) params[1];
            String brdUserId = (String) params[2];

            String serverURL = "http://52.41.114.24/enterview/boardWrite.php";
            String postParameters = "brdSubject=" + brdSubject + "&brdContents=" + brdContents + "&brdUserId=" + brdUserId;

            Log.d("이거봐", "" + postParameters);

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
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String data = "";
                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                data = sb.toString().trim();

                bufferedReader.close();

                Log.d("디비 성공", "디비성공함"+data);
                return sb.toString();


            } catch (Exception e) {

                Log.d("디비에러", "디비에러났대");
                return new String("Error: " + e.getMessage());
            }

        }
    }

}
