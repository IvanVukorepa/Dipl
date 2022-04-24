package com.example.androidchatapp.Services;

import org.json.JSONException;
import org.json.JSONObject;
import com.google.common.io.BaseEncoding;

public class AuthTokenService {
    private static JSONObject payloadJson, headerJson, signatureJson;
    private static String token;

    public static boolean decodeToken(String token){
        String[] parts = token.split("\\."); // split out the "parts" (header, payload and signature)

        String tokenHeader = new String (BaseEncoding.base64Url().decode(parts[0]));
        String tokenPayload = new String (BaseEncoding.base64Url().decode(parts[1]));
        //String tokenSignature = new String(BaseEncoding.base64Url().decode(parts[2]));

        try{
            AuthTokenService.headerJson = new JSONObject(tokenHeader);
            AuthTokenService.payloadJson = new JSONObject(tokenPayload);
            //AuthTokenService.signatureJson = new JSONObject(tokenSignature);
        } catch(JSONException e) {
            e.printStackTrace();
            return false;
        }
        AuthTokenService.setToken(token);
        return true;
    }

    public static JSONObject getPayloadData(){
        return AuthTokenService.payloadJson;
    }

    public static String getPayloadData(String name){
        return AuthTokenService.payloadJson.optString(name, "failureToGet" + name);
    }

    public static void setToken(String token){
        AuthTokenService.token = token;
    }

    public static String getToken(){
        return AuthTokenService.token;
    }
}
