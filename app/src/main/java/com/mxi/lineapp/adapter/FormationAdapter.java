package com.mxi.lineapp.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxi.lineapp.R;
import com.mxi.lineapp.activity.OpenSavedFormationActivity;
import com.mxi.lineapp.activity.SinglePlayerActivity;
import com.mxi.lineapp.activity.VersusActivity;
import com.mxi.lineapp.database.SQLitehelper;
import com.mxi.lineapp.network.CommonClass;

import java.util.ArrayList;

/**
 * Created by Akshay on 15-Mar-17.
 */
public class FormationAdapter extends RecyclerView.Adapter<FormationAdapter.MyViewHolder> {

    CommonClass cc;
    SQLitehelper dbcon;
    ArrayList<String> data;
    private LayoutInflater inflater;
    private Context context;

    public FormationAdapter(Context context, ArrayList<String> data) {
        cc = new CommonClass(context);
        dbcon = new SQLitehelper(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.raw_item_formation_name, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tv_nav_item_title.setText(data.get(position));

        holder.tv_nav_item_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OpenSavedFormationActivity.openSavedFormationName = data.get(position);
                OpenSavedFormationActivity.formation_id = dbcon.getFormationIdFromFormationName(data.get(position));
                OpenSavedFormationActivity.OSFA_total_players = dbcon.getTotalPlayersFromFormationId(OpenSavedFormationActivity.formation_id);
                OpenSavedFormationActivity.fromOpenSavedActivity = true;
                if (cc.loadPrefBoolean("haveAnyPlayer")) {
                    cc.savePrefBoolean("isPlayer2Ready", true);
                    int colourId= Integer.parseInt(dbcon.getColorIdFromFormationId(OpenSavedFormationActivity.formation_id));
                    cc.savePrefInt("player2_T_Shirt", colourId);
                    CommonClass.team2Name = data.get(position).trim();


                    Cursor cur = null;
                    cur = dbcon.getTeam2();
                    if (cur.getCount() != 0 && cur != null) {
                        dbcon.deleteTeam2();
                        dbcon.creataTeam2();
                    }

                    Cursor cursor = null;
                    cursor = dbcon.getPlayersFromFormationId(OpenSavedFormationActivity.formation_id);
                    Log.e("cursor", cursor.getCount() + "");
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


                    context.startActivity(new Intent(context, VersusActivity.class));
                    ((Activity) context).finish();

                } else {

                    cc.savePrefBoolean("isPlayer2Ready", false);
                    context.startActivity(new Intent(context, SinglePlayerActivity.class));
                    ((Activity) context).finish();
                }

            }
        });

        holder.tv_nav_item_title.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showFormationMenu(holder.tv_nav_item_title, holder, getRelativeLeft(holder.tv_nav_item_title), getRelativeTop(holder.tv_nav_item_title),position);
                return true;
            }
        });

        holder.iv_delete_formation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                builder.setCancelable(false);

                View dialogView = inflater.inflate(R.layout.dialog_confirmation, null);
                builder.setView(dialogView);

                final android.support.v7.app.AlertDialog alert = builder.create();
                alert.setCanceledOnTouchOutside(true);
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                final TextView tv_confirm_text = (TextView) dialogView.findViewById(R.id.tv_confirm_text);
                tv_confirm_text.setText(context.getResources().getString(R.string.q_formation_player));
                Button btn_yes = (Button) dialogView.findViewById(R.id.btn_yes);
                Button btn_no = (Button) dialogView.findViewById(R.id.btn_no);
                ImageView iv_cancel = (ImageView) dialogView.findViewById(R.id.iv_cancel);

                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.cv_formation.setVisibility(View.GONE);
                        dbcon.deleteFormation(data.get(holder.getAdapterPosition()));
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

    private void showFormationMenu(final TextView sliderTextView, final MyViewHolder holder, int x, int y, final int position) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_menu_formation);
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
//        TextView tv_delete_formation = (TextView) dialog.findViewById(R.id.tv_delete_formation);

        tv_copy_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Temp", sliderTextView.getText().toString().trim());
                clipboard.setPrimaryClip(clip);
                cc.showToast("Player Name is copied");
                dialog.dismiss();
            }
        });

        tv_rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int formationId = Integer.parseInt(dbcon.getFormationIdFromFormationName(sliderTextView.getText().toString()));
                renameFormation(sliderTextView, formationId,position);
                dialog.dismiss();
            }
        });

       /* tv_delete_formation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cv_formation.setVisibility(View.GONE);
                dbcon.deleteFormation(data.get(holder.getAdapterPosition()));
            }
        });*/
    }

    private void renameFormation(final TextView sliderTextView, final int formationId, final int position) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        builder.setCancelable(false);

        View dialogView = inflater.inflate(R.layout.dialog_rename_player, null);
        builder.setView(dialogView);

        final android.support.v7.app.AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(true);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText et_rename_player = (EditText) dialogView.findViewById(R.id.et_player_name);
        et_rename_player.setText(sliderTextView.getText().toString().trim());
        et_rename_player.setHint(context.getResources().getString(R.string.hint_choose_the_name_of_your_team));
        TextView tv_rename_title = (TextView) dialogView.findViewById(R.id.tv_rename_title);
        tv_rename_title.setText(context.getResources().getString(R.string.rename_formation));

        ImageView btn_cancel = (ImageView) dialogView.findViewById(R.id.iv_cancel);
        ImageView btn_yes = (ImageView) dialogView.findViewById(R.id.iv_ok);

        et_rename_player.setText(sliderTextView.getText().toString().trim());

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();


            }
        });

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_rename_player.getText().toString().trim().equals("")) {
                    et_rename_player.setError(context.getResources().getString(R.string.enter_player_name));
                } else {
                    Cursor cursor = null;
                    cursor = dbcon.isFormationAvailable(et_rename_player.getText().toString().trim());
                    cursor.moveToFirst();
                    if (cursor.getCount() == 0 || cursor.equals(null)) {
                        Log.e("isFormationAvailable", "true");
                        dbcon.updateFormationName(String.valueOf(formationId), et_rename_player.getText().toString().trim());

                        data.set(position,et_rename_player.getText().toString());
                        notifyDataSetChanged();
//                        sliderTextView.setText(et_rename_player.getText().toString().trim());
                        alert.dismiss();
                    } else {
                        cc.showToast(context.getResources().getString(R.string.formation_name_exist));
                    }
                }
            }
        });
        alert.show();
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nav_item_title;
        CardView cv_formation;
        ImageView iv_delete_formation;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_delete_formation = (ImageView) itemView.findViewById(R.id.iv_delete_formtion);
            cv_formation = (CardView) itemView.findViewById(R.id.cv_formation);
            tv_nav_item_title = (TextView) itemView.findViewById(R.id.tv_raw_item_player_name);
        }
    }
}
