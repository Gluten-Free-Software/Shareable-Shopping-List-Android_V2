package com.glutenfreesoftware.shareable_shopping;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ListActivity extends AppCompatActivity {

    private String username;
    private String room;
    private int roomID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        username = bundle.getString("username");
        room = bundle.getString("room");
        roomID = bundle.getInt("roomID");
        System.out.println(username + " " + room);

        Fragment fragment = null;
        fragment = new ShoppingLists();
        Bundle fragmentBundle = new Bundle();
        fragmentBundle.putString("username", this.username);
        fragmentBundle.putString("room", this.room);
        fragmentBundle.putInt("roomID", this.roomID);
        fragment.setArguments(fragmentBundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_list, fragment);
        transaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("ListActivity Resumed! ******************************");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("ListActivity Paused! ******************************");
    }

}
