package com.propertygurutest.hackernewsreader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.propertygurutest.hackernewsreader.util.Util;


/**
 * Created by HJ Chin on 10/11/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    public NewsItem getItemAt(int i) {
        return data.get(i);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        final TextView no;
        final TextView title;
        final TextView source;
        final TextView point;
        final TextView author;
        final TextView time;
        final TextView comment;
        final View itemView;

        ViewHolder(View itemView) {
            super(itemView);

            no = (TextView)itemView.findViewById(R.id.no);
            title = (TextView)itemView.findViewById(R.id.title);
            source = (TextView)itemView.findViewById(R.id.source);
            point = (TextView)itemView.findViewById(R.id.point);
            author = (TextView)itemView.findViewById(R.id.author);
            time = (TextView)itemView.findViewById(R.id.time);
            comment = (TextView)itemView.findViewById(R.id.comment);
            this.itemView = itemView;
        }
    }

    private Context context;
    private ArrayList<NewsItem> data;
    private Callback callback;

    interface Callback{
        void onItemClick(int position);
        void requestNews(int position);
    }

    NewsAdapter(Context context, ArrayList<NewsItem> data, Callback callback){
        this.context = context;
        this.data = data;
        this.callback = callback;
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, final int position) {

        NewsItem newsItem = data.get(position);

        if(newsItem.title == null){
            callback.requestNews(position);
            return;
        }

        holder.no.setText(String.valueOf(position+1)+".");
        holder.title.setText(newsItem.title);

        String host = getHostName(newsItem.url);
        if(host.equals("")){
            holder.source.setVisibility(View.GONE);
        }else{
            holder.source.setVisibility(View.VISIBLE);
            holder.source.setText(" ("+host+")");
        }

        String points = "point";
        if(newsItem.score > 1){
            points += "s";
        }

        holder.point.setText(newsItem.score+" "+points);
        holder.author.setText(" by "+newsItem.by);

        CharSequence value = Util.getPrettyTime(newsItem.time);
        holder.time.setText(" "+value);

        String comments;
        if(newsItem.kids == null){
            comments = "0 comment";
        }else if(newsItem.kids.length >1){
            comments = newsItem.kids.length + " comments";
        }else{
            comments = newsItem.kids.length + " comment";
        }

        holder.comment.setText(" | "+comments);

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                callback.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    private static String getHostName(String url) {
        return Util.getHostname(url);
    }
}
