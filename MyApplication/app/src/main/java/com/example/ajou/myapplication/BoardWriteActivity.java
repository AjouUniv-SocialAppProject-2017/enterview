package com.example.ajou.myapplication;

import android.app.Activity;
import android.app.Dialog;
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
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
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


    private static final String TAG = MainActivity.class.getSimpleName();

    TextView sub;
    EditText desc;
    VideoView videoView;

    LoginActivity log;

    String s_desc,s_sub,s_user;
    String param_usrIdx ;
    String path;
    String videoUrl= "http://52.41.114.24/enterview/VideoUpload/uploads/";
    String filename;
    String param_nickname;
    String param_email;
    String param_notification;
    String param_major;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);

        log = new LoginActivity();

        Intent intent = getIntent();
        final String question = intent.getExtras().getString("question");
        param_usrIdx = intent.getExtras().getString("param_usrIdx");
        param_major = intent.getExtras().getString("param_major");
        param_nickname = intent.getExtras().getString("param_nickname");
        param_notification = intent.getExtras().getString("param_notification");
        param_email = intent.getExtras().getString("param_email");

        path = intent.getExtras().getString("path");
        filename = path.substring(8,path.length());
        videoUrl += filename;

        sub = (TextView) findViewById(R.id.sub);
        sub.setText(question);
        desc = (EditText) findViewById(R.id.desc);
        videoView = (VideoView) findViewById(R.id.videoView);

        videoView.setVideoURI(Uri.parse(path));

        final MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);

    }


    // 업로드 버튼
    public void questionWrite_upload(View v){
        ShowDialog("글을 등록하시겠습니까",0);
    }

    // 뒤로가기 버튼
    public void write_cancel(View v){
        this.finish();
    }

    class InsertData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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
            String brdUrl = (String) params[3];

            String serverURL = "http://52.41.114.24/enterview/boardWrite.php";

            //현재 시간 구하기
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String str = sdf.format(date);

            String postParameters = "brdSubject=" + brdSubject + "&brdContents=" + brdContents +
                    "&brdUserId=" + brdUserId + "&brdUrl=" + brdUrl + "&brdDate=" + str;

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

    private void uploadVideo() {
        class UploadVideo extends AsyncTask<Void, Void, String> {

            ProgressDialog uploading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                uploading = ProgressDialog.show(BoardWriteActivity.this, "Uploading File", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                uploading.dismiss();

            }

            @Override
            protected String doInBackground(Void... params) {
                Upload u = new Upload();

                String msg = u.uploadVideo(path);
                return msg;
            }
        }
        UploadVideo uv = new UploadVideo();
        uv.execute();
    }

    private void ShowDialog(String contents, int type) {
        // two button
        if (type == 0) {
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
                    s_sub = sub.getText().toString();
                    s_desc = desc.getText().toString();
                    s_user=param_usrIdx;

                    InsertData task = new InsertData();
                    task.execute(s_sub,s_desc,s_user,videoUrl);

                    Log.d("이걸봐", "" + s_desc+" "+s_user);
                    Toast.makeText(getApplicationContext(),"글이 등록되었습니다",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BoardWriteActivity.this, MainActivity.class);
                    intent.putExtra("param_usrIdx",param_usrIdx);
                    intent.putExtra("param_major",param_major);
                    intent.putExtra("param_nickname",param_nickname);
                    intent.putExtra("param_notification",param_notification);
                    intent.putExtra("param_email",param_email);
                    startActivity(intent);
                    uploadVideo();
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
