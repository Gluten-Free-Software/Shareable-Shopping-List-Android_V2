package com.glutenfreesoftware.shareable_shopping;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ListItemActivity extends AppCompatActivity {

    private String username;
    private String list;
    private int listID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        username = bundle.getString("username");
        list = bundle.getString("list");
        listID = bundle.getInt("listID");
        System.out.println(" in listitemact! " + username + " " + list);

        Fragment fragment = null;
        fragment = new ListItems();
        Bundle fragmentBundle = new Bundle();
        fragmentBundle.putString("username", this.username);
        fragmentBundle.putString("list", this.list);
        fragmentBundle.putInt("listID", this.listID);
        fragment.setArguments(fragmentBundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_list_item, fragment);
        transaction.commit();

    }
}
