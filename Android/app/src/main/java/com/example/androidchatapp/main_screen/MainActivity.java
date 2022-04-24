package com.example.androidchatapp.main_screen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.androidchatapp.R;
import com.example.androidchatapp.Services.AuthTokenService;
import com.example.androidchatapp.Services.ChatService;
import com.example.androidchatapp.Services.ServerCalback;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    URI uri;
    WebSocketClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        String username = AuthTokenService.getPayloadData("username");
        Log.i("error", username);
        ChatService.getConnection(getApplicationContext(), username, new ServerCalback() {
            @Override
            public void onSucess(String url) {
                Log.e("url", url);
                try{
                    uri = new URI(url);
                    client = new WebSocketClient(uri) {
                        @Override
                        public void onOpen(ServerHandshake handshakedata) {
                            Log.e("faaf", "connection opened");
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
                    Log.e("error", "uri syntax excepton");
                }
            }
        });


/*
        client = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {

            }

            @Override
            public void onMessage(String message) {

            }

            @Override
            public void onClose(int code, String reason, boolean remote) {

            }

            @Override
            public void onError(Exception ex) {

            }
        };

        client.connect();*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        client.close();
    }
}