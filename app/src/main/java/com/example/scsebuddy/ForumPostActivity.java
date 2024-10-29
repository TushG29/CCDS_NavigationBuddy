package com.example.scsebuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.scsebuddy.requestsresults.ConstantVariables;
import com.example.scsebuddy.requestsresults.Location;
import com.example.scsebuddy.requestsresults.PathResult;
import com.example.scsebuddy.requestsresults.RetrofitInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForumPostActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    Spinner topicSpinner;
    CheckBox annoymousCb;
    EditText contentEditText, titleEditText;
    AutoCompleteTextView tagSearchTextView;
    private String[] mapName;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_post);

        topicSpinner = this.findViewById(R.id.topicSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.topic_by_spinner, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        topicSpinner.setAdapter(adapter);
        context = this;
        retrofit = new Retrofit.Builder().baseUrl(ConstantVariables.getSERVER_URL()).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        contentEditText = this.findViewById(R.id.contentEditText);
        titleEditText = this.findViewById(R.id.titleEditText);
        annoymousCb = this.findViewById(R.id.annoymousCb);
        tagSearchTextView = findViewById(R.id.postTagSearchTextView);

        Intent ii = getIntent();
        Bundle b = ii.getExtras();
        if (b != null) {
            selectValue(topicSpinner, b.get("forumTopic") + "");
        }
        loadData();
    }

    private void loadData() {
        retrofit = new Retrofit.Builder().baseUrl(ConstantVariables.getSERVER_URL()).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<PathResult> getAllPath = retrofitInterface.executeGetAllPath();

        getAllPath.enqueue(new Callback<PathResult>() {
            @Override
            public void onResponse(Call<PathResult> call, Response<PathResult> response) {
                if (response.code() == 200) {
                    PathResult pathR = response.body();
                    ArrayList<Location> locations = new ArrayList<>(Arrays.asList(pathR.getLocations()));
                    mapName = new String[locations.size()];
                    for (int i = 0; i < locations.size(); i++) {
                        Location location = locations.get(i);
                        mapName[i] = location.getName();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, mapName);
                    tagSearchTextView.setAdapter(adapter);


                } else if (response.code() == 404) {
                    Log.e("HHH", "No such start/destination");
                }
            }

            @Override
            public void onFailure(Call<PathResult> call, Throwable t) {
            }
        });
    }

    public void postAddTag(View v) {
        String textSearch = tagSearchTextView.getText().toString();
        boolean isValid = false;
        for (String mapName1 : mapName) {
            if (tagSearchTextView.getText().toString().equals(mapName1))
                isValid = true;
        }
        if (isValid) {
            LinearLayout tagsLayout = findViewById(R.id.postTagsLayout);

            Button newTagButton = new Button(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            newTagButton.setLayoutParams(params);
            newTagButton.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_button_black));
            newTagButton.setTextColor(ContextCompat.getColor(context, R.color.white));
            newTagButton.setAllCaps(false);
            newTagButton.setTextSize(13);
            newTagButton.setText(textSearch);
            newTagButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ForumPostActivity.this);
                    builder1.setMessage("Do you want to delete this button?");
                    builder1.setCancelable(true);


                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    newTagButton.setVisibility(View.GONE);
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            });
            tagsLayout.addView(newTagButton);
            FrameLayout frameLayout = new FrameLayout(this);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(5,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            frameLayout.setLayoutParams(params1);
            tagsLayout.addView(frameLayout);
        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(ForumPostActivity.this);
            builder1.setMessage("Location does not exist.");
            builder1.setCancelable(true);
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }

    }

    public void addForumPost(View v) {
        if (!(titleEditText.getText().toString().isEmpty()) && !(contentEditText.getText().toString().isEmpty())) {
            SharedPreferences sp = getSharedPreferences("UserPreferences", Context.MODE_WORLD_READABLE);
            String email = sp.getString("USER_EMAIL", "");
            int noOfPosts = sp.getInt("noOfPosts", 0) + 1;

            if (annoymousCb.isChecked()) {
                email = "Anonymous";
            }


            String tagsString = "";
            LinearLayout tagsLayout = findViewById(R.id.postTagsLayout);
            int n = tagsLayout.getChildCount();
            int cnt = 0;
            for (int i = 0; i < n; i++) {
                if (tagsLayout.getChildAt(i) instanceof Button) {
                    Button tag = (Button) tagsLayout.getChildAt(i);
                    String tagString = tag.getText().toString();
                    if (cnt == 0) tagsString += tagString;
                    else {
                        tagsString += "," + tagString;
                    }
                    cnt++;
                }
            }

            Calendar calendar = Calendar.getInstance();
            Date date = (Date) calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            HashMap<String, String> map = new HashMap<>();
            map.put("email", email);
            map.put("title", titleEditText.getText() + "");
            map.put("topic", topicSpinner.getSelectedItem().toString());
            map.put("dateTime", sdf.format(date));
            map.put("content", contentEditText.getText() + "");
            map.put("noOfPosts", noOfPosts + "");
            map.put("tags", tagsString);

            Call<Void> call = retrofitInterface.executeForumPost(map);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200) {
                        Toast.makeText(ForumPostActivity.this, "Posted Successfully!", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(context, ForumViewActivity.class);
                        i.putExtra("topicTitle", topicSpinner.getSelectedItem().toString() + "");
                        setResult(Activity.RESULT_OK, i);
                        finish();
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (response.code() == 400) {
                        Toast.makeText(ForumPostActivity.this, "Wrong Credentials!", Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(ForumPostActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(ForumPostActivity.this);
            builder1.setMessage("Post requires title and descriptions to be filled.");
            builder1.setCancelable(true);
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }

    }

    private void selectValue(Spinner spinner, Object value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}