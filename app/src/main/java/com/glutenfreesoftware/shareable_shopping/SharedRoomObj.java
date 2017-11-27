package com.glutenfreesoftware.shareable_shopping;

/**
 * Created by Kristian on 25.11.2017.
 */

public class SharedRoomObj {

    private int roomID;
    private String roomName;
    private String roomOwner;

    public SharedRoomObj(int roomID, String roomName, String roomOwner) {
        this.roomID = roomID;
        this.roomName = roomName;
        this.roomOwner = roomOwner;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomOwner() {
        return roomOwner;
    }

    public void setRoomOwner(String roomOwner) {
        this.roomOwner = roomOwner;
    }
}
