package com.example.scsebuddy.dynamicdesign;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scsebuddy.R;
import com.example.scsebuddy.requestsresults.ForumComment;

import java.util.ArrayList;

public class ForumComment_RecyclerViewAdapter extends RecyclerView.Adapter<ForumComment_RecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<ForumComment> forumComments;

    public ForumComment_RecyclerViewAdapter(Context context, ArrayList<ForumComment> forumComments) {
        this.context = context;
        this.forumComments = forumComments;
    }

    @NonNull
    @Override
    public ForumComment_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.form_post_row, parent, false);
        return new ForumComment_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForumComment_RecyclerViewAdapter.MyViewHolder holder, int position) {

        holder.postByTextView.setText("Post By: " + forumComments.get(position).getName());
        holder.forumPostTextView.setText(forumComments.get(position).getDescription());
        holder.datePostedTextView.setText("Date Posted: " + forumComments.get(position).getDatePublished());
    }

    @Override
    public int getItemCount() {
        return forumComments.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView topicTitleTextView, postByTextView, datePostedTextView, forumPostTextView;
        LinearLayout postRowLayout;
        ImageView btnForumLike;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            topicTitleTextView = itemView.findViewById(R.id.topicTitleTextView);
            postByTextView = itemView.findViewById(R.id.postByTextView);
            datePostedTextView = itemView.findViewById(R.id.datePostedTextView);
            forumPostTextView = itemView.findViewById(R.id.forumPostTextView);
            postRowLayout = itemView.findViewById(R.id.postRowLayout);
            btnForumLike = itemView.findViewById(R.id.btnForumLike);
            btnForumLike.setVisibility(View.GONE);
            topicTitleTextView.setVisibility(View.GONE);
            postRowLayout.setVisibility(View.GONE);
        }
    }
}
