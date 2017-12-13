package com.example.ajou.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class BoardSearchActivity extends AppCompatActivity {

    private RecyclerView frqView;
    private RecyclerView.Adapter Adapter_board;
    private RecyclerView.LayoutManager layoutManager_board;

    String param_usrIdx, param_major;

    String mJsonString;

    public static String[] listBoardId = new String[100];
    RecyclerView boardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_search);

        Intent intent = getIntent();
        param_usrIdx = intent.getExtras().getString("param_usrIdx");
        param_major = intent.getExtras().getString("param_major");
    }


    public void srchBtnClick(View v){
        LinearLayout frqLayout = (LinearLayout)findViewById(R.id.frqLayout);
        frqLayout.setVisibility(View.INVISIBLE);

        boardView = (RecyclerView)findViewById(R.id.frq_board_list);
        boardView.setHasFixedSize(true);

        //검색어
        TextView searchKey = (TextView)findViewById(R.id.searchKey);
        String searchKey_txt = searchKey.getText().toString();
        //한글 검색어는 URL 인코딩을 해주어야한다
        searchKey_txt = URLEncoder.encode(searchKey_txt);

        //같은직무 체크박스
        CheckBox checkBox = (CheckBox)findViewById(R.id.checkBox);
        String sameJobIsChecked = (checkBox.isChecked()) ? param_major : "";
        sameJobIsChecked = URLEncoder.encode(sameJobIsChecked);

        //검색 결과 받아오기
        GetSearchData task = new GetSearchData();
        task.execute("http://52.41.114.24/enterview/boardList.php?searchKey="+searchKey_txt+"&sameJob="+sameJobIsChecked+"&usrIdx="+param_usrIdx);

        layoutManager_board = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

    }

    public class GetSearchData extends AsyncTask<String, Void, String> {

        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            mJsonString = result;
            showSearchResult();
        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(5000);
                Log.d("젭알",""+httpURLConnection.getURL());
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
                Log.e("검색잘하자",e.getMessage());
                errorString = e.toString();
                return null;
            }

        }
    }

    private void showSearchResult() {
        try {

            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("webnautes");

            ArrayList<Board_item> board_items = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String brdIdx = item.getString("brdIdx");
                listBoardId[i] = brdIdx;
                Log.d("여기 리스트 아이디",""+listBoardId[i]);
                String brdContents = item.getString("brdContents");
                String brdSubject = item.getString("brdSubject");
                String brdDate = item.getString("brdDate");
                String brdNickname = item.getString("brdNickname");
                String brdUrl = item.getString("brdUrl");
                String brdRating = item.getString("brdRating");
                if(brdRating.equals("")||brdRating.equals("null")){
                    brdRating="별점주기";
                }
                String brdUserId = item.getString("brdUserId");


                board_items.add(new Board_item(brdRating, brdNickname, brdSubject, brdDate, brdContents,
                        "댓글",brdUrl,brdUserId));
            }


            //수정필요 : 검색결과 없을 때
            if(jsonArray.length()<=0){
                Log.d("검색결과없을때","여기로");
                LinearLayout searchBoard = (LinearLayout)findViewById(R.id.searchBoard);
                searchBoard.removeAllViews();
                TextView noSearchResult = new TextView(BoardSearchActivity.this);
                noSearchResult.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                noSearchResult.setPadding(50, 100, 10, 10);
                noSearchResult.setTextColor(getResources().getColor(R.color.colorAccent));
                noSearchResult.setTextSize(13);
                //noSearchResult.setTextAlignment();
                noSearchResult.setText("검색결과가 없습니다.");
                searchBoard.addView(noSearchResult);
            }

            boardView.setLayoutManager(layoutManager_board);
            Adapter_board = new Adapter_boardList(this, board_items, 1,param_usrIdx);
            // Adapter_proud.notifyDataSetChanged();
            boardView.setAdapter(Adapter_board);

        } catch (JSONException e) {

        }

    }
}
