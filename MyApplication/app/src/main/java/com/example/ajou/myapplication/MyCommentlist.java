package com.example.ajou.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

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
 * Created by ajou on 2017-12-13.
 */

public class MyCommentlist extends AppCompatActivity {
    String param_usrIdx;

    private RecyclerView mycommentView;
    private RecyclerView.Adapter Adapter_mycommentList;
    private RecyclerView.LayoutManager layoutManager_mycomment;
    String[] commentlist = new String[100];
    ArrayList<String> Mycommentlist_item = new ArrayList<>();
    static String mJsonString;
    int count = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycommentlist);
        Intent intent = getIntent();
        param_usrIdx = intent.getExtras().getString("param_usrIdx");

        mycommentView = (RecyclerView) findViewById(R.id.mycomment_list);
        mycommentView.setHasFixedSize(true);
        layoutManager_mycomment = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        MyCommentlist.GetData getData = new MyCommentlist.GetData();
        getData.execute();

        final GestureDetector gestureDetector = new GestureDetector(MyCommentlist.this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

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
            String param = "cmntUserId=" + param_usrIdx ;
            count = 0;
            Log.e("POST", param);
            try {
                /* 서버연결 */
                URL url = new URL("http://52.41.114.24/enterview/mycommentlist.php");
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
                    commentlist[i] = item.getString("cmntDate")+item.getString("cmntContents");
                    count++;
                    Log.e("나와", commentlist[i]);
                    Mycommentlist_item.add(commentlist[i]);
                }

                mycommentView.setLayoutManager(layoutManager_mycomment);
                Adapter_mycommentList = new Adapter_mycommentList(getApplicationContext(), Mycommentlist_item, 1);
                mycommentView.setAdapter(Adapter_mycommentList);

            } catch (JSONException e) {
                Log.e("나와", e.toString());
            }
        }
    }
}
