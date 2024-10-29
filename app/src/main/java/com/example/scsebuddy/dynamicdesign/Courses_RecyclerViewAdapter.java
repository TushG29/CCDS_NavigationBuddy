package com.example.scsebuddy.dynamicdesign;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scsebuddy.CourseViewActivity;
import com.example.scsebuddy.R;
import com.example.scsebuddy.requestsresults.ConstantVariables;
import com.example.scsebuddy.requestsresults.Course;
import com.example.scsebuddy.requestsresults.RetrofitInterface;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Courses_RecyclerViewAdapter extends RecyclerView.Adapter<Courses_RecyclerViewAdapter.MyViewHolder> {
    Context context;
    static Context context1;
    ArrayList<Course> courses;
    int fav = 0;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    public Courses_RecyclerViewAdapter(Context context, ArrayList<Course> courses) {
        this.context = context;
        this.courses = courses;
    }

    @NonNull
    @Override
    public Courses_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.course_row, parent, false);
        return new Courses_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Courses_RecyclerViewAdapter.MyViewHolder holder, int position) {

        holder.ausTextView.setText(courses.get(position).getAUs() + " AUs");
        holder.courseCodeTextView.setText(courses.get(position).getCode());
        holder.courseTitleTextView.setText(courses.get(position).getTitle());
        fav = courses.get(position).getFavorite();
        if (fav == 0) {
            holder.favouriteImageView.setImageResource(R.drawable.ic_course_bookmark_outline);
            holder.courseFavTextView.setText(fav + "");
        } else {
            holder.favouriteImageView.setImageResource(R.drawable.ic_course_bookmark_yellow);
            holder.courseFavTextView.setText(fav + "");
        }
        holder.favouriteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrofit = new Retrofit.Builder().baseUrl(ConstantVariables.getSERVER_URL()).addConverterFactory(GsonConverterFactory.create()).build();
                retrofitInterface = retrofit.create(RetrofitInterface.class);

                HashMap<String, String> map = new HashMap<>();
                if (fav == 1) {
                    fav = 0;
                } else {
                    fav = 1;
                }
                SharedPreferences sp = context.getSharedPreferences("UserPreferences", Context.MODE_WORLD_READABLE);
                String email = sp.getString("USER_EMAIL", "");

                map.put("courseFavID", holder.courseCodeTextView.getText() + "");
                map.put("courseFav", fav + "");
                map.put("email", email);
                Call<Void> call = retrofitInterface.executeCourseFav(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200) {
                            Toast.makeText(context, "Favorite Updated Successfully!", Toast.LENGTH_LONG).show();
                            if (fav == 1) {
                                holder.favouriteImageView.setImageResource(R.drawable.ic_course_bookmark_yellow);
                            } else {
                                holder.favouriteImageView.setImageResource(R.drawable.ic_course_bookmark_outline);
                            }
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else if (response.code() == 400) {
                            Toast.makeText(context, "Wrong Credentials!", Toast.LENGTH_LONG).show();
                            if (fav == 1) {
                                fav = 0;
                            } else {
                                fav = 1;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView courseCodeTextView, courseTitleTextView, ausTextView, courseFavTextView;
        ImageView favouriteImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            context1 = itemView.getContext();

            favouriteImageView = itemView.findViewById(R.id.favouriteImageView);
            courseCodeTextView = itemView.findViewById(R.id.courseCodeTextView);
            courseTitleTextView = itemView.findViewById(R.id.courseTitleTextView);
            courseFavTextView = itemView.findViewById(R.id.courseFavTextView);
            ausTextView = itemView.findViewById(R.id.ausTextView);
        }

        @Override
        public void onClick(View view) {
            final Intent intent;
            intent = new Intent(context1, CourseViewActivity.class);
            intent.putExtra("courseCode", courseCodeTextView.getText());
            intent.putExtra("courseTitle", courseTitleTextView.getText());
            intent.putExtra("courseFav", courseFavTextView.getText());
            context1.startActivity(intent);
        }
    }
}
