package com.example.ajou.myapplication;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thfad_000 on 2017-05-05.
 */
public class Adapter_boardList extends RecyclerView.Adapter<Adapter_boardList.ViewHolder> {

    Context context;
    List<Board_item> items;
    int item_layout;
    private View popupView;
    private ImageButton btnClosePopup;
    RatingBar rb, rb1, rb2, rb3, rb4, rb5;
    Button save_btn;
    LinearLayout popup_element;

    BulletinBoardFragment fr = new BulletinBoardFragment();

    private RecyclerView boardReview;
    private  RecyclerView.Adapter Adapter_board_review;
    private RecyclerView.LayoutManager layoutManager_board_review;

    String mJsonString;

    String usrIdx;
    String check = "";

    public Adapter_boardList(Context context, List<Board_item> items, int item_layout, String usrIdx) {
        this.context=context;
        this.items=items;
        this.item_layout=item_layout;
        this.usrIdx=usrIdx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Board_item item=items.get(position);
        holder.contents.setText(item.getDesc());
        holder.name.setText(item.getName());
        holder.title.setText(item.getTitle());
        holder.comment.setText(item.getComment());
        holder.rating.setText(item.getRating());
        Uri uri = Uri.parse(item.getUrl());
        holder.videoView.setVideoURI(uri);
        final MediaController mediaController = new MediaController(context);
        holder.videoView.setMediaController(mediaController);

        Log.d("name Url 확인",""+item.getName()+"/"+item.getUrl());


        final int posi = holder.getAdapterPosition();

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        final int width = dm.widthPixels;
        final int height = dm.heightPixels;


        // 댓글 클릭 시 댓글창 생성
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    popupView = inflater.inflate(R.layout.popup_review, null);
                    final PopupWindow pw = new PopupWindow(popupView, width-50,height-150, true);
                    pw.setAnimationStyle(R.style.Animation_AppCompat_DropDownUp);
                    pw.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                    pw.setOutsideTouchable(true);
                    btnClosePopup = (ImageButton) popupView.findViewById(R.id.btn_close_popup);
                    Button btnUpload = (Button) popupView.findViewById(R.id.comment_upload);
                    final EditText commentDesc = (EditText) popupView.findViewById(R.id.comment_desc);

                    // 댓글 리스트
                    boardReview = (RecyclerView) popupView.findViewById(R.id.board_comment);
                    boardReview.setHasFixedSize(true);

                    layoutManager_board_review = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

                    final String itemId= fr.listId[posi];
                    Log.d("댓글이 달릴 게시글 아이디",""+itemId);
                    GetData getTask = new GetData();
                    getTask.execute(itemId);

                    btnUpload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String desc = commentDesc.getText().toString();
                            String userId = usrIdx;

                            InsertData insertTask = new InsertData();
                            insertTask.execute(itemId,userId,desc);

                            commentDesc.setText(null);
                            //수정필요 댓글 update 어케 하냐..
                            boardReview.removeAllViewsInLayout();
                            GetData getTask = new GetData();
                            getTask.execute(itemId);

                        }
                    });

                    btnClosePopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pw.dismiss();
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // 별점 클릭 시 팝업창 생성
        holder.rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    popupView = inflater.inflate(R.layout.popup_rating, null);

                    final PopupWindow pw = new PopupWindow(popupView, width - 100, height - 200, true);
                    pw.setAnimationStyle(R.style.Animation_AppCompat_DropDownUp);
                    pw.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                    final String itemId= fr.listId[posi];

                    btnClosePopup = (ImageButton) popupView.findViewById(R.id.btn_close_popup);
                    btnClosePopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pw.dismiss();
                        }
                    });

                    //각각 rating 제어
                    rb =(RatingBar)popupView.findViewById(R.id.ratingBar);
                    rb1 =(RatingBar)popupView.findViewById(R.id.ratingBar1);
                    rb2 =(RatingBar)popupView.findViewById(R.id.ratingBar2);
                    rb3 =(RatingBar)popupView.findViewById(R.id.ratingBar3);
                    rb4 =(RatingBar)popupView.findViewById(R.id.ratingBar4);
                    rb5 =(RatingBar)popupView.findViewById(R.id.ratingBar5);

                    //별점 데이터 read
                    GetRatingData getData = new GetRatingData();
                    getData.execute(itemId,usrIdx);

                    RatingBar.OnRatingBarChangeListener ratingBarChangeListener = new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float rating,
                                                    boolean fromUser) {
                            float total_rating = rb1.getRating()+rb2.getRating()+rb3.getRating()+rb4.getRating()+rb5.getRating();
                            rb.setRating(total_rating/5);
                        }
                    };

                    rb1.setOnRatingBarChangeListener(ratingBarChangeListener);
                    rb2.setOnRatingBarChangeListener(ratingBarChangeListener);
                    rb3.setOnRatingBarChangeListener(ratingBarChangeListener);
                    rb4.setOnRatingBarChangeListener(ratingBarChangeListener);
                    rb5.setOnRatingBarChangeListener(ratingBarChangeListener);

                    //저장 버튼
                    save_btn = (Button)popupView.findViewById(R.id.save_btn);
                    save_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            InsertRatingData insertTask = new InsertRatingData();
                            insertTask.execute(itemId,usrIdx,""+rb1.getRating(),""+rb2.getRating(),""+rb3.getRating(),
                                    ""+rb4.getRating(),""+rb5.getRating(),""+rb.getRating());
                            pw.dismiss();
                        }
                    });

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView contents;
        TextView name;
        TextView title;
        TextView comment;
        TextView rating;
        VideoView videoView;

        public ViewHolder(View itemView) {

            super(itemView);
            contents=(TextView)itemView.findViewById(R.id.board_desc);
            name=(TextView)itemView.findViewById(R.id.board_name);
            title = (TextView)itemView.findViewById(R.id.board_title);
            comment = (TextView)itemView.findViewById(R.id.board_comment);
            rating = (TextView)itemView.findViewById(R.id.rating_btn);
            videoView = (VideoView) itemView.findViewById(R.id.videoView);
        }
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    class InsertData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }


        @Override
        protected String doInBackground(String... params) {


            //$prdliId $prdcmUserId $prdcmContents

            String prdliId = (String) params[0];
            String prdcmUserId = (String) params[1];
            String prdcmContents = (String) params[2];

            String serverURL = "http://52.41.114.24/enterview/insertBrdReview.php";
            String postParameters = "brdliId=" + prdliId + "&brdcmUserId=" + prdcmUserId
                    + "&brdcmContents=" + prdcmContents;

            Log.d("이거봐", "" + postParameters);

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                //httpURLConnection.setRequestProperty("content-type", "application/json");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

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
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                Log.d("디비 성공", "디비성공함");
                return sb.toString();


            } catch (Exception e) {

                Log.d("디비에러", "디비에러났대");
                return new String("Error: " + e.getMessage());
            }

        }
    }

    private class GetData extends AsyncTask<String, Void, String> {

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


            String brdliId = (String) params[0];

            String serverURL = "http://52.41.114.24/enterview/readBrdReview.php";
            String postParameters = "brdliId=" + brdliId ;

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);

                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

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
                errorString = e.toString();
                return null;
            }

        }
    }

    private void showResult() {
        try {

            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("webnautes");

            ArrayList<BoardReview_item> boardReview_items = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String desc = item.getString("reviewDesc");
                String nick = item.getString("userNic");
                Log.d("댓글아나와라",nick+":"+desc);

                //  proud.setImage(prdPicture);

                boardReview_items.add(new BoardReview_item(nick,desc));
            }

            boardReview.setLayoutManager(layoutManager_board_review);
            Adapter_board_review = new Adapter_board_review(context, boardReview_items, 1);
            boardReview.setAdapter(Adapter_board_review);

            Log.d("GetData", "Success");

        } catch (JSONException e) {
            Log.d("GetData", "Error");
        }

    }

    //★★★★★★★★★★★★★★★★별점★★★★★★★★★★★★★★★★
    class InsertRatingData extends AsyncTask<String, Void, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }


        @Override
        protected String doInBackground(String... params) {


            //$prdliId $prdcmUserId $prdcmContents

            String brdIdx = (String) params[0];
            String usrIdx = (String) params[1];
            String rb1 = (String) params[2];
            String rb2 = (String) params[3];
            String rb3 = (String) params[4];
            String rb4 = (String) params[5];
            String rb5 = (String) params[6];
            String rb = (String) params[7];

            String serverURL = "http://52.41.114.24/enterview/insertBrdRating.php";
            String postParameters = "brdIdx=" + brdIdx + "&usrIdx=" + usrIdx + "&rb1=" + rb1
                    + "&rb2=" + rb2 + "&rb3=" + rb3 + "&rb4=" + rb4 + "&rb5=" + rb5 + "&rb=" + rb;

            Log.d("이거봐", "" + postParameters);

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                //httpURLConnection.setRequestProperty("content-type", "application/json");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

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
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                Log.d("디비 성공", "디비성공함");
                return sb.toString();


            } catch (Exception e) {

                Log.d("디비에러", "디비에러났대");
                return new String("Error: " + e.getMessage());
            }

        }
    }

    private class GetRatingData extends AsyncTask<String, Void, String> {

        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            mJsonString = result;
            showRatingResult();

        }

        @Override
        protected String doInBackground(String... params) {


            String brdIdx = (String) params[0];
            String usrIdx = (String) params[1];

            String serverURL = "http://52.41.114.24/enterview/readBrdRating.php";
            String postParameters = "brdIdx=" + brdIdx + "&usrIdx="+ usrIdx;
            Log.d("왜 안나오징",postParameters);

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);

                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

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
                errorString = e.toString();
                return null;
            }

        }
    }

    private void showRatingResult() {
        try {

            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("webnautes");

            Log.d("제발 좀", jsonArray.toString());

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String dbrb1 = item.getString("rb1");
                String dbrb2 = item.getString("rb2");
                String dbrb3 = item.getString("rb3");
                String dbrb4 = item.getString("rb4");
                String dbrb5 = item.getString("rb5");
                String dbrb = item.getString("rb");

                //내 글일 때
                if(item.getString("my").equals("my")){
                    //레이팅바 조절 X
                    rb1.setIsIndicator(true);
                    rb2.setIsIndicator(true);
                    rb3.setIsIndicator(true);
                    rb4.setIsIndicator(true);
                    rb5.setIsIndicator(true);
                    rb.setIsIndicator(true);

                    //저장버튼 삭제
                    popup_element = (LinearLayout)popupView.findViewById(R.id.popup_element);
                    TextView myRatingTxt = new TextView(popupView.getContext());
                    myRatingTxt.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                    myRatingTxt.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                    myRatingTxt.setPadding(10,10,0,10);
                    myRatingTxt.setText("다른 회원들이 평가한 각 항목의 평균 점수입니다.");
                    popup_element.addView(myRatingTxt);
                    popup_element.removeView(save_btn);

                }else{

                    Log.d("남의 글","others");
                }

                Log.d("댓글아나와라",dbrb1+","+dbrb2+","+dbrb3+","+dbrb4+","+dbrb5+"==>"+dbrb);

                rb1.setRating(Float.parseFloat(dbrb1));
                rb2.setRating(Float.parseFloat(dbrb2));
                rb3.setRating(Float.parseFloat(dbrb3));
                rb4.setRating(Float.parseFloat(dbrb4));
                rb5.setRating(Float.parseFloat(dbrb5));
                rb.setRating(Float.parseFloat(dbrb));

                //  proud.setImage(prdPicture);

                //boardReview_items.add(new BoardReview_item(nick,desc));
            }

            /*boardReview.setLayoutManager(layoutManager_board_review);
            Adapter_board_review = new Adapter_board_review(context, boardReview_items, 1);
            boardReview.setAdapter(Adapter_board_review);*/

            Log.d("GetData", "Success");

        } catch (JSONException e) {
            Log.d("GetData", "Error");
            Log.e("에러",e.getMessage());
        }

    }


}