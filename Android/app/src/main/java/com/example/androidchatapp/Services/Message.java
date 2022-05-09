package com.example.androidchatapp.Services;

public class Message {
    public String user;
    public String message;
    public String time;

    public Message(String input){
        user = "test";
        message = input;
        time = "";
    }

    public Message(String User, String Content){
        user = User;
        message = Content;
        time = "";
    }
}
