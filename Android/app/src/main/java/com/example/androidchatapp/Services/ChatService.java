package com.example.androidchatapp.Services;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidchatapp.R;
import com.example.androidchatapp.main_screen.ChatListDataStorage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.List;


public class ChatService {

    public static String chatName = "";
    public static int notificationId = 0;

    public static void rejoinGroups(final Context context, final String username){
        final String rejoinGroupsURL = context.getApplicationContext().getString(R.string.ChatServiceBaseURL) + context.getApplicationContext().getString(R.string.rejoin) + "?username=" + username;
        StringRequest request = new StringRequest(StringRequest.Method.POST, rejoinGroupsURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("info", "rejoined groups");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", "failed to rejoin groups");
            }
        });

        Volley.newRequestQueue(context).add(request);
    }

    public static void getAllGroupsForUser(final Context context, final String username, final BaseAdapter adapter){
        final String url = context.getApplicationContext().getString(R.string.ChatServiceBaseURL) + context.getApplicationContext().getString(R.string.getAllGroupsUser) + "?username=" + username;
        JsonArrayRequest getGroups = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("blabla", response.toString());

                Gson gson = new Gson();
                Type type = new TypeToken<List<String>>(){}.getType();
                ChatListDataStorage.chats = gson.fromJson(response.toString(), type);

                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error retrieving data from server", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(context).add(getGroups);
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
