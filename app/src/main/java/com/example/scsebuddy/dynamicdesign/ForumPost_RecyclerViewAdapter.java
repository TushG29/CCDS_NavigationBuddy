package com.example.scsebuddy.dynamicdesign;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scsebuddy.ForumViewCommentsActivity;
import com.example.scsebuddy.MapActivity;
import com.example.scsebuddy.R;
import com.example.scsebuddy.requestsresults.ConstantVariables;
import com.example.scsebuddy.requestsresults.ForumPost;
import com.example.scsebuddy.requestsresults.RetrofitInterface;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForumPost_RecyclerViewAdapter extends RecyclerView.Adapter<ForumPost_RecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<ForumPost> posts;
    static Context context1;
    int btnLike = 0;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    public ForumPost_RecyclerViewAdapter(Context context, ArrayList<ForumPost> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ForumPost_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.form_post_row, parent, false);
        return new ForumPost_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForumPost_RecyclerViewAdapter.MyViewHolder holder, int position) {
        Log.e("TEST", "IM HERE?");

        holder.topicTitleTextView.setText(posts.get(position).getTitle());
        holder.postByTextView.setText("Post By: " + posts.get(position).getName());
        holder.datePostedTextView.setText("Date Posted: " + posts.get(position).getDatePublished());
        holder.forumPostTextView.setText(posts.get(position).getDescription());
        for (int i = 0; i < posts.get(position).getTags().size(); i++) {
            if (posts.get(position).getTags().get(i).equals("")) continue;
            Button tag = new Button(context);
            tag.setText(posts.get(position).getTags().get(i));
            tag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MapActivity.class);
                    Button tag = (Button) view;
                    String tagLocation = tag.getText().toString();
                    intent.putExtra("mapDestination", tagLocation);

                    context.startActivity(intent);
                }
            });
            holder.postRowLayout.addView(tag);
        }
        if (posts.get(position).getFavorite() == 1) {
            holder.btnForumLike.setImageResource(R.drawable.thumb_up_24_red);
            btnLike = 1;
        } else {
            holder.btnForumLike.setImageResource(R.drawable.thumb_up_24);
            btnLike = 0;
        }
        holder.iDTextView.setText(posts.get(position).getID() + "");

        holder.btnForumLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrofit = new Retrofit.Builder().baseUrl(ConstantVariables.getSERVER_URL()).addConverterFactory(GsonConverterFactory.create()).build();
                retrofitInterface = retrofit.create(RetrofitInterface.class);
                HashMap<String, String> map = new HashMap<>();
                if (btnLike == 1) {
                    btnLike = 0;
                } else {
                    btnLike = 1;
                }
                SharedPreferences sp = context.getSharedPreferences("UserPreferences", Context.MODE_WORLD_READABLE);
                String email = sp.getString("USER_EMAIL", "");

                map.put("postFav", btnLike + "");
                map.put("postID", holder.iDTextView.getText() + "");
                map.put("email", email);
                Call<Void> call = retrofitInterface.executeForumPostLike(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200) {
                            if (btnLike == 1) {
                                holder.btnForumLike.setImageResource(R.drawable.thumb_up_24_red);
                            } else {
                                holder.btnForumLike.setImageResource(R.drawable.thumb_up_24);
                            }
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else if (response.code() == 400) {
                            if (btnLike == 1) {
                                btnLike = 0;
                            } else {
                                btnLike = 1;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView topicTitleTextView, postByTextView, datePostedTextView, forumPostTextView, iDTextView;
        LinearLayout postRowLayout;
        ImageView btnForumLike;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            context1 = itemView.getContext();
            topicTitleTextView = itemView.findViewById(R.id.topicTitleTextView);
            postByTextView = itemView.findViewById(R.id.postByTextView);
            datePostedTextView = itemView.findViewById(R.id.datePostedTextView);
            forumPostTextView = itemView.findViewById(R.id.forumPostTextView);
            postRowLayout = itemView.findViewById(R.id.postRowLayout);
            btnForumLike = itemView.findViewById(R.id.btnForumLike);
            iDTextView = itemView.findViewById(R.id.iDTextView);
        }

        @Override
        public void onClick(View view) {
            final Intent intent;
            Log.e("TEST", "HERE?");
            intent = new Intent(context1, ForumViewCommentsActivity.class);
            intent.putExtra("postID", iDTextView.getText() + "");
            intent.putExtra("forumPost", forumPostTextView.getText() + "");
            intent.putExtra("datePosted", datePostedTextView.getText() + "");
            intent.putExtra("postBy", postByTextView.getText() + "");
            intent.putExtra("topicTitle", topicTitleTextView.getText() + "");
            context1.startActivity(intent);
        }
    }
}
