package com.glutenfreesoftware.shareable_shopping;

/**
 * Created by Kristian on 26.11.2017.
 */

public class SharedListObj {

    private int listID;
    private String username;
    private String listName;

    public SharedListObj(int listID, String listName, String username) {
        this.listID = listID;
        this.listName = listName;
        this.username = username;
    }

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
