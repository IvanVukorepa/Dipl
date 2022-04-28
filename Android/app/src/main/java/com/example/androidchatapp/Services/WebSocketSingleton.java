package com.example.androidchatapp.Services;

import android.content.Context;
import android.util.Log;

import com.android.volley.toolbox.StringRequest;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

public class WebSocketSingleton {

    public static WebSocketClient client;
    private static URI uri;

    public static WebSocketClient getSocketClient(final Context context, final String url, final String username){
        if (client != null && client.isOpen()){
            return client;
        }
        try{
            uri = new URI(url);
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Sec-WebSocket-Protocol", "json.webpubsub.azure.v1");
            client = new WebSocketClient(uri, headers) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.e("faaf", "connection opened");
                    ChatService.rejoinGroups(context, username);
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
                    Log.e("faaf", "error" + ex.toString());

                }
            };
            try{
                client.connectBlocking();
            } catch(InterruptedException ex){
                Log.e("exception", ex.toString());
            }

        } catch (URISyntaxException e){
            Log.e("error", "uri syntax exception");
        }

        return client;
    }
}
