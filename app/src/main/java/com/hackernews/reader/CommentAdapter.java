package com.hackernews.reader;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hackernews.reader.util.Util;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;

/**
 * Created by HJ Chin on 13/11/2017.
 *
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    public CommentItem getItemAt(int i) {
        return comment.get(i);
    }

    interface Callback{
        void requestComment(int position);
        void requestReply(int position);
    }

    private final ArrayList<CommentItem> comment;
    private final Callback callback;

    CommentAdapter(ArrayList<CommentItem> comment, Callback callback){
        this.comment = comment;
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent,false);
        view.findViewById(R.id.indicator).setVisibility(View.INVISIBLE);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        CommentItem commentItem = comment.get(position);

        if(commentItem.by == null){
            callback.requestComment(position);
            return;
        }

        holder.itemView.findViewById(R.id.indicator).setVisibility(View.VISIBLE);

        holder.by.setText(commentItem.by);
        holder.time.setText(Util.getPrettyTime(commentItem.time));

        //Log.i(AppLog.LOG_TAG,commentItem.text);
        if(commentItem.text != null)
            holder.comment.setHtml(commentItem.text);
        else
            holder.comment.setHtml("");

        if(commentItem.kids == null || commentItem.kids.length == 0){
            holder.viewKid.setVisibility(View.GONE);
        }else{
            holder.viewKid.setVisibility(View.VISIBLE);
            if(commentItem.kids.length > 1){
                holder.viewKid.setText("View "+ commentItem.kids.length+" replies");
            }else{
                holder.viewKid.setText("View 1 reply");
            }

            holder.viewKid.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    callback.requestReply(holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return comment.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        final TextView by;
        final TextView time;
        final HtmlTextView comment;
        final TextView viewKid;
        final View itemView;

        ViewHolder(View itemView) {
            super(itemView);
            by = (TextView)itemView.findViewWithTag("by");
            time = (TextView)itemView.findViewWithTag("time");
            comment = (HtmlTextView)itemView.findViewWithTag("comment");
            viewKid = (TextView)itemView.findViewWithTag("kid");
            this.itemView = itemView;
        }
    }
}
