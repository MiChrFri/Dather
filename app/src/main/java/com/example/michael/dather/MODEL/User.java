package com.example.michael.dather.MODEL;

/**
 * Created by michael on 06/06/16.
 */

public class User {
    String userId;
    String gmtOffset;
    String q1;
    String q2;
    String q3;
    String q4;
    String q5;
    String q6;

    public User(String userId, String gmtOffset) {
        this.userId = userId;
        this.gmtOffset = gmtOffset;
        this.q1 = "";
        this.q2 = "";
        this.q3 = "";
        this.q4 = "";
        this.q5 = "";
        this.q6 = "";
    }

    public void addAnswers(String q1, String q2, String q3, String q4, String q5, String q6) {
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
        this.q4 = q4;
        this.q5 = q5;
        this.q6 = q6;
    }

    //public User(String userId, String gmtOffset, ) {
}
