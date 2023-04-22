package com.stud.langrep.api.translateapi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TranslateAPI {
    static public String API_URL = "https://api.mymemory.translated.net/get?q=";

    public String translateText(String text, String from, String to){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(5, 10, TimeUnit.SECONDS))
                .build();
        Request request = new Request.Builder()
                .url(API_URL + text + "&langpair=" + from + "|" + to)
                .build();
        try(Response response = client.newCall(request).execute()){
            JSONObject json = new JSONObject(response.body().string());
            return encodingToUTF8(json.getJSONObject("responseData").getString("translatedText"));
        } catch (IOException | JSONException e){
            e.printStackTrace();
        }
        return null;
    }
    public static String encodingToUTF8(String text){
        return new String(text.getBytes(StandardCharsets.UTF_8));
    }
}
