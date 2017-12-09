package com.example.ajou.myapplication;

import android.content.Context;
import android.content.Intent;
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
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
public class Adapter_frqList extends RecyclerView.Adapter<Adapter_frqList.ViewHolder> {

    Context context;
    List<BoardSearch_frequestion_item> items;
    int item_layout;

    public Adapter_frqList (Context context, List<BoardSearch_frequestion_item> items, int item_layout) {
        this.context=context;
        this.items=items;
        this.item_layout=item_layout;
    }

    @Override
    public Adapter_frqList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_frequestion,parent,false);
        Adapter_frqList.ViewHolder holder = new Adapter_frqList.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(Adapter_frqList.ViewHolder holder, int position) {

        final BoardSearch_frequestion_item item=items.get(position);
        holder.desc.setText(item.getDesc());

        //질문 클릭리스너 안되네.. 흠
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BoardDetailActivity.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView desc;

        public ViewHolder(View itemView) {

            super(itemView);
            desc = (TextView)itemView.findViewById(R.id.fsq_list);
        }
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

}