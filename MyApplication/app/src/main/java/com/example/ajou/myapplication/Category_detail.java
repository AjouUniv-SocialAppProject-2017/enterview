package com.example.ajou.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    TextView selectedQuestion;
    String select;
    String finalquestion;

    public static String[] listBoardId = new String[100];

    Button record_start;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_detail);
        Intent intent = getIntent();
        final int category_num = intent.getExtras().getInt("category_num");
        selectedQuestion = (TextView) findViewById(R.id.selected_question);

        record_start = (Button) findViewById(R.id.record_start);
        record_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(),category_num+"번쨰 리스트",Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(getApplicationContext(), BoardWriteActivity.class);
                Intent intent  = new Intent(getApplicationContext(),RecordActivity.class);
                intent.putExtra("finalquestion",finalquestion);
                startActivity(intent);

            }
        });

        questionView = (RecyclerView) findViewById(R.id.question_list);
        questionView.setHasFixedSize(true);
        layoutManager_question = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        ArrayList<String> question_item = new ArrayList<>();


        for (int i = 0; i < 15; i++) {
            select = i+"번째 질문입니다";
            question_item.add(select);
            // 질문 받아서

        }


        questionView.setLayoutManager(layoutManager_question);
        Adapter_questionList = new Adapter_questionList(this, question_item, 1);
        questionView.setAdapter(Adapter_questionList);
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
                    Toast.makeText(getApplicationContext(), tv.getText().toString(), Toast.LENGTH_SHORT).show();
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


}
