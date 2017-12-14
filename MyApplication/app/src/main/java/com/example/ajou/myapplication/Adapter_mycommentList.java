package com.example.ajou.myapplication;

        import android.content.Context;
        import android.content.Intent;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.ArrayList;
        import java.util.List;
/**
 * Created by ajou on 2017-12-13.
 */


public class Adapter_mycommentList  extends RecyclerView.Adapter<Adapter_mycommentList.ViewHolder>  {
    Context context;
    ArrayList<Mycommentlist_item> items;
    int item_layout;
    String param_usrIdx;
    //Category_detail activity = new Category_detail();


    public Adapter_mycommentList(Context context, ArrayList<Mycommentlist_item> items, int item_layout, String param_usrIdx) {
        this.context=context;
        this.items=items;
        this.item_layout=item_layout;
        this.param_usrIdx=param_usrIdx;
    }

    @Override
    public Adapter_mycommentList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mycommentlist,parent,false);
        Adapter_mycommentList.ViewHolder holder = new Adapter_mycommentList.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Mycommentlist_item item = items.get(position);
        holder.date.setText(item.getDate());
        holder.desc.setText(item.getComment());
        holder.mycomment_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,BoardDetailActivity.class);
                intent.putExtra("brdIdx",item.getBrdIdx());
                intent.putExtra("param_usrIdx",param_usrIdx);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mycomment_list;
        TextView date, desc;

        public ViewHolder(View itemView) {

            super(itemView);
            mycomment_list = (LinearLayout)itemView.findViewById(R.id.mycomment_list);
            date = (TextView)itemView.findViewById(R.id.mycomment_date);
            desc = (TextView)itemView.findViewById(R.id.mycomment_desc);
        }
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }
}
