package com.example.scsebuddy.dynamicdesign;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scsebuddy.MapActivity;
import com.example.scsebuddy.R;
import com.example.scsebuddy.requestsresults.CourseReview;

import java.util.ArrayList;

public class CourseReview_RecyclerViewAdapter extends RecyclerView.Adapter<CourseReview_RecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<CourseReview> reviews;

    public CourseReview_RecyclerViewAdapter(Context context, ArrayList<CourseReview> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public CourseReview_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.course_review_row, parent, false);
        return new CourseReview_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseReview_RecyclerViewAdapter.MyViewHolder holder, int position) {
        Log.e("TEST", "IM HERE?");
        holder.courseReviewTextView.setText(reviews.get(position).getDescription());
        holder.postByTextView.setText("Post By: " + reviews.get(position).getName());
        holder.datePostedTextView.setText("Date Posted: " + reviews.get(position).getDatePublished());
        holder.gradeTextView.setText("Grade: " + reviews.get(position).getGrade());
        for (int i = 0; i < reviews.get(position).getTags().size(); i++) {
            if (reviews.get(position).getTags().get(i).equals("")) continue;
            Button tag = new Button(context);
            tag.setText(reviews.get(position).getTags().get(i));
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
            holder.courseRowLayout.addView(tag);
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView courseReviewTextView, postByTextView, datePostedTextView, gradeTextView;
        LinearLayout courseRowLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            courseReviewTextView = itemView.findViewById(R.id.courseReviewTextView);
            postByTextView = itemView.findViewById(R.id.postByTextView);
            datePostedTextView = itemView.findViewById(R.id.datePostedTextView);
            gradeTextView = itemView.findViewById(R.id.gradeTextView);
            courseRowLayout = itemView.findViewById(R.id.courseRowLayout);
        }
    }
}