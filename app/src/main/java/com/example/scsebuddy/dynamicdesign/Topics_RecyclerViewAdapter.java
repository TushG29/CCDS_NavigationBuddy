package com.example.scsebuddy.dynamicdesign;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scsebuddy.ForumViewActivity;
import com.example.scsebuddy.R;
import com.example.scsebuddy.requestsresults.Topic;

import java.util.ArrayList;

public class Topics_RecyclerViewAdapter extends RecyclerView.Adapter<Topics_RecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<Topic> topics;
    static Context context1;

    public Topics_RecyclerViewAdapter(Context context, ArrayList<Topic> topics) {
        this.context = context;
        this.topics = topics;
    }

    @NonNull
    @Override
    public Topics_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.topic_row, parent, false);
        return new Topics_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Topics_RecyclerViewAdapter.MyViewHolder holder, int position) {

        holder.topicDescriptionTextView.setText(topics.get(position).getDescription());
        int numberOfPosts = topics.get(position).getNumberOfPosts();
        holder.topicNoOfPostsTextView.setText(String.valueOf(numberOfPosts));
        holder.topicTitleTextView.setText(topics.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView topicTitleTextView, topicDescriptionTextView, topicNoOfPostsTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            topicTitleTextView = itemView.findViewById(R.id.topicTitleTextView);
            topicDescriptionTextView = itemView.findViewById(R.id.topicDescriptionTextView);
            topicNoOfPostsTextView = itemView.findViewById(R.id.topicNoOfPostsTextView);
            context1 = itemView.getContext();
        }

        @Override
        public void onClick(View view) {
            final Intent intent;
            intent = new Intent(context1, ForumViewActivity.class);
            intent.putExtra("topicTitle", topicTitleTextView.getText().toString());
            intent.putExtra("noOfPosts", topicNoOfPostsTextView.getText().toString());
            Log.e("HELLO", topicTitleTextView.getText().toString());
            context1.startActivity(intent);
        }
    }
}
