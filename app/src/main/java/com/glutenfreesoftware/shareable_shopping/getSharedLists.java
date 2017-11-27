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

public class getSharedLists extends AsyncTask<URL,Integer,List<SharedListObj>> {
    public interface OnPostExecute {
        void onPostExecute(List<SharedListObj> messages);
    }

    OnPostExecute callback;

    public getSharedLists(OnPostExecute callback) {
        this.callback = callback;
    }

    @Override
    protected List<SharedListObj> doInBackground(URL... urls) {
        if(urls.length < 1) return Collections.EMPTY_LIST;


        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        List<SharedListObj> result = new ArrayList<>();

        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection)urls[0].openConnection();
            JsonReader jr = new JsonReader(new InputStreamReader(con.getInputStream()));
            jr.beginArray();
            while (jr.hasNext()) {
                int listID = 0;
                String username = null;
                String listName = null;
                jr.beginObject();
                while (jr.hasNext()) {
                    switch (jr.nextName()) {
                        case "listID":
                            jr.beginObject();
                            while(jr.hasNext()){
                                switch (jr.nextName()){
                                    case "listID":
                                        listID = jr.nextInt();
                                        break;
                                    case "listName":
                                        listName = jr.nextString();
                                        break;
                                    case "roomID":
                                        jr.beginObject();
                                        while(jr.hasNext()){
                                            switch (jr.nextName()){
                                                case "userID":
                                                    jr.beginObject();
                                                    while(jr.hasNext()){
                                                        switch (jr.nextName()){
                                                            case "username":
                                                                username = jr.nextString();
                                                                break;
                                                            default:
                                                                jr.skipValue();
                                                        }
                                                    }
                                                    jr.endObject();
                                                    break;
                                                default:
                                                    jr.skipValue();
                                            }
                                        }
                                        jr.endObject();
                                        break;
                                    default:
                                        jr.skipValue();
                                }
                            }
                            jr.endObject();
                            break;
                        default:
                            jr.skipValue();
                    }
                }
                jr.endObject();
                System.out.println("getSharedLists: " + listID + " " + listName + " " + username);
                result.add(new SharedListObj(listID, listName, username));
            }
            jr.endArray();
        } catch (IOException e) {
            Log.e("LoadThumb","Failed to load sharedLists from " + urls[0],e);
        }


        return result;
    }

    @Override
    protected void onPostExecute(List<SharedListObj> SharedLists) {
        if(callback != null)
            callback.onPostExecute(SharedLists);
    }
}