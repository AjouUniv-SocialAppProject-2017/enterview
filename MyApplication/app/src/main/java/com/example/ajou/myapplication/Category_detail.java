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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

/**
 * Created by ajou on 2017-11-14.
 */

public class Category_detail extends AppCompatActivity {

    private RecyclerView questionView;
    private RecyclerView.Adapter Adapter_questionList;
    private RecyclerView.LayoutManager layoutManager_question;
    TextView selectedQuestion;
    String select;
    String finalquestion;
    int qstnCategory;
    static String mJsonString;
    String[] questionlist = new String[100];
    int count = 0;
    int flag = 0;
    ArrayList<String> question_item = new ArrayList<>();


    Button record_start;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_detail);
        flag = 0;
        Intent intent = getIntent();
        qstnCategory = intent.getExtras().getInt("qstnCategory");
        final String param_usrIdx = intent.getExtras().getString("param_usrIdx");
        final String param_major = intent.getExtras().getString("param_nickname");
        final String param_notification = intent.getExtras().getString("param_notification");
        final String param_email = intent.getExtras().getString("param_email");
        final String param_nickname = intent.getExtras().getString("param_nickname");
        selectedQuestion = (TextView) findViewById(R.id.selected_question);

        record_start = (Button) findViewById(R.id.record_start);
        record_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( flag == 0){
                    Toast.makeText(getApplicationContext(),"질문을 선택해주세요",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent  = new Intent(getApplicationContext(),RecordActivity.class);
                    intent.putExtra("finalquestion",finalquestion);
                    intent.putExtra("param_usrIdx",param_usrIdx);
                    intent.putExtra("param_major",param_major);
                    intent.putExtra("param_nickname",param_nickname);
                    intent.putExtra("param_notification",param_notification);
                    intent.putExtra("param_email",param_email);
                    startActivity(intent);
                    finish();
                }
               // Toast.makeText(getApplicationContext(),category_num+"번쨰 리스트",Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(getApplicationContext(), BoardWriteActivity.class);


            }
        });

        questionView = (RecyclerView) findViewById(R.id.question_list);
        questionView.setHasFixedSize(true);
        layoutManager_question = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        GetData getData = new GetData();
        getData.execute();

        final GestureDetector gestureDetector = new GestureDetector(Category_detail.this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        questionView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    TextView tv = (TextView) rv.getChildViewHolder(child).itemView.findViewById(R.id.question);
                   // Toast.makeText(getApplicationContext(), tv.getText().toString(), Toast.LENGTH_SHORT).show();
                    flag = 1;
                    selectedQuestion.setText(tv.getText().toString());
                    finalquestion = tv.getText().toString();
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

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
            String param = "qstnCategory=" + qstnCategory ;
            count = 0;
            Log.e("POST", param);
            try {
                /* 서버연결 */
                URL url = new URL("http://52.41.114.24/enterview/questionlist.php");
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
                    questionlist[i] = item.getString("qstnContents");
                    count++;
                    Log.e("나와", questionlist[i]);
                    question_item.add(questionlist[i]);
                }

                questionView.setLayoutManager(layoutManager_question);
                Adapter_questionList = new Adapter_questionList(getApplicationContext(), question_item, 1);
                questionView.setAdapter(Adapter_questionList);

            } catch (JSONException e) {
                Log.e("나와", e.toString());
            }
        }
    }


}
