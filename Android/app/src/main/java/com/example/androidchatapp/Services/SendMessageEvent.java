package com.example.androidchatapp.Services;

public class SendMessageEvent{
    public final String message;

    public SendMessageEvent(String mess){
        message = mess;
    }
}