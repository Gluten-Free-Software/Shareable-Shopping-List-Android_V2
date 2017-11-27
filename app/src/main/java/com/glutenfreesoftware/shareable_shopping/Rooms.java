package com.glutenfreesoftware.shareable_shopping;

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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by oebar on 08.11.2017.
 */

public class Rooms extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private String username = "";
    private String password = "";
    //private int roomID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.layout_rooms, container, false);

        username = getArguments().getString("username");
        //password = getArguments().getString("password");
        //System.out.println("**************************************************************Username = " + username);

        Button deleteRoomBtn = (Button) view.findViewById(R.id.add_room);
        deleteRoomBtn.setOnClickListener(new View.OnClickListener() {
            String roomNamePosted = "";

            @Override
            public void onClick(final View v) {
                Button addRoomBtn = (Button) view.findViewById(R.id.add_room);
                addRoomBtn.setOnClickListener(new View.OnClickListener() {
                    String addedRoom = "";

                    @Override
                    public void onClick(final View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Add folder");

                        // Set up the input
                        final EditText input = new EditText(getActivity());
                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        builder.setView(input);
                        // Set up the buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                roomNamePosted = input.getText().toString();
                                sendMessage(v, roomNamePosted, username);
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Do something after 3s = 3000ms
                                        getRoomsMethod();
                                    }
                                }, 3000);
                                System.out.println("user:" + roomNamePosted);
                                addedRoom = input.getText().toString();

                                System.out.println(addedRoom);
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
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.rooms_recyclerview);
                // use this setting to
                // improve performance if you know that changes
                // in content do not change the layout size
                // of the RecyclerView
        recyclerView.setHasFixedSize(true);
                // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        getRoomsMethod();
         return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Folders");
    }

    public void deleteRoom(View view) {

    }

    public void getRoomsMethod() {
        System.out.println("getRoomsMethod");

        final List<RoomObj> input = new ArrayList<>();
        try {
            new getRooms(new getRooms.OnPostExecute() {
                @Override
                public void onPostExecute(List<RoomObj> rooms) {
                    if (rooms.isEmpty()) {
                        System.out.println("rooms is empty");
                    }
                    for (RoomObj r : rooms) {
                        //System.out.println(r.getRoomName() + " " + r.getRoomOwner());
                        input.add(r);
                    }
                    mAdapter = new RoomAdapter(username, input);
                    recyclerView.setAdapter(mAdapter);
                }
            }).execute(new URL("http://192.168.1.43:8080/ssl-fk-sharing/api/rooms/getUserRooms?roomOwner=" + username));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(View v, String roomName, String roomOwner) {
        //String massageToSend = "test";
        //final String message = massageToSend.getText().toString();
        //massageToSend.setText("");
        //System.out.println("Message: " + message);

        RequestQueue queue = Volley.newRequestQueue(v.getContext());
        String url = "http://192.168.1.43:8080/ssl-fk-sharing/api/rooms/addRoom?roomName=" + roomName + "&roomOwner=" + roomOwner;
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


