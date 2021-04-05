package com.mdp.fypapp.Model;

import java.util.HashMap;
import java.util.Map;

public class Result {
    private String userId;
    private String title;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private Map<String, String> answers = new HashMap<>();


    public Result(String userId, String title, String time, Map<String, String> answers) {
        this.userId = userId;
        this.title = title;
        this.time = time;
        this.answers = answers;
    }

    public Result() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String titlr) {
        this.title = titlr;
    }

    public Map<String, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, String> answers) {
        this.answers = answers;
    }
}
