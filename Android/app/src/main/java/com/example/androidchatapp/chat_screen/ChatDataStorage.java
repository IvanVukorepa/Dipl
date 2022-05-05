package com.example.androidchatapp.chat_screen;

import android.content.Context;
import android.util.Log;

import com.example.androidchatapp.Services.ChatService;
import com.example.androidchatapp.Services.Message;
import com.example.androidchatapp.Services.PubSubData;

import java.util.ArrayList;

public class ChatDataStorage {

    public static ArrayList<Message> messages = new ArrayList<>();

    public static void fillData(final Context context, final chatAdapter adapter){

    }

    public static void addMessage(Context context, final PubSubData data, chatAdapter adapter){

        Log.e("a", ChatService.chatName);
        Log.e("a", data.group);

        if (ChatService.chatName.equals(data.group)){
            messages.add(data.data);
            adapter.notifyDataSetChanged();
        } else{
            //show notification as we are in a different chat
            ChatService.showNotification(context);
        }
    }
}
