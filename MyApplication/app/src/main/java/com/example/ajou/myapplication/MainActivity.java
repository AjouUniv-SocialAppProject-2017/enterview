package com.example.ajou.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by ajou on 2017-11-08.
 */
// 여기가 로그인 후 나오게 되는 메인 액티비티
public class MainActivity extends AppCompatActivity {
    ViewPager pager;
    ImageButton category, bulletin, myPage; //탭 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        final String cookies = intent.getExtras().getString("cookies");
        Toast.makeText(getApplicationContext(),cookies,Toast.LENGTH_LONG).show();
        pager = (ViewPager)findViewById(R.id.pager);
        category= (ImageButton)findViewById(R.id.btn_category);
        bulletin = (ImageButton)findViewById(R.id.btn_bulletin);
        myPage = (ImageButton)findViewById(R.id.btn_mypage);

        pager.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(1); // 게시판이 첫 화면으로 설정

        View.OnClickListener movePageListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                int tag = (int)view.getTag();
                pager.setCurrentItem(tag);
            }
        };

        category.setOnClickListener(movePageListener);
        category.setTag(0);
        bulletin.setOnClickListener(movePageListener);
        bulletin.setTag(1);
        myPage.setOnClickListener(movePageListener);
        myPage.setTag(2);
    }

    private class pagerAdapter extends FragmentStatePagerAdapter
    {
        public pagerAdapter(FragmentManager fm )
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position)
            {
                case 0:
                    return new CategoryFragment();
                case 1:
                    return new BulletinBoardFragment();
                case 2:
                    return new MyPageFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // total page count
            return 3;
        }
    }

}
