package com.mxi.lineapp.activity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mxi.lineapp.R;
import com.mxi.lineapp.adapter.ExpandableListAdapter;
import com.mxi.lineapp.adapter.ImageAdapter;
import com.mxi.lineapp.database.SQLitehelper;
import com.mxi.lineapp.dragevent.DragController;
import com.mxi.lineapp.dragevent.DragLayer;
import com.mxi.lineapp.model.player;
import com.mxi.lineapp.network.CommonClass;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VersusSinglePlayerActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    FrameLayout fl_capture_layout;
    //    public static boolean fromVersusSingle=false;
    public static boolean fromVersusSinglePlayer1 = false;
    public static boolean fromVersusSinglePlayer2 = false;
    boolean team1 = false;
    String team = "";
    int t_shirt_color = 0;
    protected PowerManager.WakeLock mWakeLock;
    List<String> mainSquad;
    List<String> substitute;
    List<String> listDataHeader;
    ExpandableListAdapter listAdapter;
    HashMap<String, List<String>> listDataChild;
    int temp = 0;
    int x_touch = 0;
    int y_touch = 0;
    int heightDifference = 0;
    int widthDifference = 0;
    //    ArrayAdapter adapter;
    int total_players = 0;
    int lay_height = 0;
    int lay_width = 0;
    int scr_width = 0;
    int scr_height = 0;
    int tolranceWidth = 0;
    CommonClass cc;
    boolean isFromGIF = false;
    boolean isFromBackPressed = false;
    ArrayList<player> playerArrayList;
    SQLitehelper dbcon;
    boolean drawerOpen = false;
    LinearLayoutManager llm;
    LinearLayout ll_nav_view;
    LinearLayout activity_single_player;
    TextView tv_team_name;
    ExpandableListView lvExp;
    int total_players_in_formation = 0;
    ImageView iv_nav, iv_add_player, iv_change_no_of_player, iv_change_colour;
    ImageView iv_save, iv_animation, iv_versus, iv_share;
    //    TextView tv_nav_main_menu, tv_nav_open_saved, tv_nav_share, tv_nav_quit;
    private DragController mDragController;   // Object that sends out drag-drop events while a view is being moved.
    private DragLayer mDragLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_versus_single_player);
        mDragController = new DragController(this);
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();

        cc = new CommonClass(this);
        tolranceWidth = cc.loadPrefInt("imageViewWidth") / 2;
        dbcon = new SQLitehelper(this);

        team = getIntent().getStringExtra("team");
        if (team.equals("team1")) {
            cc.savePrefBoolean("Team2", false);
            team1 = true;
            t_shirt_color = cc.loadPrefInt("player1_T_Shirt");
        } else {
            cc.savePrefBoolean("Team2", true);
            t_shirt_color = cc.loadPrefInt("player2_T_Shirt");
            team1 = false;
        }

        String team_name = getIntent().getStringExtra("team_name");

        llm = new LinearLayoutManager(this);

        tv_team_name = (TextView) findViewById(R.id.tv_team_name);
        fl_capture_layout = (FrameLayout) findViewById(R.id.fl_capture_layout);

        ll_nav_view = (LinearLayout) findViewById(R.id.ll_nav_view);
        activity_single_player = (LinearLayout) findViewById(R.id.activity_single_player);
        lvExp = (ExpandableListView) findViewById(R.id.lvExp);
        iv_add_player = (ImageView) findViewById(R.id.iv_add_player);
        iv_change_no_of_player = (ImageView) findViewById(R.id.iv_change_no_of_player);
        iv_change_colour = (ImageView) findViewById(R.id.iv_change_colour);
        iv_save = (ImageView) findViewById(R.id.iv_save);
        iv_animation = (ImageView) findViewById(R.id.iv_animation);
        iv_versus = (ImageView) findViewById(R.id.iv_versus);
        iv_share = (ImageView) findViewById(R.id.iv_share);

        tv_team_name.setText(team_name);

        setUpViews();
        setListeners();

        ll_nav_view.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                final int action = event.getAction();
                switch (action) {

                    case DragEvent.ACTION_DRAG_STARTED:
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        break;

                    case DragEvent.ACTION_DROP: {

                        return (true);
                    }

                    case DragEvent.ACTION_DRAG_ENDED: {

                        return (true);
                    }
                    default:
                        break;
                }
                return true;
            }
        });


        mDragLayer.post(new Runnable() {
            public void run() {
                lay_height = mDragLayer.getHeight();
                lay_width = mDragLayer.getWidth();
                setUpPlayerFirstTime();
                setUpSlidePanel();
            }
        });

    }

    @Override
    public void onDestroy() {
        this.mWakeLock.release();
        super.onDestroy();
    }

    private void setUpSlidePanel() {
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        lvExp.setAdapter(listAdapter);

        lvExp.expandGroup(0);
        lvExp.expandGroup(1);


        lvExp.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                boolean inIfTrue = false;
                if (view instanceof LinearLayout) {

                    LinearLayout linearLayout = (LinearLayout) view;
                    int count_item = linearLayout.getChildCount();

                    View lv = linearLayout.getChildAt(0);
                    TextView sliderTextView = (TextView) lv;

                    int count = mDragLayer.getChildCount();


                    View v = null;
                    for (int i = 0; i < count; i++) {
                        v = mDragLayer.getChildAt(i);
                        if (v instanceof LinearLayout) {
                            LinearLayout ll = (LinearLayout) v;
                            View v1 = null;
                            int count_ll = ll.getChildCount();
                            for (int j = 0; j < count_ll; j++) {

                                v1 = ll.getChildAt(j);

                                if (v1 instanceof TextView) {

                                    TextView textView = (TextView) v1;
                                    int arrayPosition = getArrayPositionFromString(sliderTextView.getText().toString().trim());
                                    if (sliderTextView.getText().equals(textView.getText())) {
                                        inIfTrue = true;
                                        showListOptionDialog(VersusSinglePlayerActivity.this, getRelativeLeft(sliderTextView), getRelativeTop(sliderTextView), sliderTextView, textView, position, i, arrayPosition);
                                    } else {

                                        if(sliderTextView.getText().toString().equals("Main Team") || sliderTextView.getText().toString().equals("Reserve Players")){
                                            inIfTrue = true;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (!inIfTrue) {
                        int arrayPosition = getArrayPositionFromString(sliderTextView.getText().toString().trim());
                        showSubListOptionDialog(VersusSinglePlayerActivity.this, getRelativeLeft(sliderTextView), getRelativeTop(sliderTextView), sliderTextView, arrayPosition);
                    }
                }
                return true;
            }

            private int getArrayPositionFromString(String name) {
                int position = 0;

                for (int i = 0; i < playerArrayList.size(); i++) {
                    if (name.equals(playerArrayList.get(i).getPlayer_name())) {
                        position = i;
                    }
                }
                return position;
            }
        });


    }

    private void showSubListOptionDialog(Context context, int x, int y, final TextView sliderTextView, final int arrayCount) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_menu_list);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP | Gravity.LEFT;
        int half_w = sliderTextView.getWidth();
        int half_t = sliderTextView.getTop();
        wmlp.x = x + half_w / 2;
        wmlp.y = y + half_t / 2;

        dialog.show();

        TextView tv_copy_text = (TextView) dialog.findViewById(R.id.tv_copy_text);
        TextView tv_rename = (TextView) dialog.findViewById(R.id.tv_rename);
        TextView tv_delete_player = (TextView) dialog.findViewById(R.id.tv_delete_player);
        TextView tv_replace_player = (TextView) dialog.findViewById(R.id.tv_replace_player);
//        tv_replace_player.setVisibility(View.GONE);

        tv_copy_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Temp", sliderTextView.getText().toString().trim());
                clipboard.setPrimaryClip(clip);
                cc.showToast("Player Name is copied");
                dialog.dismiss();
            }
        });

        tv_rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renameSubPlayer(sliderTextView, arrayCount);
                dialog.dismiss();
            }
        });

        tv_replace_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                replaceSubPlayer(arrayCount, sliderTextView);

            }
        });

        tv_delete_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(VersusSinglePlayerActivity.this);
                LayoutInflater inflater = (LayoutInflater) VersusSinglePlayerActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                builder.setCancelable(false);
                View dialogView = inflater.inflate(R.layout.dialog_confirmation, null);
                builder.setView(dialogView);
                final android.support.v7.app.AlertDialog alert = builder.create();
                alert.setCanceledOnTouchOutside(true);
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                final TextView tv_confirm_text = (TextView) dialogView.findViewById(R.id.tv_confirm_text);
                tv_confirm_text.setText(getResources().getString(R.string.q_delete_player));
                Button btn_yes = (Button) dialogView.findViewById(R.id.btn_yes);
                Button btn_no = (Button) dialogView.findViewById(R.id.btn_no);
                ImageView iv_cancel = (ImageView) dialogView.findViewById(R.id.iv_cancel);

                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (total_players_in_formation < playerArrayList.size()) {
                            if (team1) {
                                dbcon.deletePlayerTeam1(playerArrayList.get(arrayCount).getPlayer_name());
                            } else {
                                dbcon.deletePlayerTeam2(playerArrayList.get(arrayCount).getPlayer_name());
                            }

                            playerArrayList.remove(arrayCount);
                            dialog.dismiss();
                            setUpExpandable();
                            listAdapter.notifyDataSetChanged();
                        } else {
                            dialog.dismiss();
                            cc.showSnackbar(activity_single_player, "Minimum number of players limit in the formation is reached");
                        }
                        alert.dismiss();
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
        });
    }


    private void renameSubPlayer(TextView sliderTextView, final int arrayCount) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(VersusSinglePlayerActivity.this);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        builder.setCancelable(false);

        View dialogView = inflater.inflate(R.layout.dialog_rename_player, null);
        builder.setView(dialogView);

        final android.support.v7.app.AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(true);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final EditText et_rename_player = (EditText) dialogView.findViewById(R.id.et_player_name);
        et_rename_player.setText(sliderTextView.getText().toString().trim());

        ImageView btn_cancel = (ImageView) dialogView.findViewById(R.id.iv_cancel);
        ImageView btn_yes = (ImageView) dialogView.findViewById(R.id.iv_ok);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();


            }
        });

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rename_name = et_rename_player.getText().toString().trim();
                boolean isNameMatched = false;
                if (rename_name.equals("")) {
                    et_rename_player.setError(getResources().getString(R.string.enter_player_name));
                } else {

                    for (int i = 0; i < playerArrayList.size(); i++) {
                        if (rename_name.equals(playerArrayList.get(i).getPlayer_name())) {
                            isNameMatched = true;
                        }
                    }

                    if (!isNameMatched) {
                        playerArrayList.get(arrayCount).setPlayer_name(et_rename_player.getText().toString().trim());
                        setUpExpandable();
                        listAdapter.notifyDataSetChanged();
                        alert.dismiss();
                    } else {
                        et_rename_player.setError(getResources().getString(R.string.exist_player_name));
                    }
                }

            }
        });
        alert.show();

    }


    public void replaceSubPlayer(final int arrayCount, final TextView tv_player) {
        final ArrayList<String> substituteNames;
        ArrayAdapter<String> subAdapter;
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(VersusSinglePlayerActivity.this);

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
        substituteNames = new ArrayList<String>();
        for (int i = 0; i < playerArrayList.size(); i++) {
            if (playerArrayList.get(i).isOnField()) {
                substituteNames.add(playerArrayList.get(i).getPlayer_name());
            }
        }

        subAdapter = new ArrayAdapter<String>(this, R.layout.raw_item_player_name, R.id.tv_raw_item_player_name, substituteNames);
        lv_substitute_player.setAdapter(subAdapter);

        lv_substitute_player.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int sub_id = 0;
                for (int i = 0; i < playerArrayList.size(); i++) {
                    if (substituteNames.get(position).trim().equals(playerArrayList.get(i).getPlayer_name())) {
                        sub_id = i;
                    }
                }

                int off_left = playerArrayList.get(arrayCount).getLeft_position();
                int off_top = playerArrayList.get(arrayCount).getTop_position();
                int on_left = playerArrayList.get(sub_id).getLeft_position();
                int on_top = playerArrayList.get(sub_id).getTop_position();

                playerArrayList.get(arrayCount).setOnField(true);
                playerArrayList.get(arrayCount).setLeft_position(on_left);
                playerArrayList.get(arrayCount).setTop_position(on_top);


                playerArrayList.get(sub_id).setOnField(false);


                tv_player.setText(playerArrayList.get(sub_id).getPlayer_name());


                int count = mDragLayer.getChildCount();


                View v = null;
                int Xt = 0;
                int X_ = 0;
                for (int i = 0; i < count; i++) {
                    v = mDragLayer.getChildAt(i);
                    if (v instanceof LinearLayout) {
                        LinearLayout ll = (LinearLayout) v;
                        View v1 = null;
                        int count_ll = ll.getChildCount();
                        for (int j = 0; j < count_ll; j++) {

                            v1 = ll.getChildAt(j);

                            if (v1 instanceof TextView) {

                                final TextView textView = (TextView) v1;
                                if (textView.getText().toString().equals(playerArrayList.get(sub_id).getPlayer_name())) {

                                    X_ = ll.getWidth() / 2;
                                    Xt = ll.getLeft();

                                    textView.setText(playerArrayList.get(arrayCount).getPlayer_name());

                                    final int finalXt = Xt;
                                    final int finalX_ = X_;
                                    final int finalI = i;
                                    textView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            int x__ = 0;
                                            int x_left = 0;
                                            View v = mDragLayer.getChildAt(finalI);
                                            textView.measure(0, 0);
                                            x__ = textView.getMeasuredWidth() / 2;

                                            if (x__ >= tolranceWidth) {
                                                x_left = finalXt - (x__ - finalX_);
                                            } else {
                                                x_left = finalXt - tolranceWidth;
                                            }

                                            mDragLayer.updateViewLayout(mDragLayer.getChildAt(finalI),
                                                    new DragLayer.LayoutParams(DragLayer.LayoutParams.WRAP_CONTENT, DragLayer.LayoutParams.WRAP_CONTENT, (x_left), (v.getTop())));
                                            mDragLayer.invalidate();
                                        }
                                    });
                                }
                            }
                        }
                    }
                }

                setUpExpandable();
                listAdapter.notifyDataSetChanged();
                updateFormation();
                alert.dismiss();
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


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Main Team");
        listDataHeader.add("Reserve Players");


        // Adding child data
        mainSquad = new ArrayList<String>();
        substitute = new ArrayList<String>();

        setUpExpandable();


    }

    private void setUpExpandable() {
        if (!mainSquad.isEmpty()) {
            mainSquad.clear();
        }
        if (!substitute.isEmpty()) {
            substitute.clear();
        }

        for (int i = 0; i < playerArrayList.size(); i++) {
            if (playerArrayList.get(i).isOnField()) {
                mainSquad.add(playerArrayList.get(i).getPlayer_name());
            } else if (!playerArrayList.get(i).isOnField()) {
                substitute.add(playerArrayList.get(i).getPlayer_name());
            }
        }
        listDataChild.put(listDataHeader.get(0), mainSquad);
        listDataChild.put(listDataHeader.get(1), substitute);
    }

    private void showListOptionDialog(Context context, int x, int y, final TextView sliderTextView, final TextView textView, final int position, final int child_count, final int arrayCount) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_menu_list);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP | Gravity.LEFT;
        int half_w = sliderTextView.getWidth();
        int half_t = sliderTextView.getTop();
        wmlp.x = x + half_w / 2;
        wmlp.y = y + half_t / 2;

        dialog.show();

        TextView tv_copy_text = (TextView) dialog.findViewById(R.id.tv_copy_text);
        TextView tv_rename = (TextView) dialog.findViewById(R.id.tv_rename);
        TextView tv_delete_player = (TextView) dialog.findViewById(R.id.tv_delete_player);
        TextView tv_replace_player = (TextView) dialog.findViewById(R.id.tv_replace_player);

        tv_copy_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Temp", sliderTextView.getText().toString().trim());
                clipboard.setPrimaryClip(clip);
                cc.showToast("Player Name is copied");

            }
        });

        tv_rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                renamePlayer(sliderTextView, textView, arrayCount, child_count);
            }
        });


        tv_replace_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int counter = 0;
                int anti_counter = 0;

                dialog.dismiss();
                replacePlayer(arrayCount, textView, child_count);
                for (int i = 0; i < playerArrayList.size(); i++) {

                    if (playerArrayList.get(i).isOnField()) {
                        counter = counter + 1;
                    } else {
                        anti_counter = anti_counter + 1;
                    }

                }
            }

        });

        tv_delete_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(VersusSinglePlayerActivity.this);

                LayoutInflater inflater = (LayoutInflater) VersusSinglePlayerActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                builder.setCancelable(false);

                View dialogView = inflater.inflate(R.layout.dialog_confirmation, null);
                builder.setView(dialogView);

                final android.support.v7.app.AlertDialog alert = builder.create();
                alert.setCanceledOnTouchOutside(true);
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                final TextView tv_confirm_text = (TextView) dialogView.findViewById(R.id.tv_confirm_text);
                tv_confirm_text.setText(getResources().getString(R.string.q_delete_player));
                Button btn_yes = (Button) dialogView.findViewById(R.id.btn_yes);
                Button btn_no = (Button) dialogView.findViewById(R.id.btn_no);
                ImageView iv_cancel = (ImageView) dialogView.findViewById(R.id.iv_cancel);


                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (total_players_in_formation < playerArrayList.size()) {

                            if (team1) {
                                dbcon.deletePlayerTeam1(playerArrayList.get(arrayCount).getPlayer_name());
                            } else {
                                dbcon.deletePlayerTeam2(playerArrayList.get(arrayCount).getPlayer_name());
                            }

                            mDragLayer.removeViewAt(child_count);
                            playerArrayList.remove(arrayCount);
                            dialog.dismiss();


                            if (mDragLayer.getChildCount() < total_players_in_formation) {

                                for (int i = 0; i < playerArrayList.size(); i++) {

                                    player team_player = playerArrayList.get(i);

                                    if (!team_player.isOnField()) {
                                        int left = team_player.getLeft_position();
                                        int top = team_player.getTop_position();

                                        DragLayer.LayoutParams params;

                                        params = new DragLayer.LayoutParams(DragLayer.LayoutParams.WRAP_CONTENT, DragLayer.LayoutParams.WRAP_CONTENT, (left - widthDifference), (top - heightDifference));
                                        team_player.setOnField(true);
                                        TextView textView = new TextView(VersusSinglePlayerActivity.this);
                                        textView.setText(team_player.getPlayer_name());
                                        textView.setTextColor(getResources().getColor(R.color.colorWhite));

//                                        textView.setLayoutParams(new LinearLayout.LayoutParams(tolranceWidth*2, LinearLayout.LayoutParams.WRAP_CONTENT));

                                        textView.setGravity(Gravity.CENTER_HORIZONTAL);

                                        ImageView image = new ImageView(VersusSinglePlayerActivity.this);
                                        image.setImageResource(ImageAdapter.mThumbIds[cc.loadPrefInt("t_shirt_position")]);
                                        LinearLayout linearLayout = new LinearLayout(VersusSinglePlayerActivity.this);
                                        linearLayout.setGravity(Gravity.CENTER);
                                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                                        linearLayout.addView(image);
                                        linearLayout.addView(textView);

                                        mDragLayer.addView(linearLayout, child_count, params);

                                        linearLayout.setOnClickListener(VersusSinglePlayerActivity.this);
                                        linearLayout.setOnTouchListener(VersusSinglePlayerActivity.this);
                                        break;
                                    }
                                }
                            }
                            dialog.dismiss();
                            setUpExpandable();
                            listAdapter.notifyDataSetChanged();
                        } else {
                            dialog.dismiss();
                            cc.showSnackbar(activity_single_player, "Minimum number of players limit in the formation is reached");
                        }
                        alert.dismiss();
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
        });
    }

    public void replacePlayer(final int arrayCount, final TextView tv_player, final int child_count) {
        final ArrayList<String> substituteNames;
        ArrayAdapter<String> subAdapter;
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(VersusSinglePlayerActivity.this);

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
        substituteNames = new ArrayList<String>();
        for (int i = 0; i < playerArrayList.size(); i++) {
            if (!playerArrayList.get(i).isOnField()) {
                substituteNames.add(playerArrayList.get(i).getPlayer_name());
            }
        }

        subAdapter = new ArrayAdapter<String>(this, R.layout.raw_item_player_name, R.id.tv_raw_item_player_name, substituteNames);
        lv_substitute_player.setAdapter(subAdapter);

        lv_substitute_player.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int sub_id = 0;
                for (int i = 0; i < playerArrayList.size(); i++) {
                    if (substituteNames.get(position).trim().equals(playerArrayList.get(i).getPlayer_name())) {
                        sub_id = i;
                    }
                }

                final int off_left = playerArrayList.get(arrayCount).getLeft_position();
                final int off_top = playerArrayList.get(arrayCount).getTop_position();
                int on_left = playerArrayList.get(sub_id).getLeft_position();
                int on_top = playerArrayList.get(sub_id).getTop_position();

                playerArrayList.get(arrayCount).setOnField(false);
                playerArrayList.get(sub_id).setOnField(true);
                playerArrayList.get(sub_id).setLeft_position(off_left);
                playerArrayList.get(sub_id).setTop_position(off_top);


                int Xt = 0;
                int X_ = 0;

                LinearLayout ll_md_v = null;

                View md_v = mDragLayer.getChildAt(child_count);
                if (md_v instanceof LinearLayout) {
                    ll_md_v = (LinearLayout) md_v;
                    X_ = ll_md_v.getWidth() / 2;
                    Xt = ll_md_v.getLeft();
                }


                tv_player.setText(playerArrayList.get(sub_id).getPlayer_name());


                final int finalXt = Xt;
                final int finalX_ = X_;

                tv_player.post(new Runnable() {
                    @Override
                    public void run() {
                        int x__ = 0;
                        int x_left = 0;
                        View v = mDragLayer.getChildAt(child_count);
                        tv_player.measure(0, 0);
                        x__ = tv_player.getMeasuredWidth() / 2;

                        if (x__ >= tolranceWidth) {
                            x_left = finalXt - (x__ - finalX_);
                        } else {
                            x_left = finalXt - tolranceWidth;
                        }

                        mDragLayer.updateViewLayout(mDragLayer.getChildAt(child_count),
                                new DragLayer.LayoutParams(DragLayer.LayoutParams.WRAP_CONTENT, DragLayer.LayoutParams.WRAP_CONTENT, (x_left), (v.getTop())));
                        mDragLayer.invalidate();
                        updateFormation();
                    }
                });

                setUpExpandable();
                listAdapter.notifyDataSetChanged();

                alert.dismiss();
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isFromBackPressed = true;
        updateFormation();

    }

    private void updateFormation() {
        if (team1) {
            fromVersusSinglePlayer1 = true;
        } else {
            fromVersusSinglePlayer2 = true;
        }


        for (int i = 0; i < playerArrayList.size(); i++) {
            player update_player = playerArrayList.get(i);


            if (team1) {
                if (update_player.isOnField()) {

                    int off_left = 0;
                    int off_top = 0;
                    int tv_width = 0;

                    int count = mDragLayer.getChildCount();


                    View v0 = null;
                    for (int p = 0; p < count; p++) {
                        v0 = mDragLayer.getChildAt(p);
                        if (v0 instanceof LinearLayout) {
                            LinearLayout ll = (LinearLayout) v0;
                            View v1 = null;
                            int count_ll = ll.getChildCount();
                            for (int j = 0; j < count_ll; j++) {

                                v1 = ll.getChildAt(j);

                                if (v1 instanceof TextView) {

                                    TextView textView = (TextView) v1;
                                    if (update_player.getPlayer_name().equals(textView.getText())) {
                                        off_left = mDragLayer.getChildAt(p).getLeft();
                                        off_top = mDragLayer.getChildAt(p).getTop();

                                        tv_width = textView.getWidth() / 2;
                                    }
                                }
                            }
                        }
                    }

                    if (tv_width <= tolranceWidth) {
                        tv_width = tolranceWidth;
                    }

                    playerArrayList.get(i).setLeft_position(off_left + tv_width);
                    playerArrayList.get(i).setTop_position(off_top + heightDifference);
                    dbcon.UpdateTeam1(update_player.getPlayer_name(), (off_left + tv_width) + "", (off_top + heightDifference) + "", update_player.getPlayer_id() + "", 1);
//                    dbcon.UpdateTeam1(update_player.getPlayer_name(), (off_left + tolranceWidth) + "", (off_top + heightDifference) + "", update_player.getPlayer_id() + "", 1);

                } else {

                    playerArrayList.get(i).setLeft_position(mDragLayer.getWidth() / 2);
                    playerArrayList.get(i).setTop_position(mDragLayer.getHeight() / 2);
                    dbcon.UpdateTeam1(update_player.getPlayer_name(), mDragLayer.getWidth() / 2 + "", mDragLayer.getHeight() / 2 + "", update_player.getPlayer_id() + "", 0);
                }
            } else {
                if (update_player.isOnField()) {

                    int off_left = 0;
                    int off_top = 0;
                    int tv_width = 0;

                    int count = mDragLayer.getChildCount();


                    View v0 = null;
                    for (int p = 0; p < count; p++) {
                        v0 = mDragLayer.getChildAt(p);
                        if (v0 instanceof LinearLayout) {
                            LinearLayout ll = (LinearLayout) v0;
                            View v1 = null;
                            int count_ll = ll.getChildCount();
                            for (int j = 0; j < count_ll; j++) {

                                v1 = ll.getChildAt(j);

                                if (v1 instanceof TextView) {

                                    TextView textView = (TextView) v1;
                                    if (update_player.getPlayer_name().equals(textView.getText())) {
                                        off_left = mDragLayer.getChildAt(p).getLeft();
                                        off_top = mDragLayer.getChildAt(p).getTop();
                                        tv_width = textView.getWidth() / 2;
                                    }
                                }
                            }
                        }
                    }

                    if (tv_width <= tolranceWidth) {
                        tv_width = tolranceWidth;
                    }

                    playerArrayList.get(i).setLeft_position(off_left + tv_width);
                    playerArrayList.get(i).setTop_position(off_top + heightDifference);
                    dbcon.UpdateTeam2(update_player.getPlayer_name(), (off_left + tv_width) + "", (off_top + heightDifference) + "", update_player.getPlayer_id() + "", 1);
//                    dbcon.UpdateTeam2(update_player.getPlayer_name(), (off_left + tolranceWidth) + "", (off_top + heightDifference) + "", update_player.getPlayer_id() + "", 1);

                } else {

                    playerArrayList.get(i).setLeft_position(mDragLayer.getWidth() / 2);
                    playerArrayList.get(i).setTop_position(mDragLayer.getHeight() / 2);
                    dbcon.UpdateTeam2(update_player.getPlayer_name(), mDragLayer.getWidth() / 2 + "", mDragLayer.getHeight() / 2 + "", update_player.getPlayer_id() + "", 0);

                }
            }
        }
        if (isFromGIF) {
            isFromGIF = false;
            goToGIFActivity();
        }

        if (isFromBackPressed) {
            isFromBackPressed = false;
            cc.savePrefBoolean("isPlayer1Ready", true);
            cc.savePrefBoolean("isPlayer2Ready", true);
            cc.savePrefBoolean("haveAnyPlayer", true);

            Intent launchNextActivity;
            launchNextActivity = new Intent(VersusSinglePlayerActivity.this, VersusActivity.class);
            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(launchNextActivity);
            finish();
        }
    }


    private void goToGIFActivity() {
        updateFormation();
        CommonClass.playerAnimation = playerArrayList;

        Cursor cursor = null;
        cursor = dbcon.getTeamAnim();

        try {
            if (cursor.getCount() != 0 && cursor != null) {
                dbcon.deleteTeamAnim();
                dbcon.creataTeamAnim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < playerArrayList.size(); i++) {
            player player_team1 = playerArrayList.get(i);
            int onField = 0;
            if (player_team1.isOnField())
                onField = 1;

            dbcon.insertTeamAnim(player_team1.getPlayer_name(), player_team1.getLeft_position() + "", player_team1.getTop_position() + "", onField);
        }

        CommonClass.teamAnimName = tv_team_name.getText().toString().trim();
        cc.savePrefInt("playerAnim_T_Shirt", cc.loadPrefInt("t_shirt_position"));

        startActivity(new Intent(VersusSinglePlayerActivity.this, GIFActivity.class));
    }

    private void setUpPlayerFirstTime() {
        heightDifference = getHeightDifference(true);
        widthDifference = getHeightDifference(false);

        playerArrayList = new ArrayList<player>();


        Cursor cursor = null;
        int colourId = t_shirt_color;

        if (team1) {
            cursor = dbcon.getTeam1();
        } else {
            cursor = dbcon.getTeam2();
        }


        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            int playerCountOnField = 0;
            for (int i = 0; i < cursor.getCount(); i++) {
                player player_set = new player();
                player_set.setPlayer_id(Integer.parseInt(cursor.getString(0)));
                player_set.setPlayer_name(cursor.getString(1));
                player_set.setLeft_position(Integer.parseInt(cursor.getString(2)));
                player_set.setTop_position(Integer.parseInt(cursor.getString(3)));
                boolean onField = false;
                if (cursor.getInt(cursor.getColumnIndex("onField")) == 1) {
                    onField = true;
                    playerCountOnField++;
                }
                player_set.setOnField(onField);
                playerArrayList.add(player_set);

                if (i != cursor.getCount() - 1)
                    cursor.moveToNext();
            }
            total_players_in_formation = playerCountOnField;
            callFinalSetUp(colourId);
        } else {
            cc.showToast("No Data Available");
        }

    }

    private void callFinalSetUp(int colorId) {
        cc.savePrefInt("t_shirt_position", colorId);
        int k = 0;
        for (int i = 0; i < playerArrayList.size(); i++) {

            player team_player = playerArrayList.get(i);


            final int left = team_player.getLeft_position();
            final int top = team_player.getTop_position();

            total_players++;

            if (team_player.isOnField()) {
                final TextView textView = new TextView(VersusSinglePlayerActivity.this);
                textView.setText(team_player.getPlayer_name());
                textView.setTextColor(getResources().getColor(R.color.colorWhite));

                textView.setGravity(Gravity.CENTER_HORIZONTAL);

                ImageView image = new ImageView(VersusSinglePlayerActivity.this);
                image.setImageResource(ImageAdapter.mThumbIds[colorId]);
                final LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(image);
                linearLayout.addView(textView);

/*                textView.post(new Runnable() {
                    @Override
                    public void run() {

                        DragLayer.LayoutParams params;
                        textView.measure(0, 0);
                        int tv_width = textView.getMeasuredWidth() / 2;

                        if (tv_width <= tolranceWidth) {
                            tv_width = tolranceWidth;
                        }
                        params = new DragLayer.LayoutParams(DragLayer.LayoutParams.WRAP_CONTENT, DragLayer.LayoutParams.WRAP_CONTENT, (left - tv_width), (top - heightDifference));
                        mDragLayer.addView(linearLayout, params);
                        linearLayout.setOnClickListener(VersusSinglePlayerActivity.this);
                        linearLayout.setOnTouchListener(VersusSinglePlayerActivity.this);
                    }
                });*/


                DragLayer.LayoutParams params;
                textView.measure(0, 0);
                int tv_width = textView.getMeasuredWidth() / 2;

                if (tv_width <= tolranceWidth) {
                    tv_width = tolranceWidth;
                }
                params = new DragLayer.LayoutParams(DragLayer.LayoutParams.WRAP_CONTENT, DragLayer.LayoutParams.WRAP_CONTENT, (left - tv_width), (top - heightDifference));
                mDragLayer.addView(linearLayout, params);
                linearLayout.setOnClickListener(VersusSinglePlayerActivity.this);
                linearLayout.setOnTouchListener(VersusSinglePlayerActivity.this);

            }
        }
    }

    private void setUpViews() {
        DragController dragController = mDragController;
        mDragLayer = (DragLayer) findViewById(R.id.drag_layer);
        mDragLayer.setDragController(dragController);
        dragController.addDropTarget(mDragLayer);
    }

    private int getHeightDifference(boolean b) {

        scr_height = 0;
        int difference = 0;
        scr_width = 0;
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

    private void setListeners() {
        iv_add_player.setOnClickListener(this);
        iv_change_no_of_player.setOnClickListener(this);
        iv_change_colour.setOnClickListener(this);
        iv_save.setOnClickListener(this);
        iv_animation.setOnClickListener(this);
        iv_versus.setOnClickListener(this);
        iv_share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_add_player:
                addPlayer();
                break;

            case R.id.iv_change_no_of_player:
                chooseTotalPlayer();
                break;

            case R.id.iv_change_colour:
                changeTShirtDialog();
                break;

            case R.id.iv_save:


                break;

            case R.id.iv_versus:
//                prepareForVersusMode();
                break;

            case R.id.iv_animation:
                isFromGIF = true;
                updateFormation();
                break;

            case R.id.iv_share:
                shareFormation();
                break;

        }
    }


    private void shareFormation() {

        fl_capture_layout.destroyDrawingCache();
        fl_capture_layout.setDrawingCacheEnabled(true);
        Bitmap bitmap = fl_capture_layout.getDrawingCache();

        String image_name = tv_team_name.getText().toString().trim() + "_" + cc.getCurrentDateandtime() + ".jpg";
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

    private void changeTShirtDialog() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(VersusSinglePlayerActivity.this);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        builder.setCancelable(false);

        View dialogView = inflater.inflate(R.layout.dialog_t_shirts, null);
        builder.setView(dialogView);

        final android.support.v7.app.AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(true);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ImageView iv_cancel = (ImageView) dialogView.findViewById(R.id.iv_cancel);
        GridView gridView = (GridView) dialogView.findViewById(R.id.gv_main);

        final ImageAdapter imageAdapter = new ImageAdapter(VersusSinglePlayerActivity.this);
        gridView.setAdapter(imageAdapter);

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                cc.savePrefInt("t_shirt_position", position);

                if (team1) {
                    cc.savePrefInt("player1_T_Shirt", position);
                } else {
                    cc.savePrefInt("player2_T_Shirt", position);
                }

                String formation_name = tv_team_name.getText().toString().trim();
                String formation_id = dbcon.getFormationIdFromFormationName(formation_name);

                dbcon.updateFormationColour(formation_id, cc.loadPrefInt("t_shirt_position") + "");

                int count = mDragLayer.getChildCount();


                View v = null;

                for (int i = 0; i < count; i++) {
                    v = mDragLayer.getChildAt(i);
                    if (v instanceof LinearLayout) {

                        LinearLayout ll = (LinearLayout) v;
                        View v1 = null;
                        int count_ll = ll.getChildCount();

                        for (int j = 0; j < count_ll; j++) {

                            v1 = ll.getChildAt(j);

                            if (v1 instanceof ImageView) {

                                ImageView imageView = (ImageView) v1;
                                imageView.setImageResource(ImageAdapter.mThumbIds[cc.loadPrefInt("t_shirt_position")]);
                                imageView.invalidate();
                            }
                        }
                    }
                }
                alert.dismiss();
            }
        });
        alert.show();
    }

    private int getRelativeLeft(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getLeft();
        else
            return myView.getLeft() + getRelativeLeft((View) myView.getParent());
    }

    private int getRelativeTop(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent());
    }

    private void addPlayer() {
        player add_player = new player();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = mDragLayer.getWidth() / 2;
        params.topMargin = mDragLayer.getHeight() / 2;

        int number = 0;
        int number_o = 0;
        for (int i = 0; i < playerArrayList.size(); i++) {
            String name = playerArrayList.get(i).getPlayer_name();

            boolean isOkay = false;
            if (name.length() > 6 && name.length() <= 8) {
                Character character7 = name.charAt(6);

                if (name.length() == 7) {
                    if (Character.isDigit(character7)) {
                        isOkay = true;
                    }
                } else if (name.length() == 8) {
                    Character character8 = name.charAt(7);
                    if (Character.isDigit(character7) && Character.isDigit(character8)) {
                        isOkay = true;
                    }
                } else {
                    isOkay = false;
                }

                if (isOkay) {
                    if (name.substring(0, 6).equals(getResources().getString(R.string.player_tag))) {
                        number = Integer.parseInt(name.replaceAll(getResources().getString(R.string.player_tag), ""));
                        if (number > number_o) {
                            number_o = number;
                        }
                    }
                }
            }
        }


        String add_player_name = getResources().getString(R.string.player_tag) + (number_o + 1);


        int player_id;

        if (team1) {
            dbcon.insertTeam1(add_player_name, mDragLayer.getWidth() / 2 + "", mDragLayer.getHeight() / 2 + "", 0);
            player_id= Integer.parseInt(dbcon.getPlayerIdFromTeam1(add_player_name));
        } else {
            dbcon.insertTeam2(add_player_name, mDragLayer.getWidth() / 2 + "", mDragLayer.getHeight() / 2 + "", 0);
            player_id= Integer.parseInt(dbcon.getPlayerIdFromTeam2(add_player_name));
        }



        cc.savePrefBoolean("isAnyPlayer", true);

        add_player.setPlayer_id(player_id);
        add_player.setLeft_position(mDragLayer.getWidth() / 2);
        add_player.setTop_position(mDragLayer.getHeight() / 2);
        add_player.setPlayer_name(add_player_name);
        add_player.setOnField(false);
        playerArrayList.add(add_player);

        setUpExpandable();
        listAdapter.notifyDataSetChanged();
        updateFormation();
        total_players++;
    }


    public boolean onTouch(View v, MotionEvent ev) {

        boolean handledHere = false;

        final int action = ev.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            x_touch = (int) ev.getX();
            y_touch = (int) ev.getY();

            handledHere = startDrag(v);
            if (handledHere) v.performClick();
        }
        if (action == MotionEvent.ACTION_UP) {
            x_touch = (int) ev.getX();
            y_touch = (int) ev.getY();

        }
        return handledHere;
    }

    private void renamePlayer(final TextView sliderTextView, final TextView textView, final int position, final int child_count) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(VersusSinglePlayerActivity.this);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        builder.setCancelable(false);

        View dialogView = inflater.inflate(R.layout.dialog_rename_player, null);
        builder.setView(dialogView);

        final android.support.v7.app.AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(true);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final EditText et_rename_player = (EditText) dialogView.findViewById(R.id.et_player_name);
        et_rename_player.setText(sliderTextView.getText().toString().trim());

        ImageView btn_cancel = (ImageView) dialogView.findViewById(R.id.iv_cancel);
        ImageView btn_yes = (ImageView) dialogView.findViewById(R.id.iv_ok);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();


            }
        });

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int Xt = 0;
                int X_ = 0;

                player team_player = playerArrayList.get(position);
                final int left = team_player.getLeft_position();
                final int top = team_player.getTop_position();

                String rename_name = et_rename_player.getText().toString().trim();
                boolean isNameMatched = false;
                if (rename_name.equals("")) {
                    et_rename_player.setError(getResources().getString(R.string.enter_player_name));
                } else {
                    for (int i = 0; i < playerArrayList.size(); i++) {
                        if (rename_name.equals(playerArrayList.get(i).getPlayer_name())) {
                            isNameMatched = true;
                        }
                    }

                    if (!isNameMatched) {
                        playerArrayList.get(position).setPlayer_name(et_rename_player.getText().toString().trim());


                        LinearLayout ll_md_v = null;

                        View md_v = mDragLayer.getChildAt(child_count);
                        if (md_v instanceof LinearLayout) {
                            ll_md_v = (LinearLayout) md_v;
                            X_ = ll_md_v.getWidth() / 2;
                            Xt = ll_md_v.getLeft();
                        }


                        textView.setText(et_rename_player.getText().toString().trim());

                        final int finalXt = Xt;
                        final int finalX_ = X_;
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                int x__ = 0;
                                int x_left = 0;
                                View v = mDragLayer.getChildAt(child_count);
                                textView.measure(0, 0);
                                x__ = textView.getMeasuredWidth() / 2;

                                if (x__ >= tolranceWidth) {
                                    x_left = finalXt - (x__ - finalX_);
                                } else {
                                    x_left = finalXt - tolranceWidth;
                                }
                                mDragLayer.updateViewLayout(mDragLayer.getChildAt(child_count),
                                        new DragLayer.LayoutParams(DragLayer.LayoutParams.WRAP_CONTENT, DragLayer.LayoutParams.WRAP_CONTENT, (x_left), (v.getTop())));
                                mDragLayer.invalidate();
                            }
                        });

                        setUpExpandable();
                        listAdapter.notifyDataSetChanged();
                        alert.dismiss();
                    } else {
                        et_rename_player.setError(getResources().getString(R.string.exist_player_name));
                    }
                }

            }
        });
        alert.show();
    }


    public boolean startDrag(View v) {

        Object dragInfo = v;
        mDragController.startDrag(v, mDragLayer, dragInfo, DragController.DRAG_ACTION_MOVE);
        return true;

    }

    private void chooseTotalPlayer() {

        final int oldTotalPlayers = total_players_in_formation;

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(VersusSinglePlayerActivity.this);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        builder.setCancelable(false);

        View dialogView = inflater.inflate(R.layout.dialog_total_players, null);
        builder.setView(dialogView);

        final android.support.v7.app.AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(true);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        View.OnClickListener dialogClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_player_5:
                        total_players_in_formation = 5;
                        break;
                    case R.id.tv_player_6:
                        total_players_in_formation = 6;
                        break;
                    case R.id.tv_player_7:
                        total_players_in_formation = 7;
                        break;
                    case R.id.tv_player_8:
                        total_players_in_formation = 8;
                        break;
                    case R.id.tv_player_9:
                        total_players_in_formation = 9;
                        break;
                    case R.id.tv_player_10:
                        total_players_in_formation = 10;
                        break;
                    case R.id.tv_player_11:
                        total_players_in_formation = 11;
                        break;
                }

                if (total_players_in_formation < oldTotalPlayers) {

                    int differ = oldTotalPlayers - total_players_in_formation;


                    int j = oldTotalPlayers - 1;
                    for (int i = total_players_in_formation; i < oldTotalPlayers; i++) {

                        removeFlagOfRemovedView(j);
                        j--;
                    }


                } else if (total_players_in_formation > oldTotalPlayers) {


                    if (total_players_in_formation > playerArrayList.size()) {

                        int playerTobeOn = total_players_in_formation - mDragLayer.getChildCount();
                        if (playerTobeOn > 0) {

                            for (int i = 0; i < playerTobeOn; i++) {

                                for (int j = 0; j < playerArrayList.size(); j++) {
                                    player on_player = playerArrayList.get(j);
                                    if (!on_player.isOnField()) {
                                        playerArrayList.get(j).setOnField(true);
                                        break;
                                    }
                                }
                            }
                        }


                        for (int i = playerArrayList.size(); i < total_players_in_formation; i++) {

                            player add_player = new player();
                            add_player.setPlayer_id(i);

                            int left = mDragLayer.getWidth();
                            int top = mDragLayer.getHeight();
                            add_player.setLeft_position(left);
                            add_player.setTop_position(top);
                            add_player.setOnField(true);

                            int number = 0;
                            int number_o = 0;
                            for (int l = 0; l < playerArrayList.size(); l++) {
                                String name = playerArrayList.get(l).getPlayer_name();

                                boolean isOkay = false;
                                if (name.length() >= 6 && name.length() <= 8) {
                                    Character character7 = name.charAt(6);

                                    if (name.length() == 7) {
                                        if (Character.isDigit(character7)) {
                                            isOkay = true;
                                        }
                                    } else if (name.length() == 8) {
                                        Character character8 = name.charAt(7);
                                        if (Character.isDigit(character7) && Character.isDigit(character8)) {
                                            isOkay = true;
                                        }
                                    } else {
                                        isOkay = false;
                                    }

                                    if (isOkay) {
                                        if (name.substring(0, 6).equals(getResources().getString(R.string.player_tag))) {
                                            number = Integer.parseInt(name.replaceAll(getResources().getString(R.string.player_tag), ""));
                                            if (number > number_o) {
                                                number_o = number;
                                            }
                                        }
                                    }

                                }
                            }

                            add_player.setPlayer_name(getResources().getString(R.string.player_tag) + (number_o + 1));

                            int player_id=0;

                            if (team1) {
                                dbcon.insertTeam1(add_player.getPlayer_name(), mDragLayer.getWidth() / 2 + "", mDragLayer.getHeight() / 2 + "", 0);
                                player_id= Integer.parseInt(dbcon.getPlayerIdFromTeam1(add_player.getPlayer_name()));
                            } else {
                                dbcon.insertTeam2(add_player.getPlayer_name(), mDragLayer.getWidth() / 2 + "", mDragLayer.getHeight() / 2 + "", 0);
                                player_id= Integer.parseInt(dbcon.getPlayerIdFromTeam2(add_player.getPlayer_name()));
                            }



                            cc.savePrefBoolean("isAnyPlayer", true);
                            add_player.setPlayer_id(player_id);
                            playerArrayList.add(add_player);

                            setUpExpandable();
                            listAdapter.notifyDataSetChanged();
                            updateFormation();
                            total_players++;


                        }

                    } else {

                        int playerTobeOn = total_players_in_formation - mDragLayer.getChildCount();

                        for (int i = 0; i < playerTobeOn; i++) {

                            for (int j = 0; j < playerArrayList.size(); j++) {
                                player on_player = playerArrayList.get(j);

                                if (!on_player.isOnField()) {
                                    playerArrayList.get(j).setOnField(true);
                                    break;
                                }

                            }

                        }

                    }


                }
                mDragLayer.removeAllViews();
                setUpExpandable();
                listAdapter.notifyDataSetChanged();
                callFormationDialog(total_players_in_formation);


                alert.dismiss();
            }


        };
        TextView tv_player_5, tv_player_6, tv_player_7, tv_player_8, tv_player_9, tv_player_10, tv_player_11;
        tv_player_5 = (TextView) dialogView.findViewById(R.id.tv_player_5);
        tv_player_6 = (TextView) dialogView.findViewById(R.id.tv_player_6);
        tv_player_7 = (TextView) dialogView.findViewById(R.id.tv_player_7);
        tv_player_8 = (TextView) dialogView.findViewById(R.id.tv_player_8);
        tv_player_9 = (TextView) dialogView.findViewById(R.id.tv_player_9);
        tv_player_10 = (TextView) dialogView.findViewById(R.id.tv_player_10);
        tv_player_11 = (TextView) dialogView.findViewById(R.id.tv_player_11);

        tv_player_5.setOnClickListener(dialogClickListener);
        tv_player_6.setOnClickListener(dialogClickListener);
        tv_player_7.setOnClickListener(dialogClickListener);
        tv_player_8.setOnClickListener(dialogClickListener);
        tv_player_9.setOnClickListener(dialogClickListener);
        tv_player_10.setOnClickListener(dialogClickListener);
        tv_player_11.setOnClickListener(dialogClickListener);

        ImageView btn_cancel = (ImageView) dialogView.findViewById(R.id.iv_cancel);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();

            }
        });


        alert.show();
    }


    public void callFormationDialog(final int total_players_in_formation) {
//                activity_choose_formation
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(VersusSinglePlayerActivity.this);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        builder.setCancelable(false);

        View dialogView = inflater.inflate(R.layout.dialog_formation, null);
        builder.setView(dialogView);

        final android.support.v7.app.AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(true);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final TextView tv_formation_1, tv_formation_2, tv_formation_3, tv_formation_4;

        tv_formation_1 = (TextView) dialogView.findViewById(R.id.tv_formation_1);
        tv_formation_2 = (TextView) dialogView.findViewById(R.id.tv_formation_2);
        tv_formation_3 = (TextView) dialogView.findViewById(R.id.tv_formation_3);
        tv_formation_4 = (TextView) dialogView.findViewById(R.id.tv_formation_4);
        ImageView btn_cancel = (ImageView) dialogView.findViewById(R.id.iv_cancel);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();

            }
        });

        if (total_players_in_formation == 5) {
            tv_formation_1.setText("2-2");
            tv_formation_2.setText("1-2-1");
            tv_formation_3.setText("2-1-1");
            tv_formation_4.setText("3-1");
        } else if (total_players_in_formation == 6) {
            tv_formation_1.setText("2-2-1");
            tv_formation_2.setText("3-1-1");
            tv_formation_3.setText("3-2");
            tv_formation_4.setText("2-1-2");

        } else if (total_players_in_formation == 7) {
            tv_formation_1.setText("4-1-1");
            tv_formation_2.setText("2-2-2");
            tv_formation_3.setText("3-2-1");
            tv_formation_4.setText("3-1-2");
        } else if (total_players_in_formation == 8) {
            tv_formation_1.setText("3-2-1-1");
            tv_formation_2.setText("3-3-1");
            tv_formation_3.setText("3-2-2");
            tv_formation_4.setText("3-1-2-1");

        } else if (total_players_in_formation == 9) {
            tv_formation_1.setText("4-3-1");
            tv_formation_2.setText("3-3-2");
            tv_formation_3.setText("3-4-1");
            tv_formation_4.setText("3-2-1-2");

        } else if (total_players_in_formation == 10) {
            tv_formation_1.setText("4-4-1");
            tv_formation_2.setText("3-4-2");
            tv_formation_3.setText("3-3-1-2");
            tv_formation_4.setText("3-2-2-2");
        } else if (total_players_in_formation == 11) {
            tv_formation_1.setText("3-4-3");
            tv_formation_2.setText("4-2-4");
            tv_formation_3.setText("4-4-2");
            tv_formation_4.setText("4-3-3");
        }


        View.OnClickListener dialogClickListener = new View.OnClickListener() {
            String form = "";

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_formation_1:
                        form = tv_formation_1.getText().toString().trim();
                        callFormationSetupDialog(total_players_in_formation, form);
                        alert.dismiss();
                        break;
                    case R.id.tv_formation_2:
                        form = tv_formation_2.getText().toString().trim();
                        callFormationSetupDialog(total_players_in_formation, form);
                        alert.dismiss();
                        break;
                    case R.id.tv_formation_3:
                        form = tv_formation_3.getText().toString().trim();
                        callFormationSetupDialog(total_players_in_formation, form);
                        alert.dismiss();
                        break;
                    case R.id.tv_formation_4:
                        form = tv_formation_4.getText().toString().trim();
                        callFormationSetupDialog(total_players_in_formation, form);
                        alert.dismiss();
                        break;

                }

            }

        };

        tv_formation_1.setOnClickListener(dialogClickListener);
        tv_formation_2.setOnClickListener(dialogClickListener);
        tv_formation_3.setOnClickListener(dialogClickListener);
        tv_formation_4.setOnClickListener(dialogClickListener);
        alert.show();

    }

    private void callFormationSetupDialog(final int total_players_in_formation, String form) {
        heightDifference = getHeightDifference(true);
        widthDifference = getHeightDifference(false);

        ArrayList<player> tempPlayerArrayList = cc.getPlayers(form, lay_width, lay_height, total_players_in_formation + "");

        int count = 0;
        for (int i = 0; i < playerArrayList.size(); i++) {

            if (count < total_players_in_formation) {
                player team_player = playerArrayList.get(i);
                player temp_player = tempPlayerArrayList.get(count);

                if (team_player.isOnField()) {
                    final int left = temp_player.getLeft_position();
                    final int top = temp_player.getTop_position();
                    team_player.setOnField(true);
                    final TextView textView = new TextView(VersusSinglePlayerActivity.this);
                    textView.setText(team_player.getPlayer_name());
                    textView.setTextColor(getResources().getColor(R.color.colorWhite));


                    textView.setGravity(Gravity.CENTER_HORIZONTAL);

                    ImageView image = new ImageView(VersusSinglePlayerActivity.this);
                    image.setImageResource(ImageAdapter.mThumbIds[cc.loadPrefInt("t_shirt_position")]);
                    final LinearLayout linearLayout = new LinearLayout(VersusSinglePlayerActivity.this);
                    linearLayout.setGravity(Gravity.CENTER);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    linearLayout.addView(image);
                    linearLayout.addView(textView);
                    count++;
                    final int finalCount = count;
                    /*textView.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.measure(0, 0);
                            int tv_width = textView.getMeasuredWidth() / 2;

                            if (tv_width <= tolranceWidth) {
                                tv_width = tolranceWidth;
                            }

                            DragLayer.LayoutParams params;//(left - tv_width), (top - heightDifference)
                            params = new DragLayer.LayoutParams(DragLayer.LayoutParams.WRAP_CONTENT, DragLayer.LayoutParams.WRAP_CONTENT, (left - tv_width), (top - heightDifference));

                            mDragLayer.addView(linearLayout, params);
                            linearLayout.setOnClickListener(VersusSinglePlayerActivity.this);
                            linearLayout.setOnTouchListener(VersusSinglePlayerActivity.this);

                        }
                    });*/


                    textView.measure(0, 0);
                    int tv_width = textView.getMeasuredWidth() / 2;

                    if (tv_width <= tolranceWidth) {
                        tv_width = tolranceWidth;
                    }

                    DragLayer.LayoutParams params;//(left - tv_width), (top - heightDifference)
                    params = new DragLayer.LayoutParams(DragLayer.LayoutParams.WRAP_CONTENT, DragLayer.LayoutParams.WRAP_CONTENT, (left - tv_width), (top - heightDifference));

                    mDragLayer.addView(linearLayout, params);
                    linearLayout.setOnClickListener(VersusSinglePlayerActivity.this);
                    linearLayout.setOnTouchListener(VersusSinglePlayerActivity.this);
                }
            }
        }
        mDragLayer.invalidate();
    }

    private void removeFlagOfRemovedView(int i) {
        String p_name = "";
        View v = null;
        v = mDragLayer.getChildAt(i);
        if (v instanceof LinearLayout) {

            LinearLayout ll = (LinearLayout) v;
            View v1 = null;
            int count_ll = ll.getChildCount();
            for (int k = 0; k < count_ll; k++) {

                v1 = ll.getChildAt(k);

                if (v1 instanceof TextView) {
                    TextView textView = (TextView) v1;
                    p_name = ((TextView) v1).getText().toString().trim();
                }
            }
        }

        for (int j = 0; j < playerArrayList.size(); j++) {

            player editFlagPlayer = playerArrayList.get(j);

            if (editFlagPlayer.getPlayer_name().equals(p_name)) {
                editFlagPlayer.setOnField(false);
            }

        }

    }

    @Override
    protected void onPause() {


        SaveDataInBackend saveDataInBackend = new SaveDataInBackend();
        saveDataInBackend.execute();

        super.onPause();
    }

    public class SaveDataInBackend extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                updateFormation();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


}
