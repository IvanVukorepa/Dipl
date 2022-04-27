package com.example.androidchatapp.main_screen;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.androidchatapp.R;
import com.example.androidchatapp.Services.AuthTokenService;
import com.example.androidchatapp.Services.ChatService;
import com.example.androidchatapp.Services.ServerCalback;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    URI uri;
    WebSocketClient client;
    Button testBtn, joinBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        testBtn = (Button) findViewById(R.id.button);
        joinBtn = (Button) findViewById(R.id.button2);

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject test = new JSONObject();
                try {
                    test.put("type", "event");
                    test.put("event", "testevent");
                    //test.put("ackId", 1);
                    test.put("dataType", "text");
                    test.put("data", "pozdrav, test custom event");
                } catch (JSONException e){

                }
                if (client.isOpen())
                    client.send(test.toString());
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
                if (client.isOpen())
                    client.send(test.toString());
            }
        });

        final String username = AuthTokenService.getPayloadData("username");
        Log.i("error", username);
        ChatService.getConnection(getApplicationContext(), username, new ServerCalback() {
            @Override
            public void onSucess(String url) {
                Log.e("url", url);
                try{
                    uri = new URI(url);
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Sec-WebSocket-Protocol", "json.webpubsub.azure.v1");
                    client = new WebSocketClient(uri, headers) {
                        @Override
                        public void onOpen(ServerHandshake handshakedata) {
                            Log.e("faaf", "connection opened");
                            ChatService.rejoinGroups(getApplicationContext(), username);
                        }

                        @Override
                        public void onMessage(String message) {
                            Log.e("faaf", "message" + message + "received");
                        }

                        @Override
                        public void onClose(int code, String reason, boolean remote) {

                        }

                        @Override
                        public void onError(Exception ex) {
                            Log.e("faaf", "error");

                        }
                    };

                    client.connect();

                } catch (URISyntaxException e){
                    Log.e("error", "uri syntax exception");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        client.close();
    }
}