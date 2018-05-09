package com.mxi.lineapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mxi.lineapp.R;
import com.mxi.lineapp.adapter.ImageAdapter;
import com.mxi.lineapp.database.SQLitehelper;
import com.mxi.lineapp.model.OnSwipeTouchListener;
import com.mxi.lineapp.model.player;
import com.mxi.lineapp.network.CommonClass;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class VersusActivity extends AppCompatActivity implements View.OnLongClickListener {

    public static boolean fromVersusModeSaved1 = false;
    public static boolean fromVersusModeSaved2 = false;
    boolean fromTeam1 = false;
    int SPLASH_DISPLAY_TIME = 2000;

    protected PowerManager.WakeLock mWakeLock;
    TextView tv_v_mode_team_name_1, tv_v_mode_team_name_2;
    SQLitehelper dbcon;
    int tolranceWidth = 0;
    int heightDifference = 0;
    int widthDifference = 0;
    ArrayList<player> listPlayer1, listPlayer2;
    CommonClass cc;
    AbsoluteLayout ll_team_1, ll_team_2;
    FrameLayout ll_main;
    TextView tv_team_name_1, tv_team_name_2;
    ImageView iv_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_versus);
        cc = new CommonClass(this);
        dbcon = new SQLitehelper(this);
        tolranceWidth = cc.loadPrefInt("imageViewWidth") / 2;
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();

        tv_team_name_1 = (TextView) findViewById(R.id.tv_team_name_1);
        tv_team_name_2 = (TextView) findViewById(R.id.tv_team_name_2);
        tv_v_mode_team_name_1 = (TextView) findViewById(R.id.tv_v_mode_team_name_1);
        tv_v_mode_team_name_2 = (TextView) findViewById(R.id.tv_v_mode_team_name_2);
        iv_share = (ImageView) findViewById(R.id.iv_share);

        ll_main = (FrameLayout) findViewById(R.id.ll_main);
        ll_team_1 = (AbsoluteLayout) findViewById(R.id.ll_team_1);
        ll_team_2 = (AbsoluteLayout) findViewById(R.id.ll_team_2);

        String team1 = CommonClass.team1Name;
        String team2 = CommonClass.team2Name;

        tv_team_name_1.setText(team1);
        tv_team_name_2.setText(team2);
        tv_v_mode_team_name_1.setText(team1);
        tv_v_mode_team_name_2.setText(team2);

        if (!cc.loadPrefBoolean("isAlertOff")) {
            openIntroductoryAlert();
        }


        ll_team_1.setOnTouchListener(new OnSwipeTouchListener(VersusActivity.this) {
            public void onSwipeTop() {

            }

            public void onSwipeRight() {

                cc.savePrefBoolean("Team2", false);

                Intent mainIntent = new Intent(VersusActivity.this,
                        VersusSinglePlayerActivity.class);
                mainIntent.putExtra("id", "1");
                mainIntent.putExtra("team", "team1");
                mainIntent.putExtra("team_name", tv_v_mode_team_name_1.getText().toString().trim());
                startActivity(mainIntent);
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);


            }

            public void onSwipeLeft() {

            }

            public void onSwipeBottom() {

            }

        });


        ll_team_2.setOnTouchListener(new OnSwipeTouchListener(VersusActivity.this) {
            public void onSwipeTop() {
//                Toast.makeText(VersusActivity.this, "top", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeRight() {
//                Toast.makeText(VersusActivity.this, "right", Toast.LENGTH_SHORT).show();

            }

            public void onSwipeLeft() {
//                Toast.makeText(VersusActivity.this, "left", Toast.LENGTH_SHORT).show();
                cc.savePrefBoolean("Team2", false);
                Intent intent = new Intent(VersusActivity.this, VersusSinglePlayerActivity.class);
                intent.putExtra("team", "team2");
                intent.putExtra("team_name", tv_v_mode_team_name_2.getText().toString().trim());
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            }

            public void onSwipeBottom() {
            }

        });


        ll_team_1.post(new Runnable() {
            @Override
            public void run() {

                if (cc.loadPrefBoolean("isPlayer1Ready") && cc.loadPrefBoolean("isPlayer2Ready")) {

                    setUpPlayer1();
                    setUpPlayer2();

                    iv_share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            shareFormation();
                        }
                    });
                } else {
                    cc.showSnackbar(ll_main, "Both Players are not ready");
                }
            }
        });
    }

    private void openIntroductoryAlert() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(VersusActivity.this);

        LayoutInflater inflater = (LayoutInflater) VersusActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        builder.setCancelable(false);

        View dialogView = inflater.inflate(R.layout.dialog_itro_longpress, null);
        builder.setView(dialogView);

        final android.support.v7.app.AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(true);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btn_ok = (Button) dialogView.findViewById(R.id.btn_ok);
        ImageView iv_cancel = (ImageView) dialogView.findViewById(R.id.iv_cancel);
        CheckBox checkBox = (CheckBox) dialogView.findViewById(R.id.chk_donot_show_again);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cc.savePrefBoolean("isAlertOff", isChecked);
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }

    private void shareFormation() {

        ll_main.destroyDrawingCache();
        ll_main.setDrawingCacheEnabled(true);
        Bitmap bitmap = ll_main.getDrawingCache();

        String image_name = tv_team_name_1.getText().toString().trim() + "_" + cc.getCurrentDateandtime() + ".jpg";
        String imagePath = "";
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/Line/SharedFormations/");

        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        imagePath = mediaStorageDir + "/" + image_name;

        File file = new File(imagePath);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        File f = new File(imagePath);
        Uri uri = Uri.parse("file://" + f.getAbsolutePath());
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_by));
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share, "Share image File"));
    }


    private void setUpPlayer1() {
        listPlayer1 = new ArrayList<player>();
        heightDifference = getHeightDifference(true, ll_team_1);
        widthDifference = getHeightDifference(false, ll_team_1);

        Cursor cursor = null;
        cursor = dbcon.getTeam1();
        if (cursor.getCount() != 0 && cursor != null) {
            cursor.moveToFirst();

            do {

                player player_set = new player();
                player_set.setPlayer_id(Integer.parseInt(cursor.getString(0)));
                player_set.setPlayer_name(cursor.getString(1));
                player_set.setLeft_position(Integer.parseInt(cursor.getString(2)));
                player_set.setTop_position(Integer.parseInt(cursor.getString(3)));
                boolean onField = false;
                if (cursor.getInt(cursor.getColumnIndex("onField")) == 1) {

                    onField = true;
                }

                player_set.setOnField(onField);
                listPlayer1.add(player_set);

            } while (cursor.moveToNext());


        }


        for (int i = 0; i < listPlayer1.size(); i++) {

            player team_player = listPlayer1.get(i);

            if (team_player.isOnField()) {
                final int left = getAbsoluteLeft(team_player.getLeft_position());
                final int top = getAbsoluteTop(team_player.getTop_position());

                final TextView textView = new TextView(VersusActivity.this);
                textView.setText(team_player.getPlayer_name());
                textView.setTextColor(getResources().getColor(R.color.colorWhite));
                textView.setGravity(Gravity.CENTER_HORIZONTAL);

                ImageView image = new ImageView(VersusActivity.this);
                image.setImageResource(ImageAdapter.mThumbIds[cc.loadPrefInt("player1_T_Shirt")]);
                final LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(image);
                linearLayout.addView(textView);


/*                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.measure(0, 0);
                        int tv_width = textView.getMeasuredWidth() / 2;

                        if (tv_width <= tolranceWidth) {
                            tv_width = tolranceWidth;
                        }
                        AbsoluteLayout.LayoutParams params;
                        params = new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT, AbsoluteLayout.LayoutParams.WRAP_CONTENT, (left - tv_width), (top - heightDifference));

                        linearLayout.setOnLongClickListener(VersusActivity.this);

                        ll_team_1.addView(linearLayout, params);
                    }
                });*/

                textView.measure(0, 0);
                int tv_width = textView.getMeasuredWidth() / 2;

                if (tv_width <= tolranceWidth) {
                    tv_width = tolranceWidth;
                }
                AbsoluteLayout.LayoutParams params;
                params = new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT, AbsoluteLayout.LayoutParams.WRAP_CONTENT, (left - tv_width), (top - heightDifference));

                linearLayout.setOnLongClickListener(VersusActivity.this);

                ll_team_1.addView(linearLayout, params);
            }
        }
    }

    private void setUpPlayer2() {

        heightDifference = getHeightDifference(true, ll_team_2);
        widthDifference = getHeightDifference(false, ll_team_2);
        listPlayer2 = new ArrayList<player>();

        Cursor cursor = null;
        cursor = dbcon.getTeam2();
        if (cursor.getCount() != 0 && cursor != null) {
            cursor.moveToFirst();


            do {
                player player_set = new player();
                player_set.setPlayer_id(Integer.parseInt(cursor.getString(0)));
                player_set.setPlayer_name(cursor.getString(1));
                player_set.setLeft_position(Integer.parseInt(cursor.getString(2)));
                player_set.setTop_position(Integer.parseInt(cursor.getString(3)));
                boolean onField = false;
                if (cursor.getInt(cursor.getColumnIndex("onField")) == 1) {
                    onField = true;
                }

                player_set.setOnField(onField);
                listPlayer2.add(player_set);

            } while (cursor.moveToNext());
        }

        for (int i = 0; i < listPlayer2.size(); i++) {

            player team_player = listPlayer2.get(i);

            if (team_player.isOnField()) {

                final int left = getAbsoluteLeft(team_player.getLeft_position());
                final int top = getAbsoluteTop(team_player.getTop_position());

                final TextView textView = new TextView(VersusActivity.this);
                textView.setText(team_player.getPlayer_name());
                textView.setTextColor(getResources().getColor(R.color.colorWhite));
                textView.setGravity(Gravity.CENTER_HORIZONTAL);

                ImageView image = new ImageView(VersusActivity.this);
                image.setImageResource(ImageAdapter.mThumbIds[cc.loadPrefInt("player2_T_Shirt")]);
                final LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(image);
                linearLayout.addView(textView);


/*
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.measure(0, 0);
                        int tv_width = textView.getMeasuredWidth() / 2;

                        if (tv_width <= tolranceWidth) {
                            tv_width = tolranceWidth;
                        }
                        AbsoluteLayout.LayoutParams params;
                        params = new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT, AbsoluteLayout.LayoutParams.WRAP_CONTENT, (left - tv_width), (top - heightDifference));
                        linearLayout.setOnLongClickListener(VersusActivity.this);
                        ll_team_2.addView(linearLayout, params);
                    }
                });
*/
                textView.measure(0, 0);
                int tv_width = textView.getMeasuredWidth() / 2;

                if (tv_width <= tolranceWidth) {
                    tv_width = tolranceWidth;
                }
                AbsoluteLayout.LayoutParams params;
                params = new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT, AbsoluteLayout.LayoutParams.WRAP_CONTENT, (left - tv_width), (top - heightDifference));
                linearLayout.setOnLongClickListener(VersusActivity.this);
                ll_team_2.addView(linearLayout, params);

            }
        }
        cc.savePrefBoolean("fromDuplicate", false);
        OpenSavedFormationActivity.fromOpenSavedActivity = false;
    }


    private int getHeightDifference(boolean b, AbsoluteLayout ll_main) {

        int scr_height = 0;
        int difference = 0;
        int scr_width = 0;
        int lay_height = ll_main.getHeight();
        int lay_width = ll_main.getWidth();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        scr_height = displayMetrics.heightPixels;
        scr_width = displayMetrics.widthPixels;


        if (b) {
            difference = scr_height - lay_height;
        } else {
            difference = scr_width - lay_width;
        }

        return difference;
    }

    public int getAbsoluteLeft(int left) {
        int k = 0;
        int oldWidth = cc.loadPrefInt("SingleLayoutWidth");
//        int currentWidth = ll_team_1.getWidth() / 2;
        int currentWidth = ll_team_1.getWidth();
        k = (left * currentWidth) / oldWidth;
        return k;
    }

    public int getAbsoluteTop(int top) {
        int k = 0;
        int oldHeight = cc.loadPrefInt("SingleLayoutHeight");
        int currentHeight = ll_team_1.getHeight();
        k = (top * currentHeight) / oldHeight;
        return k;
    }

    @Override
    public void onBackPressed() {

        openExitPopup();

    }

    public void replaceAlertPopUp(final int arrayCount, final String playerName, final boolean fromTeam1) {
        ArrayList<player> playerList = new ArrayList<player>();
        final AbsoluteLayout ll_team;
        String id = "";
        player teamPlayer = new player();

        if (fromTeam1) {
            teamPlayer = listPlayer1.get(arrayCount);
            playerList = listPlayer1;
            ll_team = ll_team_1;
            id = dbcon.getPlayerIdFromTeam1(playerName);
        } else {
            teamPlayer = listPlayer2.get(arrayCount);
            playerList = listPlayer2;
            ll_team = ll_team_2;
            id = dbcon.getPlayerIdFromTeam2(playerName);
        }
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(VersusActivity.this);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        builder.setCancelable(false);

        View dialogView = inflater.inflate(R.layout.dialog_vs_replace_player, null);
        builder.setView(dialogView);

        final android.support.v7.app.AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(true);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final EditText et_rename_player = (EditText) dialogView.findViewById(R.id.et_player_name);
        et_rename_player.setText(playerName);

        ImageView btn_cancel = (ImageView) dialogView.findViewById(R.id.iv_cancel);
        ImageView btn_yes = (ImageView) dialogView.findViewById(R.id.iv_ok);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();


            }
        });

        final ArrayList<player> finalPlayerList = playerList;

        final player finalTeamPlayer = teamPlayer;
        final String finalId = id;
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int Xt = 0;
                int X_ = 0;

                String rename_name = et_rename_player.getText().toString().trim();
                boolean isNameMatched = false;
                if (rename_name.equals("")) {
                    et_rename_player.setError(getResources().getString(R.string.enter_player_name));
                } else {

                    for (int i = 0; i < finalPlayerList.size(); i++) {
                        if (rename_name.equals(finalPlayerList.get(i).getPlayer_name())) {
                            isNameMatched = true;
                        }
                    }

                    if (!isNameMatched) {
                        int child_count = 0;
                        int totalChildCount = ll_team.getChildCount();
                        View v = null;
                        for (int i = 0; i < totalChildCount; i++) {
                            v = ll_team.getChildAt(i);
                            if (v instanceof LinearLayout) {
                                LinearLayout ll = (LinearLayout) v;
                                View v1 = null;
                                int count_ll = ll.getChildCount();
                                for (int j = 0; j < count_ll; j++) {

                                    v1 = ll.getChildAt(j);

                                    if (v1 instanceof TextView) {
                                        final TextView textView = (TextView) v1;
                                        if (textView.getText().toString().equals(playerName)) {
                                            child_count = i;
                                            X_ = ll.getWidth() / 2;
                                            Xt = ll.getLeft();

                                            textView.setText(et_rename_player.getText().toString());

                                            finalTeamPlayer.setPlayer_name(et_rename_player.getText().toString());

                                            if (fromTeam1) {

                                                listPlayer1.set(arrayCount, finalTeamPlayer);
                                            } else {

                                                listPlayer2.set(arrayCount, finalTeamPlayer);
                                            }

                                            final int finalChild_count = child_count;
                                            final int finalX_ = X_;
                                            final int finalXt = Xt;

                                            textView.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    int x__ = 0;
                                                    int x_left = 0;
                                                    View v = ll_team.getChildAt(finalChild_count);
                                                    textView.measure(0, 0);
                                                    x__ = textView.getMeasuredWidth() / 2;


                                                    if (x__ >= tolranceWidth) {
                                                        x_left = finalXt - (x__ - finalX_);
                                                    } else {
                                                        x_left = finalXt - tolranceWidth;
                                                    }

                                                    if (fromTeam1) {
                                                        ll_team_1.updateViewLayout(ll_team_1.getChildAt(finalChild_count),
                                                                new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT, AbsoluteLayout.LayoutParams.WRAP_CONTENT, (x_left), (v.getTop())));
                                                        ll_team_1.invalidate();
                                                    } else {
                                                        ll_team_2.updateViewLayout(ll_team_2.getChildAt(finalChild_count),
                                                                new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT, AbsoluteLayout.LayoutParams.WRAP_CONTENT, (x_left), (v.getTop())));
                                                        ll_team_2.invalidate();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        }

                        if (fromTeam1) {
                            dbcon.UpdateTeam1(finalTeamPlayer.getPlayer_name(), finalTeamPlayer.getLeft_position() + "", finalTeamPlayer.getTop_position() + "", finalId, 1);
                        } else {
                            dbcon.UpdateTeam2(finalTeamPlayer.getPlayer_name(), finalTeamPlayer.getLeft_position() + "", finalTeamPlayer.getTop_position() + "", finalId, 1);
                        }

                        alert.dismiss();
                    } else {
                        et_rename_player.setError(getResources().getString(R.string.exist_player_name));
                    }

//                    updateFormation();
                }
            }
        });
        alert.show();

    }

    private void openExitPopup() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(VersusActivity.this);

        LayoutInflater inflater = (LayoutInflater) VersusActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        builder.setCancelable(false);

        View dialogView = inflater.inflate(R.layout.dialog_confirmation, null);
        builder.setView(dialogView);

        final android.support.v7.app.AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(true);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final TextView tv_confirm_text = (TextView) dialogView.findViewById(R.id.tv_confirm_text);
        tv_confirm_text.setText(getResources().getString(R.string.q_exit_versus_mode));
        Button btn_yes = (Button) dialogView.findViewById(R.id.btn_yes);
        Button btn_no = (Button) dialogView.findViewById(R.id.btn_no);
        ImageView iv_cancel = (ImageView) dialogView.findViewById(R.id.iv_cancel);


        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cc.savePrefBoolean("isPlayer1Ready", false);
                cc.savePrefBoolean("isPlayer2Ready", false);
                cc.savePrefBoolean("haveAnyPlayer", false);
                cc.savePrefBoolean("fromDuplicate", false);
                cc.savePrefBoolean("fromNewFormation", false);
                VersusSinglePlayerActivity.fromVersusSinglePlayer1 = false;
                VersusSinglePlayerActivity.fromVersusSinglePlayer2 = false;
                fromVersusModeSaved1 = false;
                fromVersusModeSaved2 = false;
                OpenSavedFormationActivity.fromOpenSavedActivity = false;

                dbcon.deleteTeam1();
                dbcon.deleteTeam2();

                alert.dismiss();
                Intent launchNextActivity;
                launchNextActivity = new Intent(VersusActivity.this, MainActivity.class);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(launchNextActivity);
                finish();

            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert.dismiss();
            }
        });
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert.dismiss();
            }
        });

        alert.show();

    }

    @Override
    public boolean onLongClick(View v) {


        int arrayCount = 0;


        if (v.getParent() == ll_team_1) {
            fromTeam1 = true;
        } else if (v.getParent() == ll_team_2) {
            fromTeam1 = false;
        }
        if (v instanceof LinearLayout) {
            LinearLayout ll = (LinearLayout) v;
            View v1 = null;
            int count_ll = ll.getChildCount();
            boolean isMatched = false;
            for (int j = 0; j < count_ll; j++) {

                v1 = ll.getChildAt(j);

                if (v1 instanceof TextView) {
                    TextView textView = (TextView) v1;
//                    cc.showToast(textView.getText().toString());

                    if (fromTeam1) {
                        for (int i = 0; i < listPlayer2.size(); i++) {
                            if (textView.getText().toString().equals(listPlayer2.get(i).getPlayer_name())) {
                                isMatched = true;
                            }
                        }
                    } else {
                        for (int i = 0; i < listPlayer1.size(); i++) {
                            if (textView.getText().toString().equals(listPlayer1.get(i).getPlayer_name())) {
                                isMatched = true;
                            }
                        }
                    }


                    if (!fromTeam1) {

                        for (int i = 0; i < listPlayer2.size(); i++) {
                            if (textView.getText().toString().equals(listPlayer2.get(i).getPlayer_name())) {
                                arrayCount = i;
                            }
                        }

                    } else {

                        for (int i = 0; i < listPlayer1.size(); i++) {

                            if (textView.getText().toString().equals(listPlayer1.get(i).getPlayer_name())) {
                                arrayCount = i;
                            }
                        }
                    }

                    if (!isMatched) {

                        replacePlayer(arrayCount, textView, fromTeam1);

                    } else {
//                        alertDialogbox();
                        replaceAlertPopUp(arrayCount, textView.getText().toString(), fromTeam1);
                    }

                }
            }
        }

        return true;
    }

    private void replacePlayer(final int arrayCount, final TextView textView, final boolean fromTeam1) {

        final ArrayList<String> listSubPlayer = new ArrayList<String>();
        ArrayAdapter<String> subAdapter;
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(VersusActivity.this);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        builder.setCancelable(false);

        View dialogView = inflater.inflate(R.layout.dialog_replace_player, null);
        builder.setView(dialogView);

        final android.support.v7.app.AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(true);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        ImageView btn_cancel = (ImageView) dialogView.findViewById(R.id.iv_cancel);
        final ListView lv_substitute_player = (ListView) dialogView.findViewById(R.id.lv_substitute_player);

        if (!fromTeam1) {
            for (int i = 0; i < listPlayer1.size(); i++) {
                if (listPlayer1.get(i).isOnField()) {
                    listSubPlayer.add(listPlayer1.get(i).getPlayer_name());
                }
            }
        } else {
            for (int i = 0; i < listPlayer2.size(); i++) {
                if (listPlayer2.get(i).isOnField()) {
                    listSubPlayer.add(listPlayer2.get(i).getPlayer_name());
                }
            }
        }


        subAdapter = new ArrayAdapter<String>(this, R.layout.raw_item_player_name, R.id.tv_raw_item_player_name, listSubPlayer);
        lv_substitute_player.setAdapter(subAdapter);


        lv_substitute_player.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                boolean isMatchFound = false;
//                cc.showToast(listSubPlayer.get(position).trim());

                if (fromTeam1) {

                    for (int i = 0; i < listPlayer1.size(); i++) {
                        if (listSubPlayer.get(position).equals(listPlayer1.get(i).getPlayer_name())) {
                            isMatchFound = true;
                        }
                    }

                } else {

                    for (int i = 0; i < listPlayer2.size(); i++) {
                        if (listSubPlayer.get(position).equals(listPlayer2.get(i).getPlayer_name())) {
                            isMatchFound = true;
                        }
                    }
                }

                if (!isMatchFound) {
                    if (!fromTeam1) {
                        int subCount = 0;
                        player Player1 = new player();
                        player Player2 = new player();

                        for (int i = 0; i < listPlayer1.size(); i++) {
                            if (listSubPlayer.get(position).equals(listPlayer1.get(i).getPlayer_name())) {
                                subCount = i;
                            }
                        }


                        Player1.setPlayer_id(listPlayer2.get(arrayCount).getPlayer_id());
                        Player1.setPlayer_name(listPlayer2.get(arrayCount).getPlayer_name());
                        Player1.setTop_position(listPlayer2.get(arrayCount).getTop_position());
                        Player1.setLeft_position(listPlayer2.get(arrayCount).getLeft_position());
                        Player1.setOnField(listPlayer2.get(arrayCount).isOnField());

                        Player2.setPlayer_name(listPlayer1.get(subCount).getPlayer_name());
                        Player2.setPlayer_id(listPlayer1.get(subCount).getPlayer_id());
                        Player2.setTop_position(listPlayer1.get(subCount).getTop_position());
                        Player2.setLeft_position(listPlayer1.get(subCount).getLeft_position());
                        Player2.setOnField(listPlayer1.get(subCount).isOnField());

                        UpdateView(ll_team_1, Player2.getPlayer_name(), Player1.getPlayer_name());
                        UpdateView(ll_team_2, Player1.getPlayer_name(), Player2.getPlayer_name());

                        listPlayer2.get(arrayCount).setPlayer_name(Player2.getPlayer_name());
                        listPlayer2.get(arrayCount).setTop_position(Player2.getTop_position());
                        listPlayer2.get(arrayCount).setLeft_position(Player2.getLeft_position());

                        listPlayer1.get(subCount).setPlayer_name(Player1.getPlayer_name());
                        listPlayer1.get(subCount).setTop_position(Player1.getTop_position());
                        listPlayer1.get(subCount).setLeft_position(Player1.getLeft_position());

                        String id1 = dbcon.getPlayerIdFromTeam1(Player2.getPlayer_name());
                        String id2 = dbcon.getPlayerIdFromTeam2(Player1.getPlayer_name());

                        dbcon.UpdateTeam2(Player2.getPlayer_name(), Player1.getLeft_position() + "", Player1.getTop_position() + "", id2, 1);
                        dbcon.UpdateTeam1(Player1.getPlayer_name(), Player2.getLeft_position() + "", Player2.getTop_position() + "", id1, 1);
                        alert.dismiss();

                    } else {

                        int subCount = 0;
                        player Player1 = new player();
                        player Player2 = new player();

                        for (int i = 0; i < listPlayer2.size(); i++) {
                            if (listSubPlayer.get(position).equals(listPlayer2.get(i).getPlayer_name())) {
                                subCount = i;
                            }
                        }


                        Player1.setPlayer_id(listPlayer1.get(arrayCount).getPlayer_id());
                        Player1.setPlayer_name(listPlayer1.get(arrayCount).getPlayer_name());
                        Player1.setTop_position(listPlayer1.get(arrayCount).getTop_position());
                        Player1.setLeft_position(listPlayer1.get(arrayCount).getLeft_position());
                        Player1.setOnField(listPlayer1.get(arrayCount).isOnField());

                        Player2.setPlayer_name(listPlayer2.get(subCount).getPlayer_name());
                        Player2.setPlayer_id(listPlayer2.get(subCount).getPlayer_id());
                        Player2.setTop_position(listPlayer2.get(subCount).getTop_position());
                        Player2.setLeft_position(listPlayer2.get(subCount).getLeft_position());
                        Player2.setOnField(listPlayer2.get(subCount).isOnField());


                        UpdateView(ll_team_1, Player1.getPlayer_name(), Player2.getPlayer_name());
                        UpdateView(ll_team_2, Player2.getPlayer_name(), Player1.getPlayer_name());

                        listPlayer1.get(arrayCount).setPlayer_name(Player2.getPlayer_name());
                        listPlayer1.get(arrayCount).setTop_position(Player2.getTop_position());
                        listPlayer1.get(arrayCount).setLeft_position(Player2.getLeft_position());

                        listPlayer2.get(subCount).setPlayer_name(Player1.getPlayer_name());
                        listPlayer2.get(subCount).setTop_position(Player1.getTop_position());
                        listPlayer2.get(subCount).setLeft_position(Player1.getLeft_position());

                        String id1 = dbcon.getPlayerIdFromTeam1(Player1.getPlayer_name());
                        String id2 = dbcon.getPlayerIdFromTeam2(Player2.getPlayer_name());

                        dbcon.UpdateTeam1(Player2.getPlayer_name(), Player1.getLeft_position() + "", Player1.getTop_position() + "", id1, 1);
                        dbcon.UpdateTeam2(Player1.getPlayer_name(), Player2.getLeft_position() + "", Player2.getTop_position() + "", id2, 1);
                        alert.dismiss();
                    }
                } else {
                    alert.dismiss();
                    alertDialogbox();
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }

    private void alertDialogbox() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Alert!");
        builder1.setIcon(getResources().getDrawable(R.mipmap.alert));
        builder1.setMessage("You can’t make this substitution because there is another player who has the same name in the opposite team. Please rename it if you want to continue.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void UpdateView(final AbsoluteLayout ll_team, String player_name1, String player_name2) {
        int Xt = 0;
        int X_ = 0;
        int child_count = 0;
        int totalChildCount = ll_team.getChildCount();
        View v = null;
        for (int i = 0; i < totalChildCount; i++) {
            v = ll_team.getChildAt(i);
            if (v instanceof LinearLayout) {
                LinearLayout ll = (LinearLayout) v;
                View v1 = null;
                int count_ll = ll.getChildCount();
                for (int j = 0; j < count_ll; j++) {

                    v1 = ll.getChildAt(j);

                    if (v1 instanceof TextView) {
                        final TextView textView = (TextView) v1;
                        if (textView.getText().toString().equals(player_name1)) {
                            child_count = i;
                            X_ = ll.getWidth() / 2;
                            Xt = ll.getLeft();

                            textView.setText(player_name2);

                            final int finalChild_count = child_count;
                            final int finalX_ = X_;
                            final int finalXt = Xt;

                            textView.post(new Runnable() {
                                @Override
                                public void run() {
                                    int x__ = 0;
                                    int x_left = 0;
                                    View v = ll_team.getChildAt(finalChild_count);
                                    textView.measure(0, 0);
                                    x__ = textView.getMeasuredWidth() / 2;


                                    if (x__ >= tolranceWidth) {
                                        x_left = finalXt - (x__ - finalX_);
                                    } else {
                                        x_left = finalXt - tolranceWidth;
                                    }

                                    if (ll_team.equals(ll_team_1)) {
                                        ll_team_1.updateViewLayout(ll_team_1.getChildAt(finalChild_count),
                                                new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT, AbsoluteLayout.LayoutParams.WRAP_CONTENT, (x_left), (v.getTop())));
                                        ll_team_1.invalidate();
                                    } else if (ll_team.equals(ll_team_2)) {
                                        ll_team_2.updateViewLayout(ll_team_2.getChildAt(finalChild_count),
                                                new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT, AbsoluteLayout.LayoutParams.WRAP_CONTENT, (x_left), (v.getTop())));
                                        ll_team_2.invalidate();
                                    }
                                }
                            });
                        }
                    }
                }
            }
        }
    }
}
