package com.example.scsebuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class FAQActivity extends AppCompatActivity {
    private int count = 0;
    TextView faqTextView;
    ImageView faqImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        faqTextView = this.findViewById(R.id.faqTextView);
        faqImageView = this.findViewById(R.id.faqImageView);

        faqTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                if (count > 15) {
                    faqImageView.setVisibility(View.VISIBLE);

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // this code will be executed after 2 seconds
                            count = 0;
                            faqImageView.setVisibility(View.INVISIBLE);
                        }
                    }, 2000);
                }
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