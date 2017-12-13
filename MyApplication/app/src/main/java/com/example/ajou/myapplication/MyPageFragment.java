package com.example.ajou.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ajou on 2017-11-08.
 */
// mypage fragment
public class MyPageFragment extends Fragment {

    ImageView updateBtn;
    TextView personalIdContents;
    TextView personalNickContents;
    String param_usrIdx;
    String param_email ;
    String param_nickname ;
    String param_notification;
    String param_major ;

    Button boardlist;
    Button commentlist;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container,

                             @Nullable Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.my_page,
                container, false);
        Bundle bundle = getArguments();
        param_usrIdx = bundle.getString("param_usrIdx"); // 전달한 key 값
        param_email = bundle.getString("param_email");
        param_nickname = bundle.getString("param_nickname");
        param_notification = bundle.getString("param_notification");
        param_major = bundle.getString("param_major");

        boardlist = (Button) layout.findViewById(R.id.myText);
        commentlist = (Button) layout.findViewById(R.id.myComment);
        personalIdContents = (TextView) layout.findViewById(R.id.personalIdContents);
        personalNickContents = (TextView) layout.findViewById(R.id.personalNickContents);
        personalIdContents.setText(param_email);
        personalNickContents.setText(param_nickname);

        boardlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MyBoardlist.class);
                intent.putExtra("param_usrIdx",param_usrIdx);
                startActivity(intent);
            }
        });

        commentlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MyCommentlist.class);
                intent.putExtra("param_usrIdx",param_usrIdx);
                startActivity(intent);
            }
        });
        updateBtn = (ImageView) layout.findViewById(R.id.update_btn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Mod_information.class);
                intent.putExtra("param_usrIdx",param_usrIdx);
                intent.putExtra("param_email",param_email);
                intent.putExtra("param_nickname",param_nickname);
                intent.putExtra("param_notification",param_notification);
                intent.putExtra("param_major",param_major);
                startActivityForResult(intent,1);
            }
        });
        return layout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode== Activity.RESULT_OK){
                String email = data.getStringExtra("sEmail");
                String nickname = data.getStringExtra("sNickName");
                String notification = data.getStringExtra("sNotification");
                String major = data.getStringExtra("sMajor");
                param_email = email;
                param_nickname = nickname;
                param_notification = notification;
                param_major = major;
                //Toast.makeText(getApplicationContext(),"ssss",Toast.LENGTH_SHORT).show();
                personalIdContents.setText(email);
                personalNickContents.setText(nickname);
                Log.d("killlllllllllllllllll", email+"/"+nickname+"/"+notification+"/"+major);
            }
        }
    }

}
