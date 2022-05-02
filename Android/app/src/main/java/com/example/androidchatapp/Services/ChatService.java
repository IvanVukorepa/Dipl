package com.example.androidchatapp.Services;

import android.content.Context;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidchatapp.R;
import com.example.androidchatapp.main_screen.ChatListDataStorage;
import com.example.androidchatapp.main_screen.ChatsListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatService {

    private static String pubSubConnectionURL = "";
    public static String chatName = "";
    public static void getConnection(final Context context, final String username, final ServerCalback callback)
    {
        if (ChatService.pubSubConnectionURL.isEmpty()){
            final String negotiateURL = context.getApplicationContext().getString(R.string.ChatServiceBaseURL) + context.getApplicationContext().getString(R.string.negotiate) + "?userId=" + username;
            JsonObjectRequest request = new JsonObjectRequest(negotiateURL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String url = response.getString("url");
                        Log.e("error", "negotiate finished");
                        ChatService.pubSubConnectionURL = url;
                        callback.onSucess(ChatService.pubSubConnectionURL);

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
            callback.onSucess(ChatService.pubSubConnectionURL);
        }
    }

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
}
