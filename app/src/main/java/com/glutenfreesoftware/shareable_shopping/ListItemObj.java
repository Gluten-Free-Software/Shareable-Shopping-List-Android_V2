package com.glutenfreesoftware.shareable_shopping;

/**
 * Created by Kristian on 15.11.2017.
 */

public class ListItemObj {

    private String listItemList;
    private String listItemName;
    private String listItemOwner;
    private int listID;


    public ListItemObj(int listID, String listItemList, String listItemName, String listItemOwner) {
        this.listID = listID;
        this.listItemList = listItemList;
        this.listItemName = listItemName;
        this.listItemOwner = listItemOwner;
    }

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }

    public String getListItemList() {
        return listItemList;
    }

    public void setListItemList(String listItemList) {
        this.listItemList = listItemList;
    }

    public String getListItemName() {
        return listItemName;
    }

    public void setListItemName(String listItemName) {
        this.listItemName = listItemName;
    }

    public String getListItemOwner() {
        return listItemOwner;
    }

    public void setListItemOwner(String listItemOwner) {
        this.listItemOwner = listItemOwner;
    }
}
