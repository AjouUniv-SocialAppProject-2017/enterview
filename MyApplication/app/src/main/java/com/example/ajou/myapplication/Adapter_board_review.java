package com.example.ajou.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by thfad_000 on 2017-05-28.
 */
public class Adapter_board_review extends RecyclerView.Adapter<Adapter_board_review.ViewHolder> {

    Context context;
    List<BoardReview_item> items;
    int item_layout;

    public Adapter_board_review (Context context, List<BoardReview_item> items, int item_layout) {
        this.context=context;
        this.items=items;
        this.item_layout=item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_comment,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final BoardReview_item item=items.get(position);

        holder.name.setText(item.getName());
        holder.desc.setText(item.getDesc());


    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView desc;

        public ViewHolder(View itemView) {

            super(itemView);
            name=(TextView)itemView.findViewById(R.id.question_review_id);
            desc = (TextView)itemView.findViewById(R.id.question_review_desc);
        }
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }


}
