package com.example.ajou.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    static int flag = 0;
    String param_email;
    String param_nickname;
    String param_notification;
    String param_major;
    String param_usrIdx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(flag == 0) {
            // user information 가져오기
            Intent intent = getIntent();
            param_email = intent.getExtras().getString("param_email");
            param_nickname = intent.getExtras().getString("param_nickname");
            param_notification = intent.getExtras().getString("param_notification");
            param_major = intent.getExtras().getString("param_major");
            param_usrIdx = intent.getExtras().getString("param_usrIdx");
            //Toast.makeText(getApplicationContext(), param_email + "/" + param_nickname + "/" + param_major + "/" + param_notification + "/" + param_usrIdx, Toast.LENGTH_LONG).show();
            flag ++;
        }
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
                    Fragment categoryFragment = new CategoryFragment();
                    Bundle bundle0 = new Bundle(1); // 파라미터는 전달할 데이터 개수
                    bundle0.putString("param_usrIdx", param_usrIdx); // key , value
                    categoryFragment.setArguments(bundle0);
                    return categoryFragment;
                case 1:
                    Fragment boardFragment = new BulletinBoardFragment();
                    Bundle bundle1 = new Bundle(1); // 파라미터는 전달할 데이터 개수
                    bundle1.putString("param_usrIdx", param_usrIdx); // key , value
                    boardFragment.setArguments(bundle1);
                    return boardFragment;
                case 2:
                    Fragment myPageFragment = new MyPageFragment();
                    Bundle bundle2 = new Bundle(1); // 파라미터는 전달할 데이터 개수
                    bundle2.putString("param_email",param_email);
                    bundle2.putString("param_nickname",param_nickname);
                    bundle2.putString("param_notification",param_notification);
                    bundle2.putString("param_major",param_major);
                    bundle2.putString("param_usrIdx",param_usrIdx);
                    myPageFragment.setArguments(bundle2);
                    return myPageFragment;
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        flag = 0 ;
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
