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

public class getLists extends AsyncTask<URL,Integer,List<ListObj>> {
    public interface OnPostExecute {
        void onPostExecute(List<ListObj> messages);
    }

    OnPostExecute callback;

    public getLists(OnPostExecute callback) {
        this.callback = callback;
    }

    @Override
    protected List<ListObj> doInBackground(URL... urls) {
        if(urls.length < 1) return Collections.EMPTY_LIST;


        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        List<ListObj> result = new ArrayList<>();

        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection)urls[0].openConnection();
            JsonReader jr = new JsonReader(new InputStreamReader(con.getInputStream()));
            jr.beginArray();
            while (jr.hasNext()) {
                int listID = 0;
                String listRoom = null;
                String listName = null;
                String listOwner = null;
                int roomID = 0;
                jr.beginObject();
                while (jr.hasNext()) {
                    switch (jr.nextName()) {
                        case "listID":
                            listID = jr.nextInt();
                            break;
                        case "listName":
                            listName = jr.nextString();
                            break;
                        case "roomID":
                            jr.beginObject();
                            while(jr.hasNext()){
                                switch(jr.nextName()){
                                    case "roomID":
                                        roomID = jr.nextInt();
                                        break;
                                    case "roomName":
                                        listRoom = jr.nextString();
                                        break;
                                    case "userID":
                                        //System.out.println("we have userID");
                                        jr.beginObject();
                                        while(jr.hasNext()){
                                            //System.out.println("We have something");
                                            switch (jr.nextName()){
                                                case "userID":
                                                    System.out.println("UserID: " + jr.nextInt());
                                                    break;
                                                case "username":
                                                    listOwner = jr.nextString();
                                                    System.out.println("Username: " + listOwner);
                                                    break;
                                                case "email":
                                                    System.out.println("Email: " + jr.nextString());
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
                result.add(new ListObj(listID, listRoom, listName, listOwner));
            }
            jr.endArray();
        } catch (IOException e) {
            Log.e("LoadThumb","Failed to load rooms from " + urls[0],e);
        }


        return result;
    }

    @Override
    protected void onPostExecute(List<ListObj> lists) {
        if(callback != null)
            callback.onPostExecute(lists);
    }
}