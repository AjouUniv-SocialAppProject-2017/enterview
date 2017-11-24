package com.example.ajou.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ajou on 2017-11-14.
 */

public class Adapter_questionList  extends RecyclerView.Adapter<Adapter_questionList.ViewHolder>  {
    Context context;
    List<Question_item> items;
    int item_layout;
    //Category_detail activity = new Category_detail();

    public Adapter_questionList(Context context, List<Question_item> items, int item_layout) {
        this.context=context;
        this.items=items;
        this.item_layout=item_layout;
    }

    @Override
    public Adapter_questionList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_questionlist,parent,false);
        Adapter_questionList.ViewHolder holder = new Adapter_questionList.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(Adapter_questionList.ViewHolder holder, int position) {

        final Question_item item=items.get(position);
        holder.question.setText((position+1)+"번째 질문입니다");

        final int posi = holder.getAdapterPosition();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String itemId= activity.listBoardId[posi];
                //Intent intent = new Intent(context, BoardDetailActivity.class);
                //intent.putExtra("itemId",itemId);
                //context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView question;

        public ViewHolder(View itemView) {

            super(itemView);
            question = (TextView)itemView.findViewById(R.id.question);
        }
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }
}
