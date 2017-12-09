package com.example.ajou.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BoardSearchActivity extends AppCompatActivity {

    private RecyclerView frqView;
    private RecyclerView.Adapter Adapter_board;
    BoardSearch_frequestion_item frq_item;
    private RecyclerView.LayoutManager layoutManager_board;

    String mJsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_search);

        frq_item = new BoardSearch_frequestion_item();

        frqView = (RecyclerView)findViewById(R.id.fsq);
        frqView.setHasFixedSize(true);

        layoutManager_board = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        //데이터 받아오기
        GetData task = new GetData();
        task.execute("http://52.41.114.24/enterview/boardList.php");
    }

    public class GetData extends AsyncTask<String, Void, String> {

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

            String serverURL = params[0];

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

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

    private void showResult() {
        try {

            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("webnautes");

            ArrayList<BoardSearch_frequestion_item> frq_items = new ArrayList<>();

            /*for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String frqContents = item.getString("frqContents");
                frq_items.add(new BoardSearch_frequestion_item("내용"));
            }*/

            for (int i = 0; i <5; i++) {
                frq_items.add(new BoardSearch_frequestion_item("내용 "+i));
            }

            frqView.setLayoutManager(layoutManager_board);
            Adapter_board = new Adapter_frqList(this, frq_items, 1);
            // Adapter_proud.notifyDataSetChanged();
            frqView.setAdapter(Adapter_board);

        } catch (JSONException e) {
        }

    }
}
