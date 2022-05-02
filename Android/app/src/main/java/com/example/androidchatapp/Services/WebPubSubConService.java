package com.example.androidchatapp.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidchatapp.R;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class WebPubSubConService extends Service {

    public static WebSocketClient client;
    private static URI uri;
    private static String pubSubConnectionURL = "";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
/*
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.e("service", "service WebPubSubConService onStartCommand");

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Log.e("service", "service send event" + this.toString());
                        String message = intent.getExtras().get("message").toString();
                        if (!message.isEmpty()){
                            client.send(message);
                            //EventBus.getDefault().post(message);
                        }
                    }
                }
        ).start();
        return super.onStartCommand(intent, flags, startId);
    }*/

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.e("service", "service WebPubSubConService onStartCommand");

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {

                        /*String message = intent.getExtras().get("message").toString();
                        if (!message.isEmpty()){
                            Log.e("service", "service send event" + this.toString());
                            if (client != null && client.isOpen())
                                client.send(message);
                            //EventBus.getDefault().post(message);
                            return;
                        }*/
                        while(true){
                                /*final String username = "ivan";
                                getConnection(getApplicationContext(), username, new ServerCalback() {
                                    @Override
                                    public void onSucess(String url) {
                                        Log.e("url", url);
                                        //WebSocketSingleton.getSocketClient(getApplicationContext(), url, username);
                                        getSocketClient(url);
                                    }
                                });*/
                                Log.e("service", "service WebPubSubConService run");
                                try{
                                    Thread.sleep(3000);
                                } catch (InterruptedException e){

                                }
                        }
                    }
                }
        ).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        Log.e("service", "service WebPubSubConService started");

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while(true){
                            Log.e("service", "service running" + this.toString());
                            try{
                                Thread.sleep(3000);
                            } catch (InterruptedException e){

                            }
                        }
                    }
                }
        ).start();
        super.onCreate();
    }
/*
    @Override
    public void onCreate() {
        Log.e("service", "service WebPubSubConService started");

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while(true){
                            //final String username = AuthTokenService.getPayloadData("username");
                            final String username = "ivan";
                            getConnection(getApplicationContext(), username, new ServerCalback() {
                                @Override
                                public void onSucess(String url) {
                                    Log.e("url", url);
                                    //WebSocketSingleton.getSocketClient(getApplicationContext(), url, username);
                                    getSocketClient(url);
                                }
                            });
                            Log.e("service", "service WebPubSubConService run");
                            try{
                                Thread.sleep(10000);
                            } catch (InterruptedException e){

                            }
                        }
                    }
                }
        ).start();
        super.onCreate();
    }*/

    @Override
    public void onDestroy() {
        Log.e("service", "service destroyed");
        super.onDestroy();
    }

    public void getSocketClient(String url){
        Log.e("afafs", "getSocketClient");
        if (client != null && client.isOpen()){
            Log.e("afafs", "getSocketClient client exists");
            return;
        }
        try{
            uri = new URI(url);
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Sec-WebSocket-Protocol", "json.webpubsub.azure.v1");
            client = new WebSocketClient(uri, headers) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.e("faaf", "connection opened");
                }

                @Override
                public void onMessage(String message) {
                    Log.e("faaf", "message" + message + "received");
                    EventBus.getDefault().post(message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.e("faaf", "client closed due to" + reason);
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
    }

    public void getConnection(final Context context, final String username, final ServerCalback callback)
    {
        if (WebPubSubConService.pubSubConnectionURL.isEmpty()){
            final String negotiateURL = context.getApplicationContext().getString(R.string.ChatServiceBaseURL) + context.getApplicationContext().getString(R.string.negotiate) + "?userId=" + username;
            JsonObjectRequest request = new JsonObjectRequest(negotiateURL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String url = response.getString("url");
                        Log.e("error", "negotiate finished");
                        WebPubSubConService.pubSubConnectionURL = url;
                        callback.onSucess(WebPubSubConService.pubSubConnectionURL);

                    } catch (JSONException e) {
                        Log.e("error", "failed to get connection url");
                        Log.e("error", e.toString());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error", "failed to get connection url");
                    Log.e("error", negotiateURL);
                    Log.e("error", error.toString());
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> authHeader = new HashMap<>();
                    authHeader.put("Authorization", "Bearer " + AuthTokenService.getToken());
                    return authHeader;
                }
            };

            Volley.newRequestQueue(context).add(request);

        } else{
            callback.onSucess(WebPubSubConService.pubSubConnectionURL);
        }
    }
}
