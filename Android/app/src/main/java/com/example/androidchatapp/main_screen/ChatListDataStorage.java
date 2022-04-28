package com.example.androidchatapp.main_screen;

import android.content.Context;

import com.example.androidchatapp.Services.AuthTokenService;
import com.example.androidchatapp.Services.ChatService;

import java.util.ArrayList;

public class ChatListDataStorage {

    public static ArrayList<String> chats = new ArrayList<>();

    public static void fillData(final Context context, final ChatsListAdapter adapter){
        ChatService.getAllGroupsForUser(context, AuthTokenService.getPayloadData("username"), adapter);
    }
}
