package com.example.androidchatapp.Services;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidchatapp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatService {

    private static String pubSubConnectionURL = "";
    public static void getConnection(final Context context, final String username, final ServerCalback callback)
    {
        if (ChatService.pubSubConnectionURL.isEmpty()){
            String negotiateURL = context.getApplicationContext().getString(R.string.ChatServiceBaseURL) + context.getApplicationContext().getString(R.string.negotiate) + "?userId=" + username;
            JsonObjectRequest request = new JsonObjectRequest(negotiateURL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String url = response.getString("url");
                        ChatService.pubSubConnectionURL = url;
                        callback.onSucess(ChatService.pubSubConnectionURL);

                    } catch (JSONException e) {
                        Log.e("error", "failed to get connection url");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error", "failed to get connection url");
                }
            });

            Volley.newRequestQueue(context).add(request);
        } else{
            callback.onSucess(ChatService.pubSubConnectionURL);
        }
    }
}
