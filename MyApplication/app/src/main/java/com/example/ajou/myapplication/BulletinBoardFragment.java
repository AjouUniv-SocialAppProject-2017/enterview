package com.example.ajou.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ajou on 2017-11-08.
 */
// 게시판 fragment
public class BulletinBoardFragment extends Fragment {

    private RecyclerView boardView;
    private RecyclerView.Adapter Adapter_board;
    Board_item board_item;
    private RecyclerView.LayoutManager layoutManager_board;
    public static String[] listBoardId = new String[100];
    private ImageButton searchBtn;

    String mJsonString;
    public static String[] listId = new String[100];
/*
    public BulletinBoardFragment()
    {
        // required
    }
*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       // RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.bulletin_board,
       //         container, false);
        final View view = inflater.inflate(R.layout.bulletin_board, container, false);
        board_item = new Board_item();

        boardView = (RecyclerView) view.findViewById(R.id.board_list);
        boardView.setHasFixedSize(true);

        layoutManager_board = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        //게시글 ArrayList
/*        ArrayList<Board_item> board_items = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            board_items.add(new Board_item(0, "닉네임", "제목", "날짜", "desc", "댓글"));
        }

        boardView.setLayoutManager(layoutManager_board);
        Adapter_board = new Adapter_boardList(getActivity(), board_items, 1);
        boardView.setAdapter(Adapter_board);*/

        //검색버튼 클릭 시, 검색 액티비티로 이동
        searchBtn = (ImageButton)view.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BoardSearchActivity.class);
                startActivity(intent);
            }
        });

        //데이터 받아오기
        GetData task = new GetData();
        task.execute("http://52.41.114.24/enterview/boardList.php");

        //영상
        //VideoView videoView = (VideoView)view.findViewById(R.id.videoView);
        //
        //String SAMPLE_VIDEO_URL = "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_2mb.mp4";
        //videoView.setVideoURI(Uri.parse(SAMPLE_VIDEO_URL));
        //
        // final MediaController mediaController = new MediaController(this.getContext());
        //videoView.setMediaController(mediaController);

        return view;
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

            ArrayList<Board_item> board_items = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String brdIdx = item.getString("brdIdx");
                listId[i] = brdIdx;
                Log.d("여기 리스트 아이디",""+listId[i]);
                String brdContents = item.getString("brdContents");
                String brdSubject = item.getString("brdSubject");
                String brdDate = item.getString("brdDate");
                String brdNickname = item.getString("brdNickname");

                //Log.d("이미지소스",brdPicture);

                //  proud.setImage(prdPicture);

                board_items.add(new Board_item(R.drawable.board_icon, brdNickname, brdSubject, brdDate, brdContents, "댓글"));
            }

            boardView.setLayoutManager(layoutManager_board);
            Adapter_board = new Adapter_boardList(getActivity(), board_items, 1);
            // Adapter_proud.notifyDataSetChanged();
            boardView.setAdapter(Adapter_board);

        } catch (JSONException e) {
        }

    }

}


