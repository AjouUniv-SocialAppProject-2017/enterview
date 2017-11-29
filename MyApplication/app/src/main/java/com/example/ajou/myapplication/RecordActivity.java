package com.example.ajou.myapplication;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by ajou on 2017-11-28.
 */

public class RecordActivity extends AppCompatActivity {
    SurfaceView surfaceView;
    SurfaceHolder holder;
    MediaRecorder recorder = null;
    Camera camera;

    String path = "/sdcard/recorded_video.mp4";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_video);
        surfaceView  = (SurfaceView) findViewById(R.id.surfaceView);
        Button button = (Button) findViewById(R.id.button);
        Button button2 = (Button) findViewById(R.id.button2);
        holder = surfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recorder != null){
                    Toast.makeText(getApplicationContext(),"ssssss",Toast.LENGTH_LONG).show();
                    recorder.stop();
                    recorder.release();
                    recorder= null;
                    camera.stopPreview();
                    camera.release();
                    camera=null;
                }
            }
        });
    }

}
