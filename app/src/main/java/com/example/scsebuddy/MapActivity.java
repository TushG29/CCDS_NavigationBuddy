package com.example.scsebuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.scsebuddy.dynamicdesign.MapDirection_RecyclerViewAdapter;
import com.example.scsebuddy.requestsresults.ConstantVariables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.scsebuddy.requestsresults.Direction;
import com.example.scsebuddy.requestsresults.Location;
import com.example.scsebuddy.requestsresults.PathResult;
import com.example.scsebuddy.requestsresults.RetrofitInterface;

public class MapActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String[] path;
    private String[] mapName;
    Context context;
    private AutoCompleteTextView txtStartSearch, txtEndSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        context = this;
        SharedPreferences sp = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

        String email = sp.getString("USER_EMAIL", null);
        txtStartSearch = this.findViewById(R.id.txtStartSearch);
        txtEndSearch = this.findViewById(R.id.txtEndSearch);

        //loadData();
        if (email == null) {
            this.findViewById(R.id.btnForum).setVisibility(View.GONE);
            this.findViewById(R.id.btnCourse).setVisibility(View.GONE);
            this.findViewById(R.id.btnMap).setVisibility(View.GONE);
            this.findViewById(R.id.btnProfile).setVisibility(View.GONE);

            LinearLayout mainPagesButtons = this.findViewById(R.id.mainPagesButtonsLayout);
            mainPagesButtons.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.5f);

            TextView noAccountTextView = new TextView(this);
            mainPagesButtons.addView(noAccountTextView);
            noAccountTextView.setText("You have not logged in you account!");
            noAccountTextView.setLayoutParams(param1);
            noAccountTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            noAccountTextView.setGravity(Gravity.CENTER);
            noAccountTextView.setTextColor(noAccountTextView.getContext().getColor(R.color.grey_text));

            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 2.5f);

            Button loginButton = new Button(this);
            mainPagesButtons.addView(loginButton);
            loginButton.setText("Log In");
            loginButton.getResources().getFont(R.font.poppins_medium);
            loginButton.setTextColor(loginButton.getContext().getColor(R.color.grey_text));
            loginButton.setLayoutParams(params2);
            loginButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            loginButton.setGravity(Gravity.CENTER);

            retrofit = new Retrofit.Builder().baseUrl(ConstantVariables.getSERVER_URL()).addConverterFactory(GsonConverterFactory.create()).build();
            retrofitInterface = retrofit.create(RetrofitInterface.class);

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
        loadData();
        Intent intent = getIntent();
        String destination = "";
        try {
            destination = intent.getStringExtra("mapDestination");
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
        txtEndSearch.setText(destination);
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
                    txtStartSearch.setAdapter(adapter);
                    txtEndSearch.setAdapter(adapter);


                } else if (response.code() == 404) {
                    Log.e("HHH", "No such start/destination");
                }
            }

            @Override
            public void onFailure(Call<PathResult> call, Throwable t) {
            }
        });
    }

    public void navigateButton(View v) {

        retrofit = new Retrofit.Builder().baseUrl(ConstantVariables.getSERVER_URL()).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        boolean startValid = false, endValid = false;
        for (int i = 0; i < mapName.length; i++) {
            if (txtStartSearch.getText().toString().equals(mapName[i])) {
                startValid = true;
                break;
            }
        }

        for (String mapName1 : mapName) {
            if (txtEndSearch.getText().toString().equals(mapName1)) {
                endValid = true;
                break;
            }

        }
        if (startValid && endValid) {
            String start = txtStartSearch.getText().toString();
            String destination = txtEndSearch.getText().toString();

            HashMap<String, String> map = new HashMap<>();
            map.put("start", start);
            map.put("destination", destination);
            Log.e("HHH", map.toString());

            Call<PathResult> getPath = retrofitInterface.executeGetPath(map);

            getPath.enqueue(new Callback<PathResult>() {
                @Override
                public void onResponse(Call<PathResult> call, Response<PathResult> response) {
                    if (response.code() == 200) {
                        PathResult pathR = response.body();
                        ArrayList<Location> locations = new ArrayList<>(Arrays.asList(pathR.getLocations()));
                        path = new String[locations.size()];
                        for (int i = 0; i < locations.size(); i++) {
                            Location location = locations.get(i);
                            System.out.println(location.getPhotoId());
                            path[i] = location.getPhotoId();
                        }

                        ArrayList<Direction> directions = new ArrayList<>(Arrays.asList(pathR.getDirections()));
                        RecyclerView mapDirectionRecycleView = findViewById(R.id.mapDirectionRecycleView);
                        mapDirectionRecycleView.setVisibility(View.VISIBLE);
                        MapDirection_RecyclerViewAdapter adapter = new MapDirection_RecyclerViewAdapter(context, directions);
                        mapDirectionRecycleView.setAdapter(adapter);
                        mapDirectionRecycleView.setLayoutManager(new LinearLayoutManager(context));
//                    }

                    } else if (response.code() == 404) {
                        Log.e("HHH", "No such start/destination");
                    }
                }

                @Override
                public void onFailure(Call<PathResult> call, Throwable t) {

                }
            });
        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(MapActivity.this);
            builder1.setMessage("Start or End location does not exist.");
            builder1.setCancelable(true);
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }


    }

    //Bottom buttons
    public void mapScreen(View v) {
        Intent intent = new Intent(v.getContext(), PanoramaViewActivity.class);
        intent.putExtra("path", path);
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