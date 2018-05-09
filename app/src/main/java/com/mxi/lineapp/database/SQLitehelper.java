package com.mxi.lineapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class SQLitehelper {
    Context mcon;
    private static String dbname = "LineSql.db";
//    static String db_path = Environment.getExternalStorageDirectory() + "/DCIM/lessunknown/" + dbname;
    static String db_path = Environment.getExternalStorageDirectory()+ "/Line/database/" + dbname;

    SQLiteDatabase db;

    public SQLitehelper(Context mcon) {
        this.mcon = mcon;
        final File direct = new File(Environment.getExternalStorageDirectory()+ "/Line/database/");
        if (!direct.exists()) {
            direct.mkdirs();
        }

        db = mcon.openOrCreateDatabase(db_path, Context.MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE IF NOT EXISTS Formations(formation_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "formation_name VARCHAR , formation_players VARCHAR,t_shirt_colour VARCHAR);");

        db.execSQL("CREATE TABLE IF NOT EXISTS Players(player_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + " player_name VARCHAR, left_position VARCHAR, top_position VARCHAR,formation_id VARCHAR,onField INTEGER DEFAULT 0); ");

        db.execSQL("CREATE TABLE IF NOT EXISTS Team1(player_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + " player_name VARCHAR, left_position VARCHAR, top_position VARCHAR,onField INTEGER DEFAULT 0); ");

        db.execSQL("CREATE TABLE IF NOT EXISTS Team2(player_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + " player_name VARCHAR, left_position VARCHAR, top_position VARCHAR,onField INTEGER DEFAULT 0); ");

        db.execSQL("CREATE TABLE IF NOT EXISTS TeamAnim(player_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + " player_name VARCHAR, left_position VARCHAR, top_position VARCHAR,onField INTEGER DEFAULT 0); ");

/*        db.execSQL("CREATE TABLE IF NOT EXISTS BasicFormations(formation_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "formation_name VARCHAR, total_players VARCHAR);");*/

        /*
        db.execSQL("CREATE TABLE IF NOT EXISTS Markups(markup_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "pic_id VARCHAR, markups_name VARCHAR, coordinate_x VARCHAR, coordinate_y VARCHAR, comment_detail VARCHAR); ");*/

    }

//-------------------------------Query Methods -------------------------------------------------------------------------------------

    public void insertFormation(String formation_name,String formation_players,String t_shirt_colour) {

        String query = "INSERT INTO Formations(formation_name,formation_players,t_shirt_colour)VALUES ('"
                + formation_name+"','"+formation_players+"','"+t_shirt_colour+"')";
        Log.e("Query Formation", query);
        try {
            db.execSQL(query);
        } catch (SQLException e) {
            Log.e("Error Formation", e.getMessage());
        }
    }


    public void insertPlayer(String player_name, String left_position, String top_position, String formation_id,int onField) {
        String query = "INSERT INTO Players(player_name,left_position,top_position,formation_id,onField)VALUES ('"
                + player_name
                + "','"
                + left_position
                + "','"
                + top_position
                + "','"
                + formation_id
                + "','"
                + onField+"')";
        Log.e("Query Player", query);
        try {
            db.execSQL(query);
        } catch (SQLException e) {
            Log.e("Error Player", e.getMessage());
        }

    }

    public void creataTeam1(){
        db.execSQL("CREATE TABLE IF NOT EXISTS Team1(player_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + " player_name VARCHAR, left_position VARCHAR, top_position VARCHAR,onField INTEGER DEFAULT 0); ");
    }
    public void creataTeam2(){
        db.execSQL("CREATE TABLE IF NOT EXISTS Team2(player_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + " player_name VARCHAR, left_position VARCHAR, top_position VARCHAR,onField INTEGER DEFAULT 0); ");
    }

    public void creataTeamAnim(){
        db.execSQL("CREATE TABLE IF NOT EXISTS TeamAnim(player_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + " player_name VARCHAR, left_position VARCHAR, top_position VARCHAR,onField INTEGER DEFAULT 0); ");
    }

    public void insertTeam1(String player_name, String left_position, String top_position,int onField) {
        String query = "INSERT INTO Team1(player_name,left_position,top_position,onField)VALUES ('"
                + player_name
                + "','"
                + left_position
                + "','"
                + top_position
                + "','"
                + onField+"')";
        Log.e("Query Team1", query);
        try {
            db.execSQL(query);
        } catch (SQLException e) {
            Log.e("Error Team1", e.getMessage());
        }
    }


    public void insertTeam2(String player_name, String left_position, String top_position,int onField) {
        String query = "INSERT INTO Team2(player_name,left_position,top_position,onField)VALUES ('"
                + player_name
                + "','"
                + left_position
                + "','"
                + top_position
                + "','"
                + onField+"')";
        Log.e("Query Team2", query);
        try {
            db.execSQL(query);
        } catch (SQLException e) {
            Log.e("Error Team2", e.getMessage());
        }
    }
    public void insertTeamAnim(String player_name, String left_position, String top_position,int onField) {
        String query = "INSERT INTO TeamAnim(player_name,left_position,top_position,onField)VALUES ('"
                + player_name
                + "','"
                + left_position
                + "','"
                + top_position
                + "','"
                + onField+"')";
        Log.e("Query TeamAnim", query);
        try {
            db.execSQL(query);
        } catch (SQLException e) {
            Log.e("Error TeamAnim", e.getMessage());
        }
    }

    public void deleteTeam1() {
        try {
            db.execSQL("drop table Team1");
        } catch (Exception e) {
            Log.e("Error: deleteTeam1", e.getMessage());
        }

    }

    public void deleteTeam2() {
        try {
            db.execSQL("drop table Team2");
        } catch (Exception e) {
            Log.e("Error: deleteTeam2", e.getMessage());
        }

    }
    public void deleteTeamAnim() {
        try {
            db.execSQL("drop table TeamAnim");
        } catch (Exception e) {
            Log.e("Error: deleteTeamAnim", e.getMessage());
        }

    }

    public Cursor getTeam1() {
        Cursor cur = null;
        try {
            String query = "SELECT * FROM Team1";
            cur = db.rawQuery(query, null);
            if (cur != null && cur.getCount() != 0) {
                cur.moveToFirst();
            }
        } catch (Exception e) {
            Log.e("E:getTeam1", e.getMessage());
        }
        return cur;
    }

    public Cursor getTeam2() {
        Cursor cur = null;
        try {
            String query = "SELECT * FROM Team2";
            cur = db.rawQuery(query, null);
            if (cur != null && cur.getCount() != 0) {
                cur.moveToFirst();

            }
        } catch (Exception e) {
            Log.e("E:getTeam2", e.getMessage());
        }
        return cur;
    }

    public Cursor getTeamAnim() {
        Cursor cur = null;
        try {
            String query = "SELECT * FROM TeamAnim";
            cur = db.rawQuery(query, null);
            if (cur != null && cur.getCount() != 0) {
                cur.moveToFirst();

            }
        } catch (Exception e) {
            Log.e("E:getTeamAnim", e.getMessage());
        }
        return cur;
    }

    public void UpdateTeam1(String player_name, String left_position, String top_position, String player_id,int onField) {

        String query = "UPDATE Team1 SET player_name = '" + player_name+ "' , left_position = '" +left_position+"' , top_position = '"+top_position+"' , onField = '"+onField+"' WHERE player_id ='"+player_id+"'";

        Log.e("UpdateTeam1", query);
        try {
            db.execSQL(query);
        } catch (SQLException e) {
            Log.e("UpdateTeam1", e.getMessage());
        }
    }

    public String getPlayerIdFromTeam1(String player_name) {
        Cursor cur = null;
        String formationId = "";
        try {
            String query = "SELECT  * FROM  Team1 WHERE player_name = "
                    + "'" + player_name + "'";
            cur = db.rawQuery(query, null);
            if (cur != null && cur.getCount() != 0) {
                cur.moveToFirst();
                formationId = cur.getString(0);
            }
        } catch (Exception e) {
            Log.e("E:getPlayerIdFromTeam1", e.getMessage());
        }
        return formationId;
    }


    public void UpdateTeam2(String player_name, String left_position, String top_position, String player_id,int onField) {

        String query = "UPDATE Team2 SET player_name = '" + player_name+ "' , left_position = '" +left_position+"' , top_position = '"+top_position+"' , onField = '"+onField+"' WHERE player_id ='"+player_id+"'";

        Log.e("UpdateTeam2", query);
        try {
            db.execSQL(query);
        } catch (SQLException e) {
            Log.e("UpdateTeam2", e.getMessage());
        }
    }

    public String getPlayerIdFromTeam2(String player_name) {
        Cursor cur = null;
        String formationId = "";
        try {
            String query = "SELECT  * FROM  Team2 WHERE player_name = "
                    + "'" + player_name + "'";
            cur = db.rawQuery(query, null);
            if (cur != null && cur.getCount() != 0) {
                cur.moveToFirst();
                formationId = cur.getString(0);
            }
        } catch (Exception e) {
            Log.e("E:getPlayerIdFromTeam2", e.getMessage());
        }
        return formationId;
    }


    public String getFormationIdFromFormationName(String formation_name) {
        Cursor cur = null;
        String formationId = "";
        try {
            String query = "SELECT  * FROM  Formations WHERE formation_name = "
                    + "'" + formation_name + "'";
            cur = db.rawQuery(query, null);
            if (cur != null && cur.getCount() != 0) {
                cur.moveToFirst();
                formationId = cur.getString(0);
            }
        } catch (Exception e) {
            Log.e("E:getFormationIdFromFormationName", e.getMessage());
        }
        return formationId;
    }

    public String getTotalPlayersFromFormationId(String formation_id) {
        Cursor cur = null;
        String totalPlayers = "";
        try {
            String query = "SELECT  * FROM  Formations WHERE formation_id = "
                    + "'" + formation_id + "'";
            cur = db.rawQuery(query, null);
            if (cur != null && cur.getCount() != 0) {
                cur.moveToFirst();
                totalPlayers = cur.getString(2);
            }
        } catch (Exception e) {
            Log.e("E:getTotalPlayersFromFormationId", e.getMessage());
        }
        return totalPlayers;
    }

    public String getColorIdFromFormationId(String formation_id) {
        Cursor cur = null;
        String colourId = "";
        try {
            String query = "SELECT  * FROM  Formations WHERE formation_id = "
                    + "'" + formation_id + "'";
            cur = db.rawQuery(query, null);
            if (cur != null && cur.getCount() != 0) {
                cur.moveToFirst();
                colourId = cur.getString(3);
            }
        } catch (Exception e) {
            Log.e("E:getColorIdFromFormationId", e.getMessage());
        }
        return colourId;
    }

    public Cursor isFormationAvailable(String formation_name) {
        Cursor cur = null;
        boolean notAvailable = false;
        try {
            String query = "SELECT  * FROM  Formations WHERE formation_name = '" + formation_name + "'";
            cur = db.rawQuery(query, null);
        } catch (Exception e) {
            Log.e("E:isFormationAvailable", e.getMessage());
        }
        return cur;
    }


    public String getPlayerIdFromPlayerName(String player_name,String formation_id) {
        Cursor cur = null;
        String formationId = "";
        try {
            String query = "SELECT  * FROM  Players WHERE player_name = "
                    + "'" + player_name + "' AND formation_id ='"+formation_id+"'";
            cur = db.rawQuery(query, null);
            if (cur != null && cur.getCount() != 0) {
                cur.moveToFirst();
                formationId = cur.getString(0);
            }
        } catch (Exception e) {
            Log.e("E:PlayerIdFromPlayerName", e.getMessage());
        }
        return formationId;
    }



    public Cursor getFormations() {
        Cursor cur = null;
        try {
            String query = "SELECT  * FROM  Formations ";
            cur = db.rawQuery(query, null);
        } catch (Exception e) {
            Log.e("Error: getFormations ", e.getMessage());
        }

        return cur;
    }

    public Cursor getPlayersFromFormationId(String formation_id) {
        Cursor cur = null;
        try {
            String query = "SELECT  * FROM  Players WHERE formation_id = "
                    + "'" + formation_id + "'";
            cur = db.rawQuery(query, null);

        } catch (Exception e) {
            Log.e("getPlayersFromFormationId", e.getMessage());
        }

        return cur;
    }

    public void updatePlayerName(String player_id, String player_name) {

        String query = "UPDATE Players SET player_name = '" + player_name
                + "'" + " WHERE player_id = " + "'" + player_id + "'";

        Log.e("Query updatePlayerName", query);
        try {
            db.execSQL(query);
        } catch (SQLException e) {
            Log.e("Error updatePlayerName", e.getMessage());
        }
    }

    public void updateFormationName(String formation_id, String formation_name) {

        String query = "UPDATE Formations SET formation_name = '" + formation_name
                + "'" + " WHERE formation_id = " + "'" + formation_id + "'";

        Log.e("Q:updateFormationName", query);
        try {
            db.execSQL(query);
        } catch (SQLException e) {
            Log.e("E:updateFormationName", e.getMessage());
        }
    }

    public void updateFormationColour(String formation_id, String t_shirt_colour ) {

        String query = "UPDATE Formations SET t_shirt_colour = '" + t_shirt_colour
                + "'" + " WHERE formation_id = " + "'" + formation_id + "'";

        Log.e("Q:updateFormationColour", query);
        try {
            db.execSQL(query);
        } catch (SQLException e) {
            Log.e("E:updateFormationColour", e.getMessage());
        }
    }
    public void updateFormationTotalPlayer(String formation_id, String formation_players) {

        String query = "UPDATE Formations SET formation_players = '" +formation_players
                + "'" + " WHERE formation_id = " + "'" + formation_id + "'";

        Log.e("Query updatePlayerName", query);
        try {
            db.execSQL(query);
        } catch (SQLException e) {
            Log.e("Error updatePlayerName", e.getMessage());
        }
    }


    public void savePlayerForFormation(String player_name, String left_position, String top_position, String formation_id,String player_id,int onField) {

        String query = "UPDATE Players SET player_name = '" + player_name+ "' , left_position = '" +left_position+"' , top_position = '"+top_position+"' , onField = '"+onField+"' WHERE formation_id = " + "'" + formation_id + "' AND player_id ='"+player_id+"'";

        Log.e("savePlayerForFormation", query);
        try {
            db.execSQL(query);
        } catch (SQLException e) {
            Log.e("savePlayerForFormation", e.getMessage());
        }

    }

    public void deleteFormation(String formation_name) {
        try {
            db.execSQL("delete from Formations" + " where formation_name='" + formation_name + "'");
            Log.e("deletePic", "delete from Formations" + " where formation_name='" + formation_name + "'" + "");
        } catch (Exception e) {
            Log.e("Error: deleteFormation ", e.getMessage());
        }

    }

    public void deletePlayer(String player_id) {

        try {

            db.execSQL("delete from Players" + " where player_id='" + player_id + "'");
            Log.e("delete", "delete from Players" + " where player_id='" + player_id + "'" + "");
        } catch (SQLException e) {
            Log.e("Error: Deleteplayer_id", e.getMessage());
        }

    }
    public void deletePlayerTeam1(String player_name) {

        try {

            db.execSQL("delete from Team1" + " where player_name='" + player_name + "'");
            Log.e("delete", "delete from Team1" + " where player_name='" + player_name + "'" + "");

        } catch (SQLException e) {
            Log.e("Error:DeleteplayerTeam1", e.getMessage());


        }

    }

    public void deletePlayerTeam2(String player_name) {

        try {

            db.execSQL("delete from Team2" + " where player_name='" + player_name + "'");
            Log.e("delete", "delete from Team2" + " where player_name='" + player_name + "'" + "");

        } catch (SQLException e) {
            Log.e("Error:DeleteplayerTeam2", e.getMessage());


        }

    }
}
