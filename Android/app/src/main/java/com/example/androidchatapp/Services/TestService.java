package com.example.androidchatapp.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidchatapp.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class TestService extends Service {
    private final IBinder binder = new Binder();
    public static WebSocketClient client;
    private static URI uri;
    private static String pubSubConnectionURL = "";
    private static int notificationId = 0;

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        if (intent != null) {
                            String message = intent.getExtras().get("message").toString();
                            Log.e("service", "service send event" + this.toString());
                            if (!message.isEmpty()){
                                //EventBus.getDefault().post(message);
                                client.send(message);
                            } else {
                                Log.e("error","message is empty");
                            }
                        }
                    }
                }
        ).start();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e("service", "service started");

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        String authToken = getToken(getApplicationContext());
                        String username = getUsername(getApplicationContext());
                        Log.e("test", authToken + " " + username);
                        getConnection(getApplicationContext(), username, authToken, new ServerCalback() {
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
                                        }

                                        @Override
                                        public void onMessage(String message) {
                                            Log.e("faaf", "message" + message + "received");
                                            if (EventBus.getDefault().hasSubscriberForEvent(String.class)){
                                                EventBus.getDefault().post(message);
                                                //showNotification(getApplicationContext());
                                            } else{
                                                showNotification(getApplicationContext());
                                            }
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
                        });
                    }
                }
        ).start();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.e("service", "service destroyed");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void getConnection(final Context context, final String username, final String authToken, final ServerCalback callback)
    {
        if (TestService.pubSubConnectionURL.isEmpty()){
            final String negotiateURL = context.getApplicationContext().getString(R.string.ChatServiceBaseURL) + context.getApplicationContext().getString(R.string.negotiate) + "?userId=" + username;
            JsonObjectRequest request = new JsonObjectRequest(negotiateURL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String url = response.getString("url");
                        Log.e("error", "negotiate finished");
                        TestService.pubSubConnectionURL = url;
                        callback.onSucess(TestService.pubSubConnectionURL);

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
                    authHeader.put("Authorization", "Bearer " + authToken);
                    return authHeader;
                }
            };

            Volley.newRequestQueue(context).add(request);

        } else{
            callback.onSucess(TestService.pubSubConnectionURL);
        }
    }

    public String getToken(final Context context){
        MyPreferences preferences = new MyPreferences(context);

        return preferences.getString("AuthToken");
    }

    public String getUsername(final Context context){
        MyPreferences preferences = new MyPreferences(context);

        return preferences.getString("Username");
    }

    public static void showNotification(Context context){
        Log.i("service", "show notification");
        String CHANNEL_ID = context.getString(R.string.channel_name);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Notification")
                .setContentText("Notification test")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_VIBRATE);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId++, builder.build());
    }
}
