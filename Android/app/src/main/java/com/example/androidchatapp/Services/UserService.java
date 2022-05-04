package com.example.androidchatapp.Services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidchatapp.R;
import com.example.androidchatapp.login_screen.LoginActivity;
import com.example.androidchatapp.chat_screen.ChatActivity;
import com.example.androidchatapp.main_screen.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class UserService {

    public static void login(final Context context, final String username, final String password){
        String loginURL = context.getString(R.string.UsersServiceBaseURL) + context.getString(R.string.Login);
        StringRequest loginRequest = new StringRequest(StringRequest.Method.POST, loginURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String token = response;
                Log.i("token", token);

                if (AuthTokenService.decodeToken(token, context)){
                    Intent intent;
                    intent = new Intent(context.getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else{
                    Toast.makeText(context.getApplicationContext(), "Failed to decode authentication token", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context.getApplicationContext(),"username or password is wrong", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public byte[] getBody() {
                JSONObject user = new JSONObject();
                try{
                    user.put("Id", username);
                    user.put("username", username);
                    user.put("Password", password);
                } catch(JSONException e){ e.printStackTrace(); }

                return user.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        Volley.newRequestQueue(context).add(loginRequest);
    }

    public static void createUser(final Context context, final JSONObject user){
        String url = context.getString(R.string.UsersServiceBaseURL) + context.getString(R.string.addUser);

        StringRequest registerUser = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                        Toast.makeText(context, "User " + user.optString("Username", "") + " successfully registered", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            public byte[] getBody() throws AuthFailureError {

                return user.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        Volley.newRequestQueue(context).add(registerUser);
    }
}
