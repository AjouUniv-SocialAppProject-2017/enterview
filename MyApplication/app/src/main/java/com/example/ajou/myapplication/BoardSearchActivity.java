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
    BoardSearch_frequestion_item frq_item;
    private RecyclerView.LayoutManager layoutManager_board;

    String mJsonString;

    public static String[] listBoardId = new String[100];
    RecyclerView boardView;

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

            //임시로 질문 내용0,내용1,내용2...
            for (int i = 0; i <5; i++) {
                frq_items.add(new BoardSearch_frequestion_item("내용 "+i));
            }

            frqView.setLayoutManager(layoutManager_board);
            Adapter_board = new Adapter_frqList(this, frq_items, 1);
            // Adapter_proud.notifyDataSetChanged();
            frqView.setAdapter(Adapter_board);
            final GestureDetector gestureDetector = new GestureDetector(BoardSearchActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });

            //자주 검색하는 질문 항목 클릭시에
            frqView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                @Override
                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                    View child = rv.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && gestureDetector.onTouchEvent(e)) {
                        Intent intent = new Intent(frqView.getContext(), BoardDetailActivity.class);
                        frqView.getContext().startActivity(intent);
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
        } catch (JSONException e) {
        }

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
        int sameJobIsChecked = (checkBox.isChecked()) ? 1 : 0;

        GetSearchData task = new GetSearchData();
        task.execute("http://52.41.114.24/enterview/boardList.php?searchKey="+searchKey_txt+"&sameJob="+sameJobIsChecked);

        layoutManager_board = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        Log.d("srchBtnClick","srchBtnClick");
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

                //Log.d("이미지소스",brdPicture);

                //  proud.setImage(prdPicture);

                board_items.add(new Board_item(R.drawable.board_icon, brdNickname, brdSubject, brdDate, brdContents, "댓글"));
            }


            //수정필요 : 검색결과 없을 때
            if(jsonArray.length()<=0){
                Log.d("검색결과없을때","여기로");
                LinearLayout searchBoard = (LinearLayout)findViewById(R.id.searchBoard);
                TextView noSearchResult = new TextView(BoardSearchActivity.this);
                noSearchResult.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                noSearchResult.setPadding(10, 10, 10, 10);
                noSearchResult.setTextColor(getResources().getColor(R.color.colorAccent));
                noSearchResult.setTextSize(13);
                noSearchResult.setText("검색결과가 없습니다.");
                searchBoard.addView(noSearchResult);
            }

            boardView.setLayoutManager(layoutManager_board);
            Adapter_board = new Adapter_boardList(this, board_items, 1);
            // Adapter_proud.notifyDataSetChanged();
            boardView.setAdapter(Adapter_board);

        } catch (JSONException e) {

        }

    }
}
