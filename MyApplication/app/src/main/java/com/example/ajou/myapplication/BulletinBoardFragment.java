package com.example.ajou.myapplication;

import android.content.Intent;
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
import android.widget.RelativeLayout;

import org.json.JSONObject;

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
        ArrayList<Board_item> board_items = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            board_items.add(new Board_item(0, "닉네임", "제목", "날짜", "desc", "댓글"));
        }

        boardView.setLayoutManager(layoutManager_board);
        Adapter_board = new Adapter_boardList(getActivity(), board_items, 1);
        boardView.setAdapter(Adapter_board);

        //검색버튼 클릭 시, 검색 액티비티로 이동
        searchBtn = (ImageButton)view.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BoardSearchActivity.class);
                startActivity(intent);
            }
        });

        /*데이터 받아오기?
        GetData task = new GetData();
        GetQuestionData task_q = new GetQuestionData();
        task.execute("http://52.41.114.24/proudList.php");
        task_q.execute("http://52.41.114.24/questionList.php");*/
        return view;
    }

}


