package com.bigjeon.newscheck;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RCVadapter_News extends RecyclerView.Adapter<RCVadapter_News.ViewHolder>{

    private ArrayList<Category_Item> mList;
    private Context context;
    private Checked_list checked_list;

    public RCVadapter_News(ArrayList<Category_Item> list, Context context){
        this.mList = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView WriteDate;
        private String URL;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                this.title = itemView.findViewById(R.id.Category_title);
                this.WriteDate = itemView.findViewById(R.id.writeDate);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int curPos = getAdapterPosition();
                        Category_Item category_item = mList.get(curPos);
                        String url = category_item.getURL();
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.setData(Uri.parse(url));
                        context.startActivity(intent);
                        Toast.makeText(context, url, Toast.LENGTH_LONG).show();
                    }
                });

        }
    }

    @NonNull
    @Override
    public RCVadapter_News.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list, parent,false);
        return new ViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull RCVadapter_News.ViewHolder holder, int position) {
        holder.title.setText(mList.get(position).getTitle());
        holder.WriteDate.setText(mList.get(position).getWriteDate());
        holder.URL = (mList.get(position).getURL());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    public void addNews(Category_Item News){
        mList.add(News);
        notifyDataSetChanged();
    }

    public void removeItem(){
        mList.clear();
        notifyDataSetChanged();
    }
}
