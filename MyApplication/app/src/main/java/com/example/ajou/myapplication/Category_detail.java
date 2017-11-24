package com.example.ajou.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by ajou on 2017-11-14.
 */

public class Category_detail extends AppCompatActivity {

    private RecyclerView questionView;
    private RecyclerView.Adapter Adapter_questionList;
    private RecyclerView.LayoutManager layoutManager_question;


    public static String[] listBoardId = new String[100];

    Button record_start;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_detail);
        Intent intent = getIntent();
        final int category_num = intent.getExtras().getInt("category_num");

        record_start = (Button) findViewById(R.id.record_start);
        record_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(),category_num+"번쨰 리스트",Toast.LENGTH_LONG).show();

            }
        });

        questionView = (RecyclerView) findViewById(R.id.question_list);
        questionView.setHasFixedSize(true);
        layoutManager_question = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        ArrayList<Question_item> question_item = new ArrayList<>();


        for (int i = 0; i < 10; i++) {


            question_item.add(new Question_item(i+"번째 질문입니다"));
        }

        questionView.setLayoutManager(layoutManager_question);
        Adapter_questionList = new Adapter_questionList(this, question_item, 1);
        questionView.setAdapter(Adapter_questionList);
    }


}
