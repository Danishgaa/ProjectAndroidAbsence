package com.example.aplikasiabsensi.model;

//import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

public class User implements Serializable{

    private String key;
    private String email;
    private String status;

    public User(){

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return " "+email+"\n" +
                " "+status;
    }

    public User(String emaill, String statuss){
        email = emaill;
        status = statuss;
    }
}