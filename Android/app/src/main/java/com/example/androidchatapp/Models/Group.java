package com.example.androidchatapp.Models;

public class Group {

    private int id;
    private String groupName;
    private String userName;
    private String date;

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getUserName() {
        return userName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
