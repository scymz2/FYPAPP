package com.mdp.fypapp.Model;

import java.util.HashMap;
import java.util.Map;

public class Quiz {

    private String id = "";
    private String title = "";
    private Map<String, Question> questions = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Map<String, Question> questions) {
        this.questions = questions;
    }

    public Quiz() {
    }

    public Quiz(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public Quiz(String id, String title, Map<String, Question> questions) {
        this.id = id;
        this.title = title;
        this.questions = questions;
    }
}
