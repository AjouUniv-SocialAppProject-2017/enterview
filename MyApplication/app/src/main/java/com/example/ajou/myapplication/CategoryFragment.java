package com.example.ajou.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by ajou on 2017-11-07.
 */
// 녹화 fragment

public class CategoryFragment extends Fragment {
    public CategoryFragment(){

    }
    private Button category1;
    private Button category2;
    private Button category3;
    private Button category4;
    private Button category5;
    private Button category6;
    private Button category7;
    private Button category8;

    int qstnCategory;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,

                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.question_category, container, false);
        Bundle bundle = getArguments();
        final String param_usrIdx = bundle.getString("param_usrIdx"); // 전달한 key 값

        출처: http://jizard.tistory.com/66 [JIZARD]
        category1 = (Button) view.findViewById(R.id.category1);
        category1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Category_detail.class);
                qstnCategory =1;
                intent.putExtra("qstnCategory",qstnCategory);
                intent.putExtra("param_usrIdx",param_usrIdx);
                startActivity(intent);
            }
        });
        category2 = (Button) view.findViewById(R.id.category2);
        category2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Category_detail.class);
                qstnCategory =2;
                intent.putExtra("qstnCategory",qstnCategory);
                intent.putExtra("param_usrIdx",param_usrIdx);
                startActivity(intent);
            }
        });
        category3 = (Button) view.findViewById(R.id.category3);
        category3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Category_detail.class);
                qstnCategory =3;
                intent.putExtra("qstnCategory",qstnCategory);
                intent.putExtra("param_usrIdx",param_usrIdx);
                startActivity(intent);
            }
        });
        category4 = (Button) view.findViewById(R.id.category4);
        category4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Category_detail.class);
                qstnCategory =4;
                intent.putExtra("qstnCategory",qstnCategory);
                intent.putExtra("param_usrIdx",param_usrIdx);
                startActivity(intent);
            }
        });
        category5 = (Button) view.findViewById(R.id.category5);
        category5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Category_detail.class);
                qstnCategory =5;
                intent.putExtra("qstnCategory",qstnCategory);
                intent.putExtra("param_usrIdx",param_usrIdx);
                startActivity(intent);
            }
        });
        category6 = (Button) view.findViewById(R.id.category6);
        category6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Category_detail.class);
                qstnCategory =6;
                intent.putExtra("qstnCategory",qstnCategory);
                intent.putExtra("param_usrIdx",param_usrIdx);
                startActivity(intent);
            }
        });
        category7 = (Button) view.findViewById(R.id.category7);
        category7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Category_detail.class);
                qstnCategory =7;
                intent.putExtra("qstnCategory",qstnCategory);
                intent.putExtra("param_usrIdx",param_usrIdx);
                startActivity(intent);
            }
        });
        category8 = (Button) view.findViewById(R.id.category8);
        category8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Category_detail.class);
                qstnCategory =8;
                intent.putExtra("qstnCategory",qstnCategory);
                intent.putExtra("param_usrIdx",param_usrIdx);
                startActivity(intent);
            }
        });

        return view;
    }

}
