package com.example.androidchatapp.main_screen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.androidchatapp.R;
import com.example.androidchatapp.Services.AuthTokenService;
import com.example.androidchatapp.Services.ChatService;
import com.example.androidchatapp.Services.TestService;
import com.example.androidchatapp.chat_screen.ChatActivity;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ChatsListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        listView = (ListView) findViewById(R.id.mainScreenList);
        adapter = new ChatsListAdapter(getApplicationContext());
        ChatListDataStorage.fillData(getApplicationContext(), adapter);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String chatName = ChatListDataStorage.chats.get(i);
                ChatService.chatName = chatName;
                //add chatname to intent and set it in next activity, on destroy remove name and clear datastorage
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("chatName", chatName);
                startActivity(intent);
            }
        });

        final String username = AuthTokenService.getPayloadData("username");
        Log.i("error", username);

        // should probably be in Test/WebPubSub Service code
        ChatService.rejoinGroups(getApplicationContext(), username);

        Intent serviceIntent = new Intent(this, TestService.class);
        serviceIntent.putExtra("message", "");
        Log.e("service", "intent start service WebPubSubConService");
        startService(serviceIntent);
    }
}