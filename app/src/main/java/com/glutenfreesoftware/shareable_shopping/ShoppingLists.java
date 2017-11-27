package com.glutenfreesoftware.shareable_shopping;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oebar on 08.11.2017.
 */

public class ShoppingLists extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private String username = "";
    private String room = "";
    private int roomID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_shopping_lists, container, false);

        username = getArguments().getString("username");
        room = getArguments().getString("room");
        roomID = getArguments().getInt("roomID");
        //System.out.println("RoomID in ShopplingLists.java class: " + roomID);
        getActivity().setTitle(room);

        Button deleteRoomBtn = (Button) view.findViewById(R.id.delete_room);
        deleteRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Are you sure you wish to delete folder?");
                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteRoom(v, room, username);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 3s = 3000ms
                                //getListsMethod();
                                System.out.println("Folder have been deleted!");
                            }
                        }, 3000);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

                //Insert code for adding to server from server

            }

        });
        Button addListBtn = (Button) view.findViewById(R.id.add_list);
        addListBtn.setOnClickListener(new View.OnClickListener() {

            String listToAdd = "";

            @Override
            public void onClick(final View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Add list");

                // Set up the input
                final EditText input = new EditText(getActivity());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listToAdd = input.getText().toString();

                        addList(v, room, listToAdd, username);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 3s = 3000ms
                                getListsMethod();
                            }
                        }, 3000);
                        System.out.println("user:" + listToAdd);
                        //addedRoom = input.getText().toString();


                        System.out.println("user:" + listToAdd);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

                //Insert code for adding to server from server

            }

        });

        Button shareRoomBtn = (Button) view.findViewById(R.id.share_room);
        shareRoomBtn.setOnClickListener(new View.OnClickListener() {

            String shareWith = "";

            @Override
            public void onClick(final View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Share");

                // Set up the input
                final EditText input = new EditText(getActivity());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        shareWith = input.getText().toString();
                        //System.out.println(room + " " + username + " " +shareWith);
                        shareRoom(v, room, username, shareWith);
                        //System.out.println("user:" + shareWith);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

                //Insert code for adding to server from server

            }

        });


        System.out.println("Shopping list: " + username + " " + room);

        recyclerView = (RecyclerView) view.findViewById(R.id.rooms_recyclerview);
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        getListsMethod();
        /*
        final List<ListObj> input = new ArrayList<>();
        try{
            new getLists(new getLists.OnPostExecute() {
                @Override
                public void onPostExecute(List<ListObj> lists) {
                    if(lists.isEmpty()){
                        System.out.println("list is empty...hello?");
                    }
                    for(ListObj l: lists){
                        System.out.println(l.getListRoom() + " " + l.getListName() + " " + l.getListOwner());
                        input.add(l);
                    }
                    mAdapter = new ListAdapter("Kristian", input);
                    recyclerView.setAdapter(mAdapter);
                }
            }).execute(new URL("http://158.38.193.60:8080/Shareable-Shopping-List-REST/api/lists/getLists?listRoom="+room+"&listOwner="+username));
        } catch (Exception e){
            e.printStackTrace();
        }
        */
        //"http://158.38.193.60:8080/Shareable-Shopping-List-REST/api/lists/getLists?listRoom=Oppskrifter&listOwner=Kristian"
        //
        //http://158.38.193.60:8080/Shareable-Shopping-List-REST/api/lists/getLists?listRoom=Oppskrifter&listOwner=Kristian

        //List<String> input = new ArrayList<>();
        //for (int i = 0; i < 100; i++) {
        //    input.add("Test" + i);
        //}// define an adapter
        //mAdapter = new MyAdapter(input);
        //recyclerView.setAdapter(mAdapter);



        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //getActivity().setTitle("Shopping Lists");
    }


    public void getListsMethod(){

        final List<ListObj> input = new ArrayList<>();
        try{
            new getLists(new getLists.OnPostExecute() {
                @Override
                public void onPostExecute(List<ListObj> lists) {
                    if(lists.isEmpty()){
                        System.out.println("list is empty...hello?");
                    }
                    for(ListObj l: lists){
                        System.out.println(l.getListRoom() + " " + l.getListName() + " " + l.getListOwner());
                        input.add(l);
                    }
                    mAdapter = new ListAdapter(username, input);
                    recyclerView.setAdapter(mAdapter);
                }
            }).execute(new URL("http://192.168.1.43:8080/ssl-fk-sharing/api/lists/getLists?listRoom="+room+"&listOwner="+username));
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void deleteRoom(View v, String roomName, String roomOwner) {
        RequestQueue queue = Volley.newRequestQueue(v.getContext());
        String url = "http://192.168.1.43:8080/ssl-fk-sharing/api/rooms/removeRoom?roomName=" + roomName + "&roomOwner=" + roomOwner;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //System.out.println("Response is: "+ response /*response.substring(0,500)*/);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
            }

        }) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", " application/json");
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void addList(View v, String listRoom, String listName, String listOwner) {
        RequestQueue queue = Volley.newRequestQueue(v.getContext());
        String url = "http://192.168.1.43:8080/ssl-fk-sharing/api/lists/addList?listRoom=" + listRoom + "&listName=" + listName + "&listOwner=" + listOwner;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //System.out.println("Response is: "+ response /*response.substring(0,500)*/);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
            }

        }) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", " application/json");
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void shareRoom(View v, String roomName, String roomOwner, String sharedWith) {
        //String massageToSend = "test";
        //final String message = massageToSend.getText().toString();
        //massageToSend.setText("");
        //System.out.println("Shareing?");
        //System.out.println(roomName + " " + roomOwner + " " + sharedWith);

        RequestQueue queue = Volley.newRequestQueue(v.getContext());
        String url = "http://192.168.1.43:8080/ssl-fk-sharing/api/rooms/shareRoom?sharedRoomName=" + roomName + "&sharedRoomOwner=" + roomOwner + "&sharedWith=" + sharedWith;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //System.out.println("Response is: "+ response /*response.substring(0,500)*/);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
            }

        }) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", " application/json");
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
