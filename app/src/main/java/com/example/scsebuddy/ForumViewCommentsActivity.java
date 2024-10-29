package com.example.scsebuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scsebuddy.dynamicdesign.ForumComment_RecyclerViewAdapter;
import com.example.scsebuddy.requestsresults.ConstantVariables;
import com.example.scsebuddy.requestsresults.ForumComment;
import com.example.scsebuddy.requestsresults.ForumCommentResult;
import com.example.scsebuddy.requestsresults.RetrofitInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForumViewCommentsActivity extends AppCompatActivity {
    TextView titleTextView, postByTextView, contentTextView;
    private int postID;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    FloatingActionButton mAddFab;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_view_comments);

        context = this;
        retrofit = new Retrofit.Builder().baseUrl(ConstantVariables.getSERVER_URL()).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        Log.e("TEST", "I CREATED");
        titleTextView = this.findViewById(R.id.titleTextView);
        postByTextView = this.findViewById(R.id.postByTextView);
        contentTextView = this.findViewById(R.id.contentTextView);
        mAddFab = findViewById(R.id.add_fab);
        contentTextView.setMovementMethod(new ScrollingMovementMethod());
        Intent ii = getIntent();
        Bundle b = ii.getExtras();
        if (b != null) {
            titleTextView.setText(b.get("topicTitle") + "");
            postByTextView.setText(b.get("postBy") + "");
            contentTextView.setText(b.get("forumPost") + "");
            postID = Integer.parseInt(b.get("postID") + "");
        }

        SharedPreferences sp = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

        HashMap<String, String> map = new HashMap<>();
        map.put("postID", postID + "");
        map.put("email", sp.getString("USER_EMAIL", ""));

        Call<ForumCommentResult> executeForumComment = retrofitInterface.executeForumComment(map);

        executeForumComment.enqueue(new Callback<ForumCommentResult>() {
            @Override
            public void onResponse(Call<ForumCommentResult> call, Response<ForumCommentResult> response) {
                if (response.code() == 200) {
                    ForumCommentResult forumV = response.body();
                    ArrayList<ForumComment> comments = new ArrayList<>(Arrays.asList(forumV.getForumComments()));
                    RecyclerView forumCommentRecyclerView = findViewById(R.id.forumCommentRecyclerView);

                    ForumComment_RecyclerViewAdapter adapter = new ForumComment_RecyclerViewAdapter(context, comments);
                    forumCommentRecyclerView.setAdapter(adapter);
                    forumCommentRecyclerView.setLayoutManager(new LinearLayoutManager(context));


                } else if (response.code() == 404) {
                    Toast.makeText(ForumViewCommentsActivity.this, "No Data", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ForumCommentResult> call, Throwable t) {
                Toast.makeText(ForumViewCommentsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void addForumComment(View view) {
        Intent i = new Intent(this, ForumPostCommentActivity.class);
        Log.e("TEST", postID + " H");
        i.putExtra("postID", postID);
        i.putExtra("topicTitle", titleTextView.getText().toString());
        i.putExtra("forumPost", contentTextView.getText().toString());
        i.putExtra("postBy", postByTextView.getText().toString());
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle b = data.getExtras();
                if (b != null) {
                    titleTextView.setText(b.get("topicTitle") + "");
                    postByTextView.setText(b.get("postBy") + "");
                    contentTextView.setText(b.get("forumPost") + "");
                    postID = Integer.parseInt(b.get("postID") + "");
                }

                SharedPreferences sp = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

                HashMap<String, String> map = new HashMap<>();
                map.put("postID", postID + "");
                map.put("email", sp.getString("USER_EMAIL", ""));

                Call<ForumCommentResult> executeForumComment = retrofitInterface.executeForumComment(map);

                executeForumComment.enqueue(new Callback<ForumCommentResult>() {
                    @Override
                    public void onResponse(Call<ForumCommentResult> call, Response<ForumCommentResult> response) {
                        if (response.code() == 200) {
                            ForumCommentResult forumV = response.body();
                            ArrayList<ForumComment> comments = new ArrayList<>(Arrays.asList(forumV.getForumComments()));
                            RecyclerView forumCommentRecyclerView = findViewById(R.id.forumCommentRecyclerView);

                            ForumComment_RecyclerViewAdapter adapter = new ForumComment_RecyclerViewAdapter(context, comments);
                            forumCommentRecyclerView.setAdapter(adapter);
                            forumCommentRecyclerView.setLayoutManager(new LinearLayoutManager(context));


                        } else if (response.code() == 404) {
                            Toast.makeText(ForumViewCommentsActivity.this, "No Data", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ForumCommentResult> call, Throwable t) {
                        Toast.makeText(ForumViewCommentsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(ForumViewCommentsActivity.this, "Result Canceled!", Toast.LENGTH_LONG).show();
            }
        }
    } //onActivityResult
}