package com.example.androidchatapp.Services;

public class EventBusMessages {

    public class SendMessageEvent{
        public final String message;

        public SendMessageEvent(String mess){
            message = mess;
        }
    }

    public class ReceiveMessageEvent{
        public final String message;

        public ReceiveMessageEvent(String mess){
            message = mess;
        }
    }
}
