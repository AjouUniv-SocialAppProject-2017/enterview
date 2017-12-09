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
    VideoView videoView;

    BulletinBoardFragment fr = new BulletinBoardFragment();

    private RecyclerView boardReview;
    private  RecyclerView.Adapter Adapter_board_review;
    private RecyclerView.LayoutManager layoutManager_board_review;

    String mJsonString;

    public Adapter_boardList(Context context, List<Board_item> items, int item_layout) {
        this.context=context;
        this.items=items;
        this.item_layout=item_layout;
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
        Uri uri = Uri.parse(item.getUrl());
        holder.videoView.setVideoURI(uri);
        final MediaController mediaController = new MediaController(context);
        holder.videoView.setMediaController(mediaController);

        Log.d("name Url 확인",""+item.getName()+"/"+item.getUrl());


        final int posi = holder.getAdapterPosition();

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        final int width = dm.widthPixels;
        final int height = dm.heightPixels;
/*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemId= fr.listBoardId[posi];
                Intent intent = new Intent(context, BoardDetailActivity.class);
                intent.putExtra("itemId",itemId);
                context.startActivity(intent);
            }
        });
*/


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

                    String itemId= fr.listBoardId[posi];
                    Log.d("댓글 아이디",""+itemId);
                    GetData task = new GetData();
                    task.execute(itemId);

                    btnUpload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //$prdliId $prdcmUserId $prdcmContents
                            String desc = commentDesc.getText().toString();
                            //수정필요 유저아이디
                            //String userId = log.userId;
                            String userId = "1";
                            String itemId = fr.listBoardId[posi];

                            InsertData task = new InsertData();
                            task.execute(itemId,userId,desc);

                            commentDesc.setText(null);

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

        // 별점 클릭 시 댓글창 생성
        holder.rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    popupView = inflater.inflate(R.layout.popup_rating, null);

                    final PopupWindow pw = new PopupWindow(popupView, width - 100, height - 200, true);
                    pw.setAnimationStyle(R.style.Animation_AppCompat_DropDownUp);
                    pw.showAtLocation(popupView, Gravity.CENTER, 0, 0);


                    btnClosePopup = (ImageButton) popupView.findViewById(R.id.btn_close_popup);
                    btnClosePopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pw.dismiss();
                        }
                    });

                    save_btn = (Button)popupView.findViewById(R.id.save_btn);
                    save_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pw.dismiss();
                        }
                    });



                    rb =(RatingBar)popupView.findViewById(R.id.ratingBar);
                    rb1 =(RatingBar)popupView.findViewById(R.id.ratingBar1);
                    rb2 =(RatingBar)popupView.findViewById(R.id.ratingBar2);
                    rb3 =(RatingBar)popupView.findViewById(R.id.ratingBar3);
                    rb4 =(RatingBar)popupView.findViewById(R.id.ratingBar4);
                    rb5 =(RatingBar)popupView.findViewById(R.id.ratingBar5);

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

            String serverURL = "http://52.41.114.24/enterview/boardReview.php";
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


            String prdliId = (String) params[0];

            String serverURL = "http://52.41.114.24/enterview/readbrdReview.php";
            String postParameters = "brdliId=" + prdliId ;

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

                String desc = item.getString("prdReviewDesc");
                String nick = item.getString("userNic");

                //  proud.setImage(prdPicture);

                boardReview_items.add(new BoardReview_item(nick,desc));
            }

            boardReview.setLayoutManager(layoutManager_board_review);
            Adapter_board_review = new Adapter_board_review(context, boardReview_items, 1);
            boardReview.setAdapter(Adapter_board_review);

        } catch (JSONException e) {
        }

    }

}