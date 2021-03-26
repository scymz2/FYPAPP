package com.mdp.fypapp.Model;

import android.renderscript.Type;

import java.util.Date;

public class Chat {

    private String message;
    private Date date;
    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Chat(String message, Date date, Type type) {
        this.message = message;
        this.date = date;
        this.type = type;
    }

    public Chat(){}


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public enum Type {
        INCOUNT, OUTCOUNT
    }
}
