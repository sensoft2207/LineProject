package com.mxi.lineapp.model;

/**
 * Created by android on 7/3/17.
 */

public class player {


    boolean onField;
    String player_name;
    int player_id, left_position , top_position;

    public String getPlayer_name() {
        return player_name;
    }

    public void setPlayer_name(String player_name) {
        this.player_name = player_name;
    }

    public int getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(int player_id) {
        this.player_id = player_id;
    }

    public int getLeft_position() {
        return left_position;
    }

    public void setLeft_position(int left_position) {
        this.left_position = left_position;
    }

    public int getTop_position() {
        return top_position;
    }

    public void setTop_position(int top_position) {
        this.top_position = top_position;
    }

    public boolean isOnField() {
        return onField;
    }

    public void setOnField(boolean onField) {
        this.onField = onField;
    }
}
