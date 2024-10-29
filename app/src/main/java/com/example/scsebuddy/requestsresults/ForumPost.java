package com.example.scsebuddy.requestsresults;

import java.util.ArrayList;

public class ForumPost {
    private String datePublished;
    private String title;
    private String description;
    private String name;
    private String topicID;
    private String email;
    private int ID;
    private ArrayList<String> tags;

    public int getID() {
        return ID;
    }

    public int getFavorite() {
        return favorite;
    }

    private int favorite;

    public String getEmail() {
        return email;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getTopicID() {
        return topicID;
    }

    public ArrayList<String> getTags() {
        return tags;
    }
}
