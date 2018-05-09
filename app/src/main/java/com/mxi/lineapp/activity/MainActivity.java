package com.mxi.lineapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mxi.lineapp.R;
import com.mxi.lineapp.network.CommonClass;

public class MainActivity extends AppCompatActivity {


    CommonClass cc;
    TextView tv_toolbar;
    TextView tv_create_formation, tv_open_formation, tv_quit;

    //    TextView tv_versus_mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cc = new CommonClass(this);



        tv_toolbar = (TextView) findViewById(R.id.tv_toolbar_title);


        tv_create_formation = (TextView) findViewById(R.id.tv_create_formation);
        tv_open_formation = (TextView) findViewById(R.id.tv_open_formation);
        tv_quit = (TextView) findViewById(R.id.tv_quit);

        tv_create_formation.setOnClickListener(m_OnClickListener);
        tv_open_formation.setOnClickListener(m_OnClickListener);
        tv_quit.setOnClickListener(m_OnClickListener);

        AdView av_main= (AdView) findViewById(R.id.av_main);
        AdRequest adRequest = new AdRequest.Builder().build();
        av_main.loadAd(adRequest);


        tv_toolbar.setText(getResources().getString(R.string.main_activity_title));
    }

    public View.OnClickListener m_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.tv_create_formation:
                    cc.savePrefBoolean("haveAnyPlayer",false);
                    cc.savePrefBoolean("fromVersusMode",false);
                    cc.savePrefBoolean("fromOpenSavedFormation",false);
                    startActivity(new Intent(MainActivity.this, ChooseNumberOfPlayersActivity.class));
                    break;
                case R.id.tv_open_formation:
                    cc.savePrefBoolean("haveAnyPlayer",false);
                    cc.savePrefBoolean("fromVersusMode",false);
                    cc.savePrefBoolean("fromOpenSavedFormation",true);
                    startActivity(new Intent(MainActivity.this, OpenSavedFormationActivity.class));
                    break;

                case R.id.tv_quit:
                    finish();
                    break;
            }
        }
    };
}
