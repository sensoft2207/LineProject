package com.mxi.lineapp.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.mxi.lineapp.R;
import com.mxi.lineapp.model.player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CommonClass {

    public static ArrayList<player> player1;
    public static ArrayList<player> player2;
    public static ArrayList<player> playerAnimation;

    boolean result = false;
    public static String team1Name = "";
    public static String team2Name = "";
    public static String teamAnimName = "";

    private Context _context;

    SharedPreferences pref;
    public boolean isDebug;
    int w1 = 0, w2 = 0, w3 = 0, w4 = 0, w5 = 0, w6 = 0, w7 = 0, w8 = 0;
    int l1 = 0, l2 = 0, l3 = 0, l4 = 0, l5 = 0, l6 = 0, l7 = 0, l8 = 0;
    int l_unit = 0;
    int w_unit = 0;
    int bottom = 0;

    public CommonClass(Context context) {
        this._context = context;

        pref = _context.getSharedPreferences("Line",
                _context.MODE_PRIVATE);
        isDebug = false;
    }

    public boolean isConnectingToInternet() {

        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public String getCurrentDateandtime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yymmddhhmmss");
        String last_update = sdf.format(Calendar.getInstance().getTime());
        return last_update;
    }

    // Toast method
    public void showToast(String text) {
        // TODO Auto-generated method stub
        Toast.makeText(_context, text, Toast.LENGTH_SHORT).show();
    }

    public void showSnackbar(View coordinatorLayout, String text) {

        Snackbar
                .make(coordinatorLayout, text, Snackbar.LENGTH_LONG).show();
    }

    // Save String data in SharedPreferences
    public void savePrefString(String key, String value) {
        // TODO Auto-generated method stub
        Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void savePlayer1(ArrayList<player> player) {
        player1 = new ArrayList<player>();
        player1 = player;
    }

    public ArrayList<player> getPlayer1() {
        return player1;
    }

    public void savePlayer2(ArrayList<player> player) {
        player2 = new ArrayList<player>();
        player2 = player;
    }

    public ArrayList<player> getPlayer2() {
        return player2;
    }

    // Save Boolean data in SharedPreferences
    public void savePrefBoolean(String key, Boolean value) {
        // TODO Auto-generated method stub
        Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    //REtrieve String data from SharedPreferences
    public String loadPrefString(String key) {
        // TODO Auto-generated method stub
        String strSaved = pref.getString(key, "");
        return strSaved;
    }

    //REtrieve boolean data from SharedPreferences
    public Boolean loadPrefBoolean(String key) {
        // TODO Auto-generated method stub
        boolean isbool = pref.getBoolean(key, false);
        return isbool;
    }

    // Save Int data in SharedPreferences
    public void savePrefInt(String key, int value) {
        // TODO Auto-generated method stub
        Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    //REtrieve String data from SharedPreferences
    public int loadPrefInt(String key) {
        // TODO Auto-generated method stub
        int strSaved = pref.getInt(key, 0);
        return strSaved;
    }

    // Save Int data in SharedPreferences
    public void savePrefLong(String key, long value) {
        // TODO Auto-generated method stub
        Editor editor = pref.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    //REtrieve String data from SharedPreferences
    public long loadPrefLong(String key) {
        // TODO Auto-generated method stub
        long strSaved = pref.getLong(key, 0);
        return strSaved;
    }

    public void clearData() {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public ArrayList<player> getPlayers(String Formation, int left, int top, String total_players) {
        int total_player = Integer.parseInt(total_players);
        bottom = top;
        setMidpoints(left, top);


        ArrayList<player> players = new ArrayList<>();
        players = setUpAllPlayers(Formation, total_player);
        return players;
    }

    private ArrayList<player> setUpAllPlayers(String formation, int total_player) {
        ArrayList<player> players = new ArrayList<>();
        player player;
        switch (total_player) {
            case 5:

                if (formation.trim().equals("2-2")) {

                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setLeft_position(w2);
                            player.setTop_position(l7);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 3) {
                            player.setLeft_position(w6);
                            player.setTop_position(l7);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setLeft_position(w2);
                            player.setTop_position(l3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setLeft_position(w6);
                            player.setTop_position(l3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        }
                    }
                } else if (formation.trim().equals("1-2-1")) {


                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setLeft_position(w4);
                            player.setTop_position(l7);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 3) {
                            player.setLeft_position(w2);
                            player.setTop_position(l5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setLeft_position(w6);
                            player.setTop_position(l5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setLeft_position(w4);
                            player.setTop_position(l3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        }
                    }


                } else if (formation.trim().equals("2-1-1")) {


                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setLeft_position(w2);
                            player.setTop_position(l7);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 3) {
                            player.setLeft_position(w6);
                            player.setTop_position(l7);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setLeft_position(w4);
                            player.setTop_position(l5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setLeft_position(w4);
                            player.setTop_position(l3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        }
                    }

                } else if (formation.trim().equals("3-1")) {


                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setLeft_position(w2);
                            player.setTop_position(l6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 3) {
                            player.setLeft_position(w4);
                            player.setTop_position(l7);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setLeft_position(w6);
                            player.setTop_position(l6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setLeft_position(w4);
                            player.setTop_position(l3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        }
                    }

                }

                break;
            case 6:

                if (formation.trim().equals("2-2-1")) {
                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setTop_position(l7);
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 3) {
                            player.setTop_position(l7);
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setTop_position(l5);
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setTop_position(l5);
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 6) {
                            player.setTop_position(l3);
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        }

                    }
                } else if (formation.trim().equals("3-1-1")) {

                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setTop_position(l7);
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 3) {
                            player.setTop_position(l7);//l8
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setTop_position(l7);
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setTop_position(l5);
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 6) {
                            player.setTop_position(l3);
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        }

                    }
                } else if (formation.trim().equals("3-2")) {


                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setTop_position(l6);
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 3) {
                            player.setTop_position(l7);
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setTop_position(l6);
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setTop_position(l3);
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 6) {
                            player.setTop_position(l3);
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        }

                    }

                } else if (formation.trim().equals("2-1-2")) {


                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setTop_position(l7);
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 3) {
                            player.setTop_position(l7);
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setTop_position(l5);
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setTop_position(l3);
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 6) {
                            player.setTop_position(l3);
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        }

                    }


                }
                break;

            case 7:


                if (formation.trim().equals("4-1-1")) {
                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setTop_position(l7);
                            player.setLeft_position(w1);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 3) {
                            player.setTop_position(l8);
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setTop_position(l8);
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setTop_position(l7);
                            player.setLeft_position(w7);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 6) {
                            player.setTop_position(l5);
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 7) {
                            player.setTop_position(l3);
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        }

                    }
                } else if (formation.trim().equals("2-2-2")) {

                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setTop_position(l7);
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 3) {
                            player.setTop_position(l7);
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setTop_position(l5);
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setTop_position(l5);
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 6) {
                            player.setTop_position(l3);
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 7) {
                            player.setTop_position(l3);
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        }

                    }
                } else if (formation.trim().equals("3-2-1")) {


                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setTop_position(l7);
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 3) {
                            player.setTop_position(l7);//l8
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setTop_position(l7);
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setTop_position(l5);
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 6) {
                            player.setTop_position(l5);
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 7) {
                            player.setTop_position(l3);
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        }

                    }

                } else if (formation.trim().equals("3-1-2")) {


                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setTop_position(l7);
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 3) {
                            player.setTop_position(l7);//l8
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setTop_position(l7);
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setTop_position(l5);
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 6) {
                            player.setTop_position(l3);
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 7) {
                            player.setTop_position(l3);
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        }

                    }


                }

                break;

            case 8:


                if (formation.trim().equals("3-2-1-1")) {
                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setTop_position(l7);
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 3) {
                            player.setTop_position(l7);//l8
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setTop_position(l7);
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setTop_position(l6);
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 6) {
                            player.setTop_position(l6);
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 7) {
                            player.setTop_position(l5);
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 8) {
                            player.setTop_position(l3);
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        }

                    }
                } else if (formation.trim().equals("3-3-1")) {

                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setTop_position(l7);
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 3) {
                            player.setTop_position(l7);//l8
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setTop_position(l7);
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setTop_position(l5);
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 6) {
                            player.setTop_position(l5);
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 7) {
                            player.setTop_position(l5);
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 8) {
                            player.setTop_position(l3);
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        }

                    }
                } else if (formation.trim().equals("3-2-2")) {

                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setTop_position(l7);
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 3) {
                            player.setTop_position(l7);//l8
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setTop_position(l7);
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setTop_position(l5);
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 6) {
                            player.setTop_position(l5);
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 7) {
                            player.setTop_position(l3);
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 8) {
                            player.setTop_position(l3);
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        }

                    }
                } else if (formation.trim().equals("3-1-2-1")) {


                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setTop_position(l7);
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 3) {
                            player.setTop_position(l7);//l8
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setTop_position(l7);
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setTop_position(l5);
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 6) {
                            player.setTop_position(l4);
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 7) {
                            player.setTop_position(l4);
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 8) {
                            player.setTop_position(l3);
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        }

                    }
                }

                break;

            case 9:


                if (formation.trim().equals("4-3-1")) {
                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setTop_position(l7);
                            player.setLeft_position(w1);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 3) {
                            player.setTop_position(l8);
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setTop_position(l8);
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setTop_position(l7);
                            player.setLeft_position(w7);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 6) {
                            player.setTop_position(l5);
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 7) {
                            player.setTop_position(l6);
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 8) {
                            player.setTop_position(l5);
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 9) {
                            player.setTop_position(l3);
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        }

                    }
                } else if (formation.trim().equals("3-3-2")) {

                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setTop_position(l7);
                            player.setLeft_position(w1);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 3) {
                            player.setTop_position(l7);//l8
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setTop_position(l7);
                            player.setLeft_position(w7);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setTop_position(l5);
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 6) {
                            player.setTop_position(l5);//l6
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 7) {
                            player.setTop_position(l5);
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 8) {
                            player.setTop_position(l3);
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 9) {
                            player.setTop_position(l3);
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        }

                    }
                } else if (formation.trim().equals("3-4-1")) {

                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setTop_position(l7);
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 3) {
                            player.setTop_position(l7);//l8
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setTop_position(l7);
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setTop_position(l5);
                            player.setLeft_position(w1);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 6) {
                            player.setTop_position(l6);
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 7) {
                            player.setTop_position(l6);
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 8) {
                            player.setTop_position(l5);
                            player.setLeft_position(w7);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 9) {
                            player.setTop_position(l3);
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        }

                    }
                } else if (formation.trim().equals("3-2-1-2")) {


                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setTop_position(l7);
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 3) {
                            player.setTop_position(l7);//l8
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setTop_position(l7);
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setTop_position(l6);
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 6) {
                            player.setTop_position(l6);
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 7) {
                            player.setTop_position(l5);
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 8) {
                            player.setTop_position(l3);
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 9) {
                            player.setTop_position(l3);
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        }

                    }
                }


                break;

            case 10:


                if (formation.trim().equals("4-4-1")) {
                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setTop_position(l7);
                            player.setLeft_position(w1);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 3) {
                            player.setTop_position(l8);
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setTop_position(l8);
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setTop_position(l7);
                            player.setLeft_position(w7);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 6) {
                            player.setTop_position(l5);
                            player.setLeft_position(w1);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 7) {
                            player.setTop_position(l6);
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 8) {
                            player.setTop_position(l6);
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 9) {
                            player.setTop_position(l5);
                            player.setLeft_position(w7);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 10) {
                            player.setTop_position(l3);
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        }

                    }
                } else if (formation.trim().equals("3-4-2")) {

                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setTop_position(l7);
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 3) {
                            player.setTop_position(l7);//l8
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setTop_position(l7);
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setTop_position(l5);
                            player.setLeft_position(w1);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 6) {
                            player.setTop_position(l6);
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 7) {
                            player.setTop_position(l6);
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 8) {
                            player.setTop_position(l5);
                            player.setLeft_position(w7);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 9) {
                            player.setTop_position(l3);
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 10) {
                            player.setTop_position(l3);
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        }

                    }
                } else if (formation.trim().equals("3-3-1-2")) {

                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setTop_position(l8);//l7
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 3) {
                            player.setTop_position(l7);//l8
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setTop_position(l8);//l7
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setTop_position(l6);//l5
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 6) {
                            player.setTop_position(l5);//l6
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 7) {
                            player.setTop_position(l6);//l5
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 8) {
                            player.setTop_position(l3);
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 9) {
                            player.setTop_position(l3);//l2
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 10) {
                            player.setTop_position(l3);//l2
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        }

                    }
                } else if (formation.trim().equals("3-2-2-2")) {


                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setTop_position(l8);//l7
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 3) {
                            player.setTop_position(l7);//l8
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setTop_position(l8);//l7
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setTop_position(l7);//l6
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 6) {
                            player.setTop_position(l7);//l6
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 7) {
                            player.setTop_position(l5);//l4
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 8) {
                            player.setTop_position(l5);//l4
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 9) {
                            player.setTop_position(l3);//l2
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 10) {
                            player.setTop_position(l3);//l3
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        }

                    }
                }

                break;

            case 11:


                if (formation.trim().equals("3-4-3")) {
                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setTop_position(l7);
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 3) {
                            player.setTop_position(l7);//l8
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setTop_position(l7);
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setTop_position(l5);
                            player.setLeft_position(w1);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 6) {
                            player.setTop_position(l6);
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 7) {
                            player.setTop_position(l6);
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 8) {
                            player.setTop_position(l5);
                            player.setLeft_position(w7);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 9) {
                            player.setTop_position(l3);
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 10) {
                            player.setTop_position(l4);
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 11) {
                            player.setTop_position(l3);
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        }

                    }
                } else if (formation.trim().equals("4-2-4")) {

                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setTop_position(l7);
                            player.setLeft_position(w1);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 3) {
                            player.setTop_position(l8);
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setTop_position(l8);
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setTop_position(l7);
                            player.setLeft_position(w7);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 6) {
                            player.setTop_position(l6);//l5
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 7) {
                            player.setTop_position(l6);//l5
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 8) {
                            player.setTop_position(l3);//l2
                            player.setLeft_position(w1);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 9) {
                            player.setTop_position(l4);//l3
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 10) {
                            player.setTop_position(l4);//l3
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 11) {
                            player.setTop_position(l3);//l4
                            player.setLeft_position(w7);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        }

                    }
                } else if (formation.trim().equals("4-4-2")) {

                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setTop_position(l7);
                            player.setLeft_position(w1);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 3) {
                            player.setTop_position(l8);
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setTop_position(l8);
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setTop_position(l7);
                            player.setLeft_position(w7);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 6) {
                            player.setTop_position(l4);
                            player.setLeft_position(w1);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 7) {
                            player.setTop_position(l5);
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 8) {
                            player.setTop_position(l5);
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 9) {
                            player.setTop_position(l4);
                            player.setLeft_position(w7);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 10) {
                            player.setTop_position(l3);
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 11) {
                            player.setTop_position(l3);
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        }

                    }
                } else if (formation.trim().equals("4-3-3")) {


                    for (int i = 1; i <= total_player; i++) {
                        player = new player();
                        player.setOnField(true);
                        if (i == 1) {
                            player.setLeft_position(w4);
                            player.setTop_position(bottom);//chech
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 2) {
                            player.setTop_position(l7);
                            player.setLeft_position(w1);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 3) {
                            player.setTop_position(l8);
                            player.setLeft_position(w3);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 4) {
                            player.setTop_position(l8);
                            player.setLeft_position(w5);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 5) {
                            player.setTop_position(l7);
                            player.setLeft_position(w7);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);

                        } else if (i == 6) {
                            player.setTop_position(l5);
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 7) {
                            player.setTop_position(l6);
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 8) {
                            player.setTop_position(l5);
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 9) {
                            player.setTop_position(l3);
                            player.setLeft_position(w2);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 10) {
                            player.setTop_position(l4);
                            player.setLeft_position(w4);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        } else if (i == 11) {
                            player.setTop_position(l3);
                            player.setLeft_position(w6);
                            player.setPlayer_name(_context.getResources().getString(R.string.player_tag) + i);
                            player.setPlayer_id(i);
                            players.add(player);
                        }

                    }
                }


                break;

        }

        return players;
    }


    private void setMidpoints(int left, int top) {
        l_unit = top / 8;
        w_unit = left / 8;

//        l1 = l_unit;
//        l2 = l_unit * 2;
//        l3 = l_unit * 3;
//        l4 = l_unit * 4;
//        l5 = l_unit * 5;
//        l6 = l_unit * 6;
//        l7 = l_unit * 7;
//        l8 = l_unit * 8;

        l1 = 0;
        l2 = l_unit;
        l3 = l_unit * 2;
        l4 = l_unit * 3;
        l5 = l_unit * 4;
        l6 = l_unit * 5;
        l7 = l_unit * 6;
        l8 = l_unit * 7;

        w1 = w_unit;
        w2 = w_unit * 2;
        w3 = w_unit * 3;
        w4 = w_unit * 4;
        w5 = w_unit * 5;
        w6 = w_unit * 6;
        w7 = w_unit * 7;
        w8 = w_unit * 8;

/*        w1 = 0;
        w2 = w_unit ;
        w3 = w_unit * 2;
        w4 = w_unit * 3;
        w5 = w_unit * 4;
        w6 = w_unit * 5;
        w7 = w_unit * 6;
        w8 = w_unit * 7;*/

    }
}
