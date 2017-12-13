package com.example.ajou.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by ajou on 2017-12-12.
 */

public class VideoPlay extends AppCompatActivity {
    VideoView videoView;
    String path;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoplay);
        videoView =  (VideoView) findViewById(R.id.videoplayview);
        Intent intent = getIntent();
        path = intent.getExtras().getString("path");

        Uri uri = Uri.parse(path);
        videoView.setVideoURI(uri);
        final MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.start();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
