package com.example.ajou.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ajou on 2017-11-28.
 */

public class RecordActivity extends AppCompatActivity {
    SurfaceView surfaceView;
    SurfaceHolder holder;
    MediaRecorder recorder = null;
    Camera camera;
    String path = "/sdcard/";
    TextView finalquestion;
    long mNow;
    Date mDate;
    int flag = 0;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMddhhmmss");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_video);
        surfaceView  = (SurfaceView) findViewById(R.id.surfaceView);
        Intent intent = getIntent();
        final String question = intent.getExtras().getString("finalquestion");
        final String param_usrIdx = intent.getExtras().getString("param_usrIdx");
        final String param_major = intent.getExtras().getString("param_major");

        final String param_nickname = intent.getExtras().getString("param_nickname");
        final String param_notification = intent.getExtras().getString("param_notification");
        final String param_email = intent.getExtras().getString("param_email");

        path +=param_usrIdx+"_"+getTime()+".mp4";
        ImageButton button = (ImageButton) findViewById(R.id.button);
        //Button button2 = (Button) findViewById(R.id.button2);
        holder = surfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        finalquestion = (TextView) findViewById(R.id.finalquestion);
        finalquestion.setText(question);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag == 0){
                    recorder = new MediaRecorder();
                    camera = Camera.open(1);
                    camera.setDisplayOrientation(90);
                    camera.unlock();

                    recorder.setCamera(camera);
                    try{

                        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                        recorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
                        recorder.setOutputFile(path);


                        recorder.setPreviewDisplay(holder.getSurface());
                        recorder.prepare();
                        recorder.start();

                    }catch (IOException e){
                        e.printStackTrace();

                    }
                    flag = 1;
                }else if(flag == 1){
                    if(recorder != null){
                        //Toast.makeText(getApplicationContext(),"ssssss",Toast.LENGTH_LONG).show();
                        recorder.stop();
                        recorder.release();
                        recorder= null;
                        camera.stopPreview();
                        camera.release();
                        camera=null;

                        Intent intent = new Intent(RecordActivity.this, BoardWriteActivity.class);
                        intent.putExtra("question",question);
                        intent.putExtra("path",path);
                        intent.putExtra("param_usrIdx",param_usrIdx);
                        intent.putExtra("param_major",param_major);
                        intent.putExtra("param_nickname",param_nickname);
                        intent.putExtra("param_notification",param_notification);
                        intent.putExtra("param_email",param_email);
                        startActivity(intent);
                        flag = 0;
                        finish();

                    }
                }

            }
        });
        /*
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recorder != null){
                    //Toast.makeText(getApplicationContext(),"ssssss",Toast.LENGTH_LONG).show();
                    recorder.stop();
                    recorder.release();
                    recorder= null;
                    camera.stopPreview();
                    camera.release();
                    camera=null;

                    Intent intent = new Intent(RecordActivity.this, BoardWriteActivity.class);
                    intent.putExtra("question",question);
                    intent.putExtra("param_usrIdx",param_usrIdx);
                    intent.putExtra("path",path);
                    startActivity(intent);
                }
            }
        });
        */
    }
    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

}
