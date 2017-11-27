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

public class SharedRooms extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private String username = "";
    private String password = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.layout_shared_rooms, container, false);
        System.out.println("SharedRooms");

        username = getArguments().getString("username");
        recyclerView = (RecyclerView) view.findViewById(R.id.sh_rooms_recyclerview);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        getSharedRoomsMethod();
        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Shared Folders");
    }

    public void deleteRoom(View view) {

    }

    public void getSharedRoomsMethod() {
        System.out.println("getRoomsMethod");

        final List<SharedRoomObj> input = new ArrayList<>();
        try {
            new getSharedRooms(new getSharedRooms.OnPostExecute() {
                @Override
                public void onPostExecute(List<SharedRoomObj> SharedRooms) {
                    if (SharedRooms.isEmpty()) {
                        System.out.println("SharedRooms is empty");
                    }
                    for (SharedRoomObj r : SharedRooms) {
                        //System.out.println(r.getRoomName() + " " + r.getRoomOwner());
                        input.add(r);
                    }
                    mAdapter = new SharedRoomAdapter(username, input);
                    recyclerView.setAdapter(mAdapter);
                }
            }).execute(new URL("http://192.168.1.43:8080/ssl-fk-sharing/api/rooms/sharedRooms?sharedWith=" + username));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
