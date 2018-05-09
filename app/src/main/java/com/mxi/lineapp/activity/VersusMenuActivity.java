package com.mxi.lineapp.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxi.lineapp.R;
import com.mxi.lineapp.database.SQLitehelper;
import com.mxi.lineapp.network.CommonClass;

public class VersusMenuActivity extends AppCompatActivity {
    CommonClass cc;
    SQLitehelper dbcon;
    LinearLayout ll_main;
    TextView tv_toolbar;
    TextView tv_new_formation, tv_open_saved, tv_duplicate, tv_quit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_versus_menu);
        cc = new CommonClass(this);
        dbcon = new SQLitehelper(this);

        tv_toolbar = (TextView) findViewById(R.id.tv_toolbar_title);
        ll_main= (LinearLayout) findViewById(R.id.ll_main);

        tv_new_formation = (TextView) findViewById(R.id.tv_new_formation);
        tv_open_saved = (TextView) findViewById(R.id.tv_open_saved);
        tv_duplicate = (TextView) findViewById(R.id.tv_duplicate);
        tv_quit = (TextView) findViewById(R.id.tv_quit);

        if (cc.loadPrefBoolean("isPlayer1Ready")) {
            tv_duplicate.setOnClickListener(m_OnClickListener);
            tv_duplicate.setBackgroundResource(R.drawable.tv_bg_solid);
            tv_toolbar.setText(getResources().getString(R.string.versus_mode_second));
        } else {
            tv_toolbar.setText(getResources().getString(R.string.versus_mode));
            tv_duplicate.setBackgroundResource(R.drawable.tv_bg_solid_grey);
        }

        tv_new_formation.setOnClickListener(m_OnClickListener);
        tv_open_saved.setOnClickListener(m_OnClickListener);

        tv_quit.setOnClickListener(m_OnClickListener);
    }

    public View.OnClickListener m_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.tv_new_formation:
                    cc.savePrefBoolean("fromOpenSavedFormation", false);
                    if (cc.loadPrefBoolean("haveAnyPlayer") && cc.loadPrefBoolean("isPlayer1Ready")) {
                        cc.savePrefBoolean("fromNewFormation", true);
                        startActivity(new Intent(VersusMenuActivity.this, ChooseNumberOfPlayersActivity.class));
                    } else {
                        startActivity(new Intent(VersusMenuActivity.this, ChooseNumberOfPlayersActivity.class));
                    }


                    break;
                case R.id.tv_open_saved:

                    cc.savePrefBoolean("fromOpenSavedFormation", true);
                    cc.savePrefBoolean("fromNewFormation", false);
//                    if(cc.loadPrefBoolean("haveAnyPlayer") && cc.loadPrefBoolean("isPlayer1Ready"))
                    startActivity(new Intent(VersusMenuActivity.this, OpenSavedFormationActivity.class));

                    break;
                case R.id.tv_duplicate:
                    cc.savePrefBoolean("fromNewFormation", false);
                    CommonClass.player2 = CommonClass.player1;
                    cc.savePrefBoolean("isPlayer2Ready", true);
                    cc.savePrefBoolean("fromDuplicate", true);
                    cc.savePrefInt("player2_T_Shirt", cc.loadPrefInt("player1_T_Shirt"));
                    cc.savePlayer2(cc.getPlayer1());
                    CommonClass.team2Name = CommonClass.team1Name;
                    duplicateFormation();

                    break;
                case R.id.tv_quit:
                    cc.savePrefBoolean("fromDuplicate", false);
                    cc.savePrefBoolean("fromNewFormation", false);
                    cc.savePrefBoolean("isPlayer2Ready", false);
                    cc.savePrefBoolean("haveAnyPlayer", false);
                    cc.savePrefBoolean("isPlayer1Ready", false);
                    onBackPressed();
                    break;
            }
        }
    };

    private void duplicateFormation() {

        Cursor cur = null;
        cur = dbcon.getTeam2();
        if (cur.getCount() != 0 && cur != null) {
            dbcon.deleteTeam2();
            dbcon.creataTeam2();
        }

        Cursor cursor = null;
        cursor = dbcon.getTeam1();
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                dbcon.insertTeam2(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(cursor.getColumnIndex("onField")));
                if (i != cursor.getCount() - 1)
                    cursor.moveToNext();
            }
        } else {
            cc.showToast("No Data Available");
        }

        renameDuplicateFormation();
    }

    private void renameDuplicateFormation() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        builder.setCancelable(false);

        View dialogView = inflater.inflate(R.layout.dialog_rename_player, null);
        builder.setView(dialogView);

        final android.support.v7.app.AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(true);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText et_rename_player = (EditText) dialogView.findViewById(R.id.et_player_name);
        et_rename_player.setText(CommonClass.team2Name);
        et_rename_player.setHint(getResources().getString(R.string.hint_choose_the_name_of_your_team));
        TextView tv_rename_title = (TextView) dialogView.findViewById(R.id.tv_rename_title);
        tv_rename_title.setText(getResources().getString(R.string.rename_duplicate_formation));

        ImageView btn_cancel = (ImageView) dialogView.findViewById(R.id.iv_cancel);
        ImageView btn_yes = (ImageView) dialogView.findViewById(R.id.iv_ok);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                startActivity(new Intent(VersusMenuActivity.this, VersusActivity.class));

            }
        });

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et_rename_player.getText().toString().equals("")){
                    alert.dismiss();
                    CommonClass.team2Name=et_rename_player.getText().toString();
                    startActivity(new Intent(VersusMenuActivity.this, VersusActivity.class));
                }else{
                    cc.showSnackbar(ll_main,getResources().getString(R.string.please_enter_formation_name));
                }
            }
        });
        alert.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cc.savePrefBoolean("isPlayer2Ready", false);
        cc.savePrefBoolean("haveAnyPlayer", false);
        cc.savePrefBoolean("isPlayer1Ready", false);
        cc.savePrefBoolean("fromNewFormation", false);
        OpenSavedFormationActivity.fromOpenSavedActivity = false;
        finish();
    }
}
