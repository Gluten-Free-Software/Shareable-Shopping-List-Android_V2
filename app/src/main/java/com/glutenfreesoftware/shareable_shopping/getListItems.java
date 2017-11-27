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

public class getListItems extends AsyncTask<URL,Integer,List<ListItemObj>> {
    public interface OnPostExecute {
        void onPostExecute(List<ListItemObj> messages);
    }

    OnPostExecute callback;

    public getListItems(OnPostExecute callback) {
        this.callback = callback;
    }

    @Override
    protected List<ListItemObj> doInBackground(URL... urls) {
        if(urls.length < 1) return Collections.EMPTY_LIST;


        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        List<ListItemObj> result = new ArrayList<>();

        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection)urls[0].openConnection();
            JsonReader jr = new JsonReader(new InputStreamReader(con.getInputStream()));
            jr.beginArray();
            while (jr.hasNext()) {
                String listItemList = null;
                String listItemName= null;
                String listItemOwner = null;
                int listID = 0;
                jr.beginObject();
                while (jr.hasNext()) {
                    switch (jr.nextName()) {
                        case "listItemName":
                            listItemName = jr.nextString();
                            break;
                        case "listID":
                            jr.beginObject();
                            while(jr.hasNext()) {
                                switch (jr.nextName()) {
                                    case "listID":
                                        listID = jr.nextInt();
                                        break;
                                    case "listName":
                                        listItemList = jr.nextString();
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
                                                                listItemOwner = jr.nextString();
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
                result.add(new ListItemObj(listID, listItemList, listItemName, listItemOwner));
            }
            jr.endArray();
        } catch (IOException e) {
            Log.e("LoadThumb","Failed to load rooms from " + urls[0],e);
        }


        return result;
    }

    @Override
    protected void onPostExecute(List<ListItemObj> listItems) {
        if(callback != null)
            callback.onPostExecute(listItems);
    }
}