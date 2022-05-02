package com.example.androidchatapp.chat_screen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidchatapp.R;
import com.example.androidchatapp.Services.AuthTokenService;
import com.example.androidchatapp.Services.ChatService;
import com.example.androidchatapp.Services.ServerCalback;
import com.example.androidchatapp.Services.WebPubSubConService;
import com.example.androidchatapp.Services.WebSocketSingleton;
import com.example.androidchatapp.main_screen.ChatsListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    URI uri;
    //WebSocketClient client;
    Button testBtn, joinBtn;
    EditText newMessageET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        testBtn = (Button) findViewById(R.id.button);
        joinBtn = (Button) findViewById(R.id.button2);
        newMessageET = (EditText) findViewById(R.id.editTextNewMessage);

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("info", "send message clicked");
                JSONObject test = new JSONObject();
                try {
                    test.put("type", "event");
                    test.put("event", "testevent");
                    //test.put("ackId", 1);
                    test.put("dataType", "text");
                    test.put("data", "[" + ChatService.chatName + "]" + newMessageET.getText().toString());
                } catch (JSONException e){
                    Log.e("info", "JSON exception");
                }

                Intent serviceIntent = new Intent(getApplicationContext(), WebPubSubConService.class);
                serviceIntent.putExtra("message", test.toString());
                Log.e("service", "intent start service WebPubSubConService");
                startService(serviceIntent);
                /*if (WebSocketSingleton.client.isOpen()){
                    Log.e("info", "sending message");
                    WebSocketSingleton.client.send(test.toString());
                } else{
                    Log.e("info", "client not open");
                }*/
            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject test = new JSONObject();
                JSONObject data = new JSONObject();

                try {
                    test.put("type", "event");
                    test.put("event", "joinGroup");
                    //test.put("ackId", 1);
                    test.put("dataType", "json");
                    //test.put("data", {"":""});
                    data.put("username",  AuthTokenService.getPayloadData("username"));
                    data.put("group", "testGroup");
                    test.put("data", data);
                } catch(JSONException e){

                }
                /*if (WebSocketSingleton.client.isOpen())
                    WebSocketSingleton.client.send(test.toString());*/
                Intent serviceIntent = new Intent(getApplicationContext(), WebPubSubConService.class);
                serviceIntent.putExtra("message", test.toString());
                Log.e("service", "intent start service WebPubSubConService");
                startService(serviceIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (WebSocketSingleton.client != null && WebSocketSingleton.client.isOpen())
            WebSocketSingleton.client.close();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void test(String input){
        Toast.makeText(getApplicationContext(), input,Toast.LENGTH_LONG).show();
    }
}