package com.example.ajou.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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
    VideoView videoView;
    private LoginActivity log;
    final String path = "/sdcard/recorded_video.mp4";


    String s_desc,s_sub,s_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);

        log = new LoginActivity();

        Intent intent = getIntent();
        final String question = intent.getExtras().getString("question");

        sub = (TextView) findViewById(R.id.sub);
        sub.setText(question);
        desc = (EditText) findViewById(R.id.desc);
        videoView = (VideoView) findViewById(R.id.videoView);

        //path 수정필요!
        String path = "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_2mb.mp4";
        videoView.setVideoURI(Uri.parse(path));

        final MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);

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
        Toast.makeText(getApplicationContext(),"글이 등록되었습니다",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(BoardWriteActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
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
