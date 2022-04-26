package com.example.androidchatapp.Services;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidchatapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChatService {

    private static String pubSubConnectionURL = "";
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
}
