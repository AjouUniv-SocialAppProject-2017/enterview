package com.example.ajou.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by thfad_000 on 2017-05-14.
 */
public class BoardDetailActivity extends AppCompatActivity {

    private RecyclerView QuestionReview;
    private  RecyclerView.Adapter Adapter_question_review;
    private RecyclerView.LayoutManager layoutManager_questionReview;

    String mJsonString,qstSubject,qstContents,qstDate;

    public TextView date,title,desc,nick;
    public ImageView image;

    public String questionId;
    EditText reviewText;
    LoginActivity log ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_detail);

        date = (TextView) findViewById(R.id.questionD_date);
        title = (TextView) findViewById(R.id.questionD_title);
        desc = (TextView) findViewById(R.id.questionD_desc);
        image = (ImageView) findViewById(R.id.questionD_image);

        reviewText = (EditText) findViewById(R.id.question_comment);

        log = new LoginActivity();

        Intent intent = this.getIntent();
        questionId = intent.getStringExtra("itemId");

/*        GetReviewData taskR = new GetReviewData();
        taskR.execute(questionId);

        GetData task = new GetData();
        task.execute(questionId);*/

        QuestionReview = (RecyclerView) findViewById(R.id.board_comment);
        QuestionReview.setHasFixedSize(true);

        showReviewResult();

        layoutManager_questionReview = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

    }

    public void proudWrite_cancel(View v){

        finish();
    }

    public void uploadReview (View v){

        /*String desc = reviewText.getText().toString();
        String id = log.userId;

        reviewText.setText(null);

        InsertReviewData taskI = new InsertReviewData();
        taskI.execute(questionId,id, desc);

        GetReviewData taskR = new GetReviewData();
        taskR.execute(questionId);*/

    }

   /* private class GetData extends AsyncTask<String, Void, String> {

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

            String qstliId = (String) params[0];
            String serverURL = "http://52.41.114.24/questionDetail.php";
            String postParameters = "qstliId=" + qstliId ;

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
    }*/

    /*private void showResult() {
        try {

            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("webnautes");

            JSONObject item = jsonArray.getJSONObject(0);
            Log.d("이것봐",item.getString("qstDate"));
            String qstId = item.getString("qstId");
             qstSubject = item.getString("qstSubject");
             qstContents = item.getString("qstContents");
            String qstPicture = item.getString("qstPicture");
            String qstNic = item.getString("qstNic");
             qstDate = item.getString("qstDate");


        } catch (JSONException e) {
        }
        date.setText(qstDate);
        title.setText(qstSubject);
        desc.setText(qstContents);

    }*/

    /*class InsertReviewData extends AsyncTask<String, Void, String> {

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

            String qstliId = (String) params[0];
            String qstcmUserId = (String) params[1];
            String qstcmContents = (String) params[2];

            String serverURL = "http://52.41.114.24/questionReview.php";
            String postParameters = "qstliId=" + qstliId + "&qstcmUserId=" + qstcmUserId
                    + "&qstcmContents=" + qstcmContents;

            Log.d("이거임 This", "" + postParameters);

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
                return sb.toString();


            } catch (Exception e) {
                return new String("Error: " + e.getMessage());
            }

        }
    }

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

            String qstliId = (String) params[0];

            String serverURL = "http://52.41.114.24/readQuReview.php";
            String postParameters = "qstliId=" + qstliId ;


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
    }*/

    private void showReviewResult() {
        //try {

            /*JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("webnautes");*/

            ArrayList<BoardReview_item> BoardReview_items = new ArrayList<>();


            for (int i = 0; i < 10; i++) {

                /*JSONObject item = jsonArray.getJSONObject(i);

                String nick = item.getString("userNic");
                String desc = item.getString("quReviewDesc");*/

                BoardReview_items.add(new BoardReview_item("닉네임", "내용"));
            }

            QuestionReview.setLayoutManager(layoutManager_questionReview);
            Adapter_question_review = new Adapter_board_review(this, BoardReview_items, 1);
            QuestionReview.setAdapter(Adapter_question_review);
/*

        } catch (JSONException e) {
        }
*/

    }
}