package com.glutenfreesoftware.shareable_shopping;

import android.os.AsyncTask;
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

public class LoginCheck extends AsyncTask<URL,Integer,List<Users>> {
    public interface OnPostExecute {
        void onPostExecute(List<Users> users);
    }

    OnPostExecute callback;

    public LoginCheck(OnPostExecute callback) {
        this.callback = callback;
    }

    @Override
    protected List<Users> doInBackground(URL... urls) {
        if(urls.length < 1) return Collections.EMPTY_LIST;
        Log.d("LoginCheck", "URL received");


        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        List<Users> result = new ArrayList<>();

        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection)urls[0].openConnection();
            Log.d("LoginCheck", "connection established");
            JsonReader jr = new JsonReader(new InputStreamReader(con.getInputStream()));
            jr.beginArray();
            while (jr.hasNext()) {
                String username = null;
                long size = -1;
                Date date = null;

                jr.beginObject();
                while (jr.hasNext()) {
                    switch (jr.nextName()) {
                        case "username":
                            username = jr.nextString();
                            Log.d("LoginCheck", "Username received: " + username);
                            break;
                        default:
                            jr.skipValue();
                    }
                }
                jr.endObject();
                result.add(new Users(username));
            }
            jr.endArray();
        } catch (IOException e) {
            Log.e("LoginCheck","Failed to verify login" + urls[0],e);
        }


        return result;
    }

    @Override
    protected void onPostExecute(List<Users> users) {
        if(callback != null)
            callback.onPostExecute(users);
    }
}