package com.example.ajou.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by ajou on 2017-12-13.
 */


public class Adapter_myboardList  extends RecyclerView.Adapter<Adapter_myboardList.ViewHolder>  {
    Context context;
    ArrayList<Myboardlist_item> items;
    int item_layout;
    String param_usrIdx;
    //Category_detail activity = new Category_detail();


    public Adapter_myboardList(Context context, ArrayList<Myboardlist_item> items, int item_layout, String param_usrIdx) {
        this.context=context;
        this.items=items;
        this.item_layout=item_layout;
        this.param_usrIdx=param_usrIdx;
    }

    @Override
    public Adapter_myboardList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myboardlist,parent,false);
        Adapter_myboardList.ViewHolder holder = new Adapter_myboardList.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Myboardlist_item item = items.get(position);
        holder.myboard.setText(""+item.getText());
        holder.myboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,BoardDetailActivity.class);
                intent.putExtra("brdIdx",item.getBrdIdx());
                intent.putExtra("param_usrIdx",param_usrIdx);
                context.startActivity(intent);
            }
        });
        /*
       holder.question.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(context,item,Toast.LENGTH_SHORT).show();
           }
       });
*/
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {



        TextView myboard;

        public ViewHolder(View itemView) {

            super(itemView);
            myboard = (TextView)itemView.findViewById(R.id.myboard);
        }
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }
}
