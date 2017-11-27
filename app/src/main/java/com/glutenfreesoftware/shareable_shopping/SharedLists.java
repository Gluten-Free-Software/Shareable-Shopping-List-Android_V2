package com.glutenfreesoftware.shareable_shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oebar on 08.11.2017.
 */

public class SharedLists extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String username = "";
    private String password = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_shared_lists, container, false);

        username = getArguments().getString("username");
        //password = getArguments().getString("password");
        System.out.println(username);

        recyclerView = (RecyclerView) view.findViewById(R.id.sh_list_recyclerview);
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        List<String> input = new ArrayList<>();
        getSharedListsMethod();
        //mAdapter = new MyAdapter(input);
        //recyclerView.setAdapter(mAdapter);



        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Shared Lists");
    }

    public void getSharedListsMethod() {
        System.out.println("getListsMethod");

        final List<SharedListObj> input = new ArrayList<>();
        try {
            new getSharedLists(new getSharedLists.OnPostExecute() {
                @Override
                public void onPostExecute(List<SharedListObj> SharedLists) {
                    if (SharedLists.isEmpty()) {
                        System.out.println("SharedLists is empty");
                    }
                    for (SharedListObj r : SharedLists) {
                        //System.out.println(r.getRoomName() + " " + r.getRoomOwner());
                        input.add(r);
                    }
                    mAdapter = new SharedListAdapter(username, input);
                    recyclerView.setAdapter(mAdapter);
                }
            }).execute(new URL("http://192.168.1.43:8080/ssl-fk-sharing/api/lists/sharedLists?sharedWith=" + username));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
