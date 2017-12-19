package com.example.ajou.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

    String mJsonString;
    static int check = 0;
    static int check2 = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        if(flag == 0) {
            // user information 가져오기
            param_email = intent.getExtras().getString("param_email");
            param_nickname = intent.getExtras().getString("param_nickname");
            param_notification = intent.getExtras().getString("param_notification");
            param_major = intent.getExtras().getString("param_major");
            param_usrIdx = intent.getExtras().getString("param_usrIdx");
            //Toast.makeText(getApplicationContext(), param_email + "/" + param_nickname + "/" + param_major + "/" + param_notification + "/" + param_usrIdx, Toast.LENGTH_LONG).show();
            ShowDialog(param_nickname+"님 환영합니다",1);
            flag ++;
        }else{
            param_email = intent.getExtras().getString("param_email");
            param_nickname = intent.getExtras().getString("param_nickname");
            param_notification = intent.getExtras().getString("param_notification");
            param_major = intent.getExtras().getString("param_major");
            param_usrIdx = intent.getExtras().getString("param_usrIdx");
        }

        pager = (ViewPager)findViewById(R.id.pager);
        category= (ImageButton)findViewById(R.id.btn_category);
        bulletin = (ImageButton)findViewById(R.id.btn_bulletin);
        myPage = (ImageButton)findViewById(R.id.btn_mypage);
        category.setSelected(false);
        bulletin.setSelected(true);
        myPage.setSelected(false);

        pager.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(1); // 게시판이 첫 화면으로 설정

        View.OnClickListener movePageListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                int tag = (int)view.getTag();

                //현재 날짜
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String str = sdf.format(date);

                if(tag==0){
                    check2=2;
                    //당일 댓글 수 체크
                    GetData getData = new GetData();
                    getData.execute("http://52.41.114.24/enterview/checkUsr.php?usrIdx="+param_usrIdx+"&date="+str);
                }

                pager.setCurrentItem(tag);

                if(tag == 0){
                    category.setSelected(true);
                    bulletin.setSelected(false);
                    myPage.setSelected(false);
                }else if(tag == 1){
                    category.setSelected(false);
                    bulletin.setSelected(true);
                    myPage.setSelected(false);
                }else if(tag == 2){
                    category.setSelected(false);
                    bulletin.setSelected(false);
                    myPage.setSelected(true);
                }
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
                    bundle0.putString("param_major",param_major);
                    bundle0.putString("param_nickname",param_nickname);
                    bundle0.putString("param_notification",param_notification);
                    bundle0.putString("param_email",param_email);
                    categoryFragment.setArguments(bundle0);
                    //현재 날짜
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String str = sdf.format(date);

                    //댓글 수 체크
                    GetData getData = new GetData();
                    getData.execute("http://52.41.114.24/enterview/checkUsr.php?usrIdx="+param_usrIdx+"&date="+str);
                    return categoryFragment;

                case 1:
                    Fragment boardFragment = new BulletinBoardFragment();
                    Bundle bundle1 = new Bundle(1); // 파라미터는 전달할 데이터 개수
                    bundle1.putString("param_usrIdx", param_usrIdx); // key , value
                    bundle1.putString("param_major",param_major);
                    bundle1.putString("param_nickname",param_nickname);
                    bundle1.putString("param_notification",param_notification);
                    bundle1.putString("param_email",param_email);
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
        //super.onBackPressed();
        flag = 0 ;
        ShowDialog("로그아웃 하시겠습니까?", 0);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                Log.e("잘하자",e.getMessage());
                errorString = e.toString();
                return null;
            }

        }
    }

    private void showResult() {
        try {

            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("webnautes");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String countCmnt = item.getString("countCmnt");
                final int minCountCmnt = 3;
                int numCmnt = Integer.parseInt(countCmnt);

                check = (minCountCmnt-numCmnt);

                if(check>0&&check2==2){
                    pager.setCurrentItem(1);
                    category.setSelected(false);
                    bulletin.setSelected(true);
                    myPage.setSelected(false);

                    ShowDialog("게시판에서 "+check+"개의 댓글을 추가로 작성하면 영상 녹화가 가능합니다.",1);
                    check2=1;
                }
            }

        } catch (JSONException e) {
            Log.e("checkError",e.getMessage());

        }
    }

    private void ShowDialog(String contents, int type) {
        // two button
        if (type == 0) {
            LayoutInflater dialog = LayoutInflater.from(this);
            final View dialogLayout = dialog.inflate(R.layout.dialog, null);
            final Dialog myDialog = new Dialog(this);

            myDialog.setContentView(dialogLayout);
            myDialog.show();

            Button btn_ok = (Button) dialogLayout.findViewById(R.id.btn_ok);
            Button btn_cancel = (Button) dialogLayout.findViewById(R.id.btn_cancel);
            TextView contentsView = (TextView) dialogLayout.findViewById(R.id.dialog_contents);
            contentsView.setText(contents);

            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.cancel();
                }
            });
        }else if(type == 1){
            final LayoutInflater dialog = LayoutInflater.from(this);
            final View dialogLayout = dialog.inflate(R.layout.dialog2, null);
            final Dialog myDialog = new Dialog(this);

            myDialog.setContentView(dialogLayout);
            myDialog.show();

            Button btn_ok = (Button) dialogLayout.findViewById(R.id.btn_one_ok);
            Button btn_cancel = (Button) dialogLayout.findViewById(R.id.btn_cancel);
            TextView contentsView = (TextView) dialogLayout.findViewById(R.id.dialog_contents);
            contentsView.setText(contents);

            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                }
            });
        }

    }

}
