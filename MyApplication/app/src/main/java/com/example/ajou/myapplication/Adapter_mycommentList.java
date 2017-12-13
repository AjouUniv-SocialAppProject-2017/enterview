package com.example.ajou.myapplication;

        import android.content.Context;
        import android.content.Intent;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.List;
/**
 * Created by ajou on 2017-12-13.
 */


public class Adapter_mycommentList  extends RecyclerView.Adapter<Adapter_mycommentList.ViewHolder>  {
    Context context;
    List<String> items;
    int item_layout;
    //Category_detail activity = new Category_detail();


    public Adapter_mycommentList(Context context, List<String> items, int item_layout) {
        this.context=context;
        this.items=items;
        this.item_layout=item_layout;
    }

    @Override
    public Adapter_mycommentList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mycommentlist,parent,false);
        Adapter_mycommentList.ViewHolder holder = new Adapter_mycommentList.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String item = items.get(position);
        holder.question.setText(""+item);
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



        TextView question;

        public ViewHolder(View itemView) {

            super(itemView);
            question = (TextView)itemView.findViewById(R.id.mycomment);
        }
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }
}
