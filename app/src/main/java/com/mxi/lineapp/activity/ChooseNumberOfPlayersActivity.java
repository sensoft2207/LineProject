package com.mxi.lineapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mxi.lineapp.R;

public class ChooseNumberOfPlayersActivity extends AppCompatActivity implements View.OnClickListener {
    public static String choosen_number = "";
    TextView tv_toolbar;
    TextView tv_player_5, tv_player_6, tv_player_7, tv_player_8, tv_player_9, tv_player_10, tv_player_11;
    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_number_of_player);

        tv_toolbar = (TextView) findViewById(R.id.tv_toolbar_title);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_player_5 = (TextView) findViewById(R.id.tv_player_5);
        tv_player_6 = (TextView) findViewById(R.id.tv_player_6);
        tv_player_7 = (TextView) findViewById(R.id.tv_player_7);
        tv_player_8 = (TextView) findViewById(R.id.tv_player_8);
        tv_player_9 = (TextView) findViewById(R.id.tv_player_9);
        tv_player_10 = (TextView) findViewById(R.id.tv_player_10);
        tv_player_11 = (TextView) findViewById(R.id.tv_player_11);
        iv_back.setVisibility(View.VISIBLE);

        tv_toolbar.setText(getResources().getString(R.string.create_formation_title));

        AdView av_main= (AdView) findViewById(R.id.av_number_player);
        AdRequest adRequest = new AdRequest.Builder().build();
        av_main.loadAd(adRequest);
        setClickListener();
    }

    private void setClickListener() {
        tv_player_5.setOnClickListener(this);
        tv_player_6.setOnClickListener(this);
        tv_player_7.setOnClickListener(this);
        tv_player_8.setOnClickListener(this);
        tv_player_9.setOnClickListener(this);
        tv_player_10.setOnClickListener(this);
        tv_player_11.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_player_5:
                choosen_number = "5";
                goToNextActivity();
                break;
            case R.id.tv_player_6:
                choosen_number = "6";
                goToNextActivity();
                break;
            case R.id.tv_player_7:
                choosen_number = "7";
                goToNextActivity();
                break;
            case R.id.tv_player_8:
                choosen_number = "8";
                goToNextActivity();
                break;
            case R.id.tv_player_9:
                choosen_number = "9";
                goToNextActivity();
                break;
            case R.id.tv_player_10:
                choosen_number = "10";
                goToNextActivity();
                break;
            case R.id.tv_player_11:
                choosen_number = "11";
                goToNextActivity();
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    public void goToNextActivity() {
        startActivity(new Intent(ChooseNumberOfPlayersActivity.this, ChooseFormationActivity.class));
//        finish();
    }

}
