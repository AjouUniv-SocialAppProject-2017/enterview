package com.example.ajou.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by thfad_000 on 2017-05-08.
 */
public class WriteActivity extends AppCompatActivity {

    private Button btnClosePopup;
    private Button btnCreatePopup;
    private int mWidthPixels, mHeightPixels;

    EditText question_desc;
    EditText question_sub;
    private LoginActivity log;

    String s_desc,s_sub,s_user,s_image;

    final int REQ_CODE_SELECT_IMAGE=100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);

        log = new LoginActivity();

        question_desc = (EditText) findViewById(R.id.question_desc);
        question_sub = (EditText) findViewById(R.id.question_sub);
    }

    // 업로드 버튼
    public void questionWrite_upload(View v){

/*        s_desc = question_desc.getText().toString();
        s_sub = question_sub.getText().toString();
        //s_user = log.userId;

        InsertData task = new InsertData();
        task.execute(s_sub,s_desc,"111",s_user);

        Log.d("이걸봐", "" + s_desc+" "+s_image+" "+s_user);

        this.finish();*/
    }


    // 뒤로가기 버튼
    public void questionWrite_cancel(View v){
        this.finish();
    }
    // 사진 올리기
    public void uploadPhoto(View v){

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
    }

    // 이미지 가져오기
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {

                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    ImageView image = (ImageView) findViewById(R.id.question_image);

                    String imageS = getStringImage(image_bitmap);

                    s_image = image_bitmap.toString();

                    //배치해놓은 ImageView에 set
                    image.setImageBitmap(image_bitmap);

                } catch (Exception e) {

                }
            }
        }
    }


    /*class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(QuestionWriteActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
        }


        @Override
        protected String doInBackground(String... params) {


            String qstSubject = (String) params[0];
            String qstContents = (String) params[1];
            String qstPicture = (String) params[2];
            String qstUserId = (String) params[3];

            String serverURL = "http://52.41.114.24/questionUpload.php";
            String postParameters = "qstSubject=" + qstSubject + "&qstContents=" + qstContents+"&qstPicture="+qstPicture
                    + "&qstUserId=" + qstUserId;

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
    }*/
    public String getStringImage(Bitmap bmp){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }
}
