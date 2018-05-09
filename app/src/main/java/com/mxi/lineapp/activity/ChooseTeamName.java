package com.mxi.lineapp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mxi.lineapp.R;
import com.mxi.lineapp.database.SQLitehelper;
import com.mxi.lineapp.model.player;
import com.mxi.lineapp.network.CommonClass;

import java.util.ArrayList;

public class ChooseTeamName extends AppCompatActivity implements View.OnClickListener {
    ArrayList<player> listPlayer2;

    public static String team_name = "";
    ImageView iv_back, iv_ok;
    TextView tv_toolbar;
    EditText et_team_name;
    CommonClass cc;
    SQLitehelper dbcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_team_name);
        cc = new CommonClass(this);
        dbcon = new SQLitehelper(this);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_ok = (ImageView) findViewById(R.id.iv_ok);
        tv_toolbar = (TextView) findViewById(R.id.tv_toolbar_title);
        iv_back.setVisibility(View.VISIBLE);
        et_team_name = (EditText) findViewById(R.id.et_team_name);

        tv_toolbar.setText(getResources().getString(R.string.create_formation_title));

        AdView av_main= (AdView) findViewById(R.id.av_team_name);
        AdRequest adRequest = new AdRequest.Builder().build();
        av_main.loadAd(adRequest);

        iv_back.setOnClickListener(this);
        iv_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_ok:



                if (et_team_name.getText().toString().isEmpty()) {
                    et_team_name.setError(getResources().getString(R.string.enter_team_name));
                } else {


                    Cursor c= null;
                    c = dbcon.isFormationAvailable(et_team_name.getText().toString().trim());
                    c.moveToFirst();
                    if (c.getCount() == 0 || c ==null) {

                        if (cc.loadPrefBoolean("haveAnyPlayer") && cc.loadPrefBoolean("isPlayer1Ready")) {
                            cc.savePrefBoolean("isPlayer2Ready", true);
                            cc.savePrefInt("player2_T_Shirt", cc.loadPrefInt("t_shirt_position"));
                            team_name = et_team_name.getText().toString().trim();
                            CommonClass.team2Name = team_name;
                            listPlayer2 = new ArrayList<player>();
                            listPlayer2 = cc.getPlayers(ChooseFormationActivity.formation, cc.loadPrefInt("SingleLayoutWidth"), cc.loadPrefInt("SingleLayoutHeight"), ChooseNumberOfPlayersActivity.choosen_number);

                            Cursor cursor = null;
                            cursor = dbcon.getTeam2();
                            if (cursor.getCount() != 0 && cursor != null) {
                                dbcon.deleteTeam2();
                                dbcon.creataTeam2();
                            }

                            for (int i = 0; i < listPlayer2.size(); i++) {
                                player player_team1 = listPlayer2.get(i);
                                int onField = 0;
                                if (i < Integer.parseInt(ChooseNumberOfPlayersActivity.choosen_number)) {
                                    onField = 1;
                                }
                                dbcon.insertTeam2(player_team1.getPlayer_name(), player_team1.getLeft_position() + "", player_team1.getTop_position() + "", onField);
                            }

                            startActivity(new Intent(ChooseTeamName.this, VersusActivity.class));
                            finish();

                        } else {
                            cc.savePrefBoolean("isPlayer2Ready", false);
                            cc.savePrefBoolean("haveAnyPlayer",false);
                            team_name = et_team_name.getText().toString().trim();
                            startActivity(new Intent(ChooseTeamName.this, SinglePlayerActivity.class));
                            finish();
                        }

                    }else{
                        et_team_name.setError(getResources().getString(R.string.formation_name_exist));
                    }

                }
                break;
        }
    }
}
