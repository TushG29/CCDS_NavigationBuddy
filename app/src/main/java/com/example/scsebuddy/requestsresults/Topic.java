package com.example.scsebuddy.requestsresults;

public class Topic {
    private String datePublished;
    private String title;
    private String description;
    private int id;
    private int numberOfPosts;

    public int getNumberOfPosts() {
        return numberOfPosts;
    }

    private String userEmail;

    public String getDatePublished() {
        return datePublished;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
