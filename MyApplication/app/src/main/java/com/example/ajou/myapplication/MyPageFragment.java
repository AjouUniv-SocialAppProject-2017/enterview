package com.example.ajou.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by ajou on 2017-11-08.
 */
// mypage fragment
public class MyPageFragment extends Fragment {

    ImageView updateBtn;
    TextView personalIdContents;
    TextView personalNickContents;

/*    public MyPageFragment()
    {
        // required
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,

                             @Nullable Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.my_page,
                container, false);
        Bundle bundle = getArguments();
        String param_usrIdx = bundle.getString("param_usrIdx"); // 전달한 key 값
        String param_email = bundle.getString("param_email");
        String param_nickname = bundle.getString("param_nickname");
        String param_notification = bundle.getString("param_notification");
        String param_major = bundle.getString("param_major");

        personalIdContents = (TextView) layout.findViewById(R.id.personalIdContents);
        personalNickContents = (TextView) layout.findViewById(R.id.personalNickContents);
        personalIdContents.setText(param_email);
        personalNickContents.setText(param_nickname);


        updateBtn = (ImageView) layout.findViewById(R.id.update_btn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SignupActivity.class);
                startActivity(intent);
            }
        });
        return layout;
    }
}
