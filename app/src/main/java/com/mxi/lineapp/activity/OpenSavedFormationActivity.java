package com.mxi.lineapp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mxi.lineapp.R;
import com.mxi.lineapp.adapter.FormationAdapter;
import com.mxi.lineapp.database.SQLitehelper;
import com.mxi.lineapp.network.CommonClass;

import java.util.ArrayList;

public class OpenSavedFormationActivity extends AppCompatActivity {
    public static String openSavedFormationName ="";
    public static String formation_id="";
    public static String OSFA_total_players="";
    public static boolean fromOpenSavedActivity =false;

    TextView tv_toolbar;
    ImageView iv_back;
    ArrayAdapter adapter;
    public static ArrayList<String> strings;
    RecyclerView lv_saved_formation;
    SQLitehelper dbcon;
    Cursor cursor=null;
    CommonClass cc;
    LinearLayoutManager llm;
    FormationAdapter formationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_saved_formation);
        cc=new CommonClass(this);
        dbcon=new SQLitehelper(this);
        llm=new LinearLayoutManager(this);


        tv_toolbar = (TextView) findViewById(R.id.tv_toolbar_title);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        lv_saved_formation=(RecyclerView) findViewById(R.id.lv_saved_formation);

        tv_toolbar.setText(getResources().getString(R.string.open_a_saved_formation));

        cursor=dbcon.getFormations();
        if(cursor!=null && cursor.getCount()!=0){
            cursor.moveToFirst();
            strings=new ArrayList<>();
            for (int i = 0; i < cursor.getCount(); i++) {
                strings.add(cursor.getString(1));
                Log.e("SavedList",cursor.getString(1)+"");
                if(i != cursor.getCount()-1)
                    cursor.moveToNext();
            }
            setUpList();
        }else{
            cc.showToast("No Data Available");
        }


        AdView av_main= (AdView) findViewById(R.id.av_open_saved);
        AdRequest adRequest = new AdRequest.Builder().build();
        av_main.loadAd(adRequest);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void setUpList() {

        formationAdapter=new FormationAdapter(this,strings);
        lv_saved_formation.setLayoutManager(llm);
        lv_saved_formation.setAdapter(formationAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(!cc.loadPrefBoolean("isPlayer1Ready")){
            Intent launchNextActivity;
            launchNextActivity = new Intent(OpenSavedFormationActivity.this, MainActivity.class);
            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(launchNextActivity);
            finish();
        }else{
            finish();
        }

    }
}
