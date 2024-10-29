package com.example.scsebuddy.requestsresults;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.*;

public interface RetrofitInterface {

    //Everything about Account
    @POST("/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @POST("/signup")
    Call<Void> executeSignup(@Body HashMap<String, String> map);

    @POST("/changePW")
    Call<Void> executeChangePW(@Body HashMap<String, String> map);

    @POST("/verificationCode")
    Call<Void> executeCode(@Body HashMap<String, String> map);

    @POST("/changeAccDetails")
    Call<Void> executeChangeAccDetails(@Body HashMap<String, String> map);

    //Everything about Courses
    @POST("/allCourses")
    Call<CoursesResult> executeAllCourses(@Body HashMap<String, String> map);

    @POST("/allCourseReview")
    Call<CourseReviewResult> executeAllCourseReview(@Body HashMap<String, String> map);

    @POST("/courseFav")
    Call<Void> executeCourseFav(@Body HashMap<String, String> map);

    @POST("/coursePost")
    Call<Void> executeCoursePost(@Body HashMap<String, String> map);

    @POST("/searchCourses")
    Call<CoursesResult> executeSearchCourses(@Body HashMap<String, String> map);

    //not done dk how do
    @POST("/sortCourseYear")
    Call<CoursesResult> executeSortCourseYear(@Body HashMap<String, String> map);


    //Everything About Forum
    @POST("/allTopics")
    Call<TopicsResult> executeAllTopics(@Body HashMap<String, String> map);

    @POST("/forumPost")
    Call<Void> executeForumPost(@Body HashMap<String, String> map);

    @POST("/allForumPost")
    Call<ForumPostResult> executeAllForumPost(@Body HashMap<String, String> map);


    @POST("/searchPost")
    Call<ForumPostResult> executeSearchPost(@Body HashMap<String, String> map);

    @POST("/forumViewComment")
    Call<ForumCommentResult> executeForumComment(@Body HashMap<String, String> map);

    @POST("/forumCommentPost")
    Call<Void> executeForumCommentPost(@Body HashMap<String, String> map);

    @POST("/forumPostLike")
    Call<Void> executeForumPostLike(@Body HashMap<String, String> map);

    // Map

    @POST("/getPath")
    Call<PathResult> executeGetPath(@Body HashMap<String, String> map);

    @GET("/getAllPath")
    Call<PathResult> executeGetAllPath();

}
