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
 * Created by thfad_000 on 2017-05-05.
 */
public class Adapter_boardList extends RecyclerView.Adapter<Adapter_boardList.ViewHolder> {

    Context context;
    List<Board_item> items;
    int item_layout;

    BulletinBoardFragment fr = new BulletinBoardFragment();

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

        holder.name.setText(item.getName());
        holder.title.setText(item.getTitle());
        holder.comment.setText(item.getComment());

        final int posi = holder.getAdapterPosition();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemId= fr.listBoardId[posi];
                Intent intent = new Intent(context, BoardDetailActivity.class);
                intent.putExtra("itemId",itemId);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView date;
        TextView name;
        TextView title;
        TextView comment;

        public ViewHolder(View itemView) {

            super(itemView);
            name=(TextView)itemView.findViewById(R.id.proud_name);
            title = (TextView)itemView.findViewById(R.id.proud_title);
            comment = (TextView)itemView.findViewById(R.id.proud_comment);
        }
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

}