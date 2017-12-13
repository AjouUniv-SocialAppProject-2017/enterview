package com.example.ajou.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

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

/**
 * Created by thfad_000 on 2017-05-14.
 */
public class BoardDetailActivity extends AppCompatActivity {

    private RecyclerView boardReview;
    private  RecyclerView.Adapter Adapter_board_review;
    private RecyclerView.LayoutManager layoutManager_board_review;

    String mJsonString,qstSubject,qstContents,qstDate;

    public TextView date,title,desc,nick;
    public VideoView video;

    EditText reviewText;
    LoginActivity log ;
    String brdIdx,param_usrIdx;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_detail);


        Intent intent = getIntent();
        brdIdx = intent.getExtras().getString("brdIdx");
        param_usrIdx = intent.getExtras().getString("param_usrIdx");
        title = (TextView) findViewById(R.id.questionD_title);
        desc = (TextView) findViewById(R.id.questionD_desc);
        video = (VideoView) findViewById(R.id.questionD_image);
        reviewText = (EditText) findViewById(R.id.question_comment);
        log = new LoginActivity();


        GetReviewData getTask = new GetReviewData();
        getTask.execute(brdIdx);

 /*        GetData task = new GetData();
        task.execute(questionId);*/

        boardReview = (RecyclerView) findViewById(R.id.board_comment);
        boardReview.setHasFixedSize(true);
        layoutManager_board_review = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        BoardDetailActivity.GetData getData = new BoardDetailActivity.GetData();
        getData.execute();

    }
    public class GetData extends AsyncTask<String, Integer, String> {

        String errorString = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            showResult();
        }

        @Override
        protected String doInBackground(String... strings) {

            /* 인풋 파라메터값 생성 */
            String param = "brdIdx=" + brdIdx ;
            Log.e("POST", param);
            try {
                /* 서버연결 */
                URL url = new URL("http://52.41.114.24/enterview/BoardDetailActivity.php");
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
                Log.e("나와", mJsonString);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return mJsonString;
        }
        private void showResult() {
            try {

                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("webnautes");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    title.setText(item.getString("brdSubject"));
                    desc.setText(item.getString("brdContents"));
                    path = item.getString("brdUrl");
                    Uri uri = Uri.parse(path);
                    video.setVideoURI(uri);
                    video.seekTo(50);
                    video.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            Intent intent = new Intent(BoardDetailActivity.this,VideoPlay.class);
                            intent.putExtra("path",path);
                            startActivity(intent);
                            return false;
                        }
                    });
                    Log.e("나와", item.toString());
                }


            } catch (JSONException e) {
                Log.e("나와", e.toString());
            }
        }
    }

    //댓글 업로드
    public void uploadReview (View v){

        String desc = reviewText.getText().toString();
        String id = param_usrIdx;

        reviewText.setText(null);

        InsertData taskI = new InsertData();
        taskI.execute(brdIdx,id, desc);

        GetReviewData taskR = new GetReviewData();
        taskR.execute(brdIdx);

    }


    class InsertData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }


        @Override
        protected String doInBackground(String... params) {


            //$prdliId $prdcmUserId $prdcmContents

            String prdliId = (String) params[0];
            String prdcmUserId = (String) params[1];
            String prdcmContents = (String) params[2];

            String serverURL = "http://52.41.114.24/enterview/insertBrdReview.php";
            String postParameters = "brdliId=" + prdliId + "&brdcmUserId=" + prdcmUserId
                    + "&brdcmContents=" + prdcmContents;

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

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                Log.d("디비 성공", "디비성공함");
                return sb.toString();


            } catch (Exception e) {

                Log.d("디비에러", "디비에러났대");
                return new String("Error: " + e.getMessage());
            }

        }
    }

    //댓글 데이터 로드
    private class GetReviewData extends AsyncTask<String, Void, String> {

        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            mJsonString = result;
            showReviewResult();

        }

        @Override
        protected String doInBackground(String... params) {


            String brdliId = (String) params[0];

            String serverURL = "http://52.41.114.24/enterview/readBrdReview.php";
            String postParameters = "brdliId=" + brdliId ;

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);

                httpURLConnection.setConnectTimeout(5000);
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

    private void showReviewResult() {
        try {

            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("webnautes");

            ArrayList<BoardReview_item> boardReview_items = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String desc = item.getString("reviewDesc");
                String nick = item.getString("userNic");

                boardReview_items.add(new BoardReview_item(nick,desc));
            }

            boardReview.setLayoutManager(layoutManager_board_review);
            Adapter_board_review = new Adapter_board_review(this, boardReview_items, 1);
            boardReview.setAdapter(Adapter_board_review);

            Log.d("GetData", "Success");

        } catch (JSONException e) {
            Log.d("GetData", "Error");
        }

    }
}