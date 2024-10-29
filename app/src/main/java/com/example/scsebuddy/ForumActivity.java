package com.example.scsebuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scsebuddy.dynamicdesign.ForumPost_RecyclerViewAdapter;
import com.example.scsebuddy.dynamicdesign.Topics_RecyclerViewAdapter;
import com.example.scsebuddy.requestsresults.ConstantVariables;
import com.example.scsebuddy.requestsresults.ForumPost;
import com.example.scsebuddy.requestsresults.ForumPostResult;
import com.example.scsebuddy.requestsresults.RetrofitInterface;
import com.example.scsebuddy.requestsresults.Topic;
import com.example.scsebuddy.requestsresults.TopicsResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForumActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    EditText txtSearchForum;
    Context context;
    Spinner sortOrderSpinner, sortBySpinner;

    protected void onRestart() {
        super.onRestart();
        Intent i = new Intent(this, ForumActivity.class);
        startActivity(i);
        //finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        txtSearchForum = this.findViewById(R.id.txtSearchForum);

        sortOrderSpinner = this.findViewById(R.id.sortOrderSpinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.sorting_order_spinner_content, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        sortOrderSpinner.setAdapter(adapter1);

        sortBySpinner = this.findViewById(R.id.sortTopicBySpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.sorting_topic_by_spinner_content, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        sortBySpinner.setAdapter(adapter2);

        context = this;
        retrofit = new Retrofit.Builder().baseUrl(ConstantVariables.getSERVER_URL()).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        //default loading
        HashMap<String, String> map = new HashMap<>();
        map.put("orderBy", "asc");
        map.put("sortBy", "title");
        updateRV(map);
    }

    public void sortByButton(View v) {
        String orderBy = sortOrderSpinner.getSelectedItem().toString();
        String sortBy = sortBySpinner.getSelectedItem().toString();
        switch (sortBy) {
            case "Topic":
                sortBy = "Title";
                break;
            case "Count":
                sortBy = "No_Of_Posts";
                break;
        }
        // if forum search, use different updateRV()

        HashMap<String, String> map = new HashMap<>();
        map.put("orderBy", orderBy);
        map.put("sortBy", sortBy);
        updateRV(map);
    }

    public void forumSearch(View v) {

        retrofit = new Retrofit.Builder().baseUrl(ConstantVariables.getSERVER_URL()).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        HashMap<String, String> map = new HashMap<>();
        String forumSearch = txtSearchForum.getText().toString();
        map.put("forumSearch", forumSearch);

        TextView forumSearchMessageTextView = findViewById(R.id.forumSearchMessageTextViews);
        Spanned styledText = Html.fromHtml(getString(R.string.searched_by, forumSearch), Html.FROM_HTML_MODE_LEGACY);
        forumSearchMessageTextView.setText(styledText);

        Call<ForumPostResult> executeSearchPost = retrofitInterface.executeSearchPost(map);

        executeSearchPost.enqueue(new Callback<ForumPostResult>() {
            @Override
            public void onResponse(Call<ForumPostResult> call, Response<ForumPostResult> response) {
                if (response.code() == 200) {

                    ForumPostResult forumR = response.body();
                    ArrayList<ForumPost> posts = new ArrayList<>(Arrays.asList(forumR.getForumPost()));
                    RecyclerView forumPostRecyclerView = findViewById(R.id.topicsRecycleView);
                    forumPostRecyclerView.setVisibility(View.VISIBLE);

                    ForumPost_RecyclerViewAdapter adapter = new ForumPost_RecyclerViewAdapter(context, posts);
                    forumPostRecyclerView.setAdapter(adapter);
                    forumPostRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                } else if (response.code() == 404) {
                    Toast.makeText(ForumActivity.this, "No Data", Toast.LENGTH_LONG).show();

                    RecyclerView forumPostRecyclerView = findViewById(R.id.topicsRecycleView);
                    forumPostRecyclerView.setVisibility(View.GONE);
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ForumActivity.this);
                    builder1.setMessage("No search result.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }

            @Override
            public void onFailure(Call<ForumPostResult> call, Throwable t) {
                Toast.makeText(ForumActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void updateRV(HashMap map) {
        Call<TopicsResult> getAllTopics = retrofitInterface.executeAllTopics(map);

        getAllTopics.enqueue(new Callback<TopicsResult>() {
            @Override
            public void onResponse(Call<TopicsResult> call, Response<TopicsResult> response) {
                if (response.code() == 200) {
                    TopicsResult topicR = response.body();

                    ArrayList<Topic> topics = new ArrayList<>(Arrays.asList(topicR.getTopics()));

                    RecyclerView topicsRecyclerView = findViewById(R.id.topicsRecycleView);

                    Topics_RecyclerViewAdapter adapter = new Topics_RecyclerViewAdapter(context, topics);
                    topicsRecyclerView.setAdapter(adapter);
                    topicsRecyclerView.setLayoutManager(new LinearLayoutManager(context));

                } else if (response.code() == 404) {
                    Toast.makeText(ForumActivity.this, "No Data", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TopicsResult> call, Throwable t) {
                Toast.makeText(ForumActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    //Bottom buttons
    public void mapScreen(View v) {
        Intent intent = new Intent(v.getContext(), MapActivity.class);
        startActivity(intent);
    }

    public void courseScreen(View v) {
        Intent intent = new Intent(v.getContext(), CourseActivity.class);
        startActivity(intent);
    }

    public void forumScreen(View v) {
        Intent intent = new Intent(v.getContext(), ForumActivity.class);
        startActivity(intent);
    }

    public void profileScreen(View v) {
        Intent intent = new Intent(v.getContext(), ProfileActivity.class);
        startActivity(intent);
    }
}