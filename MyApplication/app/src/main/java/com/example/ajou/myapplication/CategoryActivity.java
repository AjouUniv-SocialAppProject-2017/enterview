package com.example.ajou.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by ajou on 2017-11-07.
 */

public class CategoryActivity extends AppCompatActivity {
    Button category1;
    Button category2;
    Button category3;
    Button category4;
    Button category5;
    Button category6;
    Button category7;
    Button category8;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_category);
        category1 = (Button) findViewById(R.id.category1);
        category2 = (Button) findViewById(R.id.category2);
        category3 = (Button) findViewById(R.id.category3);
        category4 = (Button) findViewById(R.id.category4);
        category5 = (Button) findViewById(R.id.category5);
        category6 = (Button) findViewById(R.id.category6);
        category7 = (Button) findViewById(R.id.category7);
        category8 = (Button) findViewById(R.id.category8);

        category1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        category2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        category3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        category4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        category5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        category6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        category7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        category8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
