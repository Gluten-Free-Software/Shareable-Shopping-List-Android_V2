package com.glutenfreesoftware.shareable_shopping;

import android.os.AsyncTask;
import android.os.Message;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class PostToDatabase extends AsyncTask<URL,Integer,String> {
    public interface OnPostExecute {
        void onPostExecute(String status);
    }

    OnPostExecute callback;

    public PostToDatabase(OnPostExecute callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(URL... urls) {
        if(urls.length < 1) return null;


        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String result = null;

        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection)urls[0].openConnection();
            JsonReader jr = new JsonReader(new InputStreamReader(con.getInputStream()));
            jr.beginArray();
            while (jr.hasNext()) {
                jr.beginObject();
                while (jr.hasNext()) {
                    switch (jr.nextName()) {
                        case "Status":
                            result = jr.nextString();
                            break;
                        default:
                            jr.skipValue();
                    }
                }
                jr.endObject();
            }
            jr.endArray();
        } catch (IOException e) {
            Log.e("PostToDatabase","Failed to posy to: " + urls[0],e);
        }


        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if(callback != null)
            callback.onPostExecute(result);
    }
}