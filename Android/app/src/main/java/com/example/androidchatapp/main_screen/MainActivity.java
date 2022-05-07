package com.example.androidchatapp.main_screen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.androidchatapp.R;
import com.example.androidchatapp.Services.AuthTokenService;
import com.example.androidchatapp.Services.ChatService;
import com.example.androidchatapp.Services.TestService;
import com.example.androidchatapp.Services.UserService;
import com.example.androidchatapp.chat_screen.ChatActivity;

import java.util.ArrayList;

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
        //ChatService.rejoinGroups(getApplicationContext(), username);

        Intent serviceIntent = new Intent(this, TestService.class);
        serviceIntent.putExtra("message", "");
        Log.e("service", "intent start service WebPubSubConService");
        startService(serviceIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                //request all possible chats from DB
                Toast.makeText(getApplicationContext(), "query submit", Toast.LENGTH_SHORT).show();

                UserService.getAll(getApplicationContext(), adapter, s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                // filter existing chats

                adapter.getFilter().filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}