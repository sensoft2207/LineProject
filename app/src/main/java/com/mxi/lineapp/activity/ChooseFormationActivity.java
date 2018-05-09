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

public class ChooseFormationActivity extends AppCompatActivity implements View.OnClickListener {
    public static String formation="";
    ImageView iv_back;
    TextView tv_toolbar, tv_formation_1, tv_formation_2, tv_formation_3, tv_formation_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_formation);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_toolbar = (TextView) findViewById(R.id.tv_toolbar_title);
        tv_formation_1 = (TextView) findViewById(R.id.tv_formation_1);
        tv_formation_2 = (TextView) findViewById(R.id.tv_formation_2);
        tv_formation_3 = (TextView) findViewById(R.id.tv_formation_3);
        tv_formation_4 = (TextView) findViewById(R.id.tv_formation_4);
        iv_back.setVisibility(View.VISIBLE);


        AdView av_main= (AdView) findViewById(R.id.av_choose_formation);
        AdRequest adRequest = new AdRequest.Builder().build();
        av_main.loadAd(adRequest);
        setClickListener();
        setUpFormation();

        tv_toolbar.setText(getResources().getString(R.string.create_formation_title));
    }

    private void setUpFormation() {
        if (ChooseNumberOfPlayersActivity.choosen_number.equals("5")) {
            tv_formation_1.setText("2-2");
            tv_formation_2.setText("1-2-1");
            tv_formation_3.setText("2-1-1");
            tv_formation_4.setText("3-1");
        } else if (ChooseNumberOfPlayersActivity.choosen_number.equals("6")) {
            tv_formation_1.setText("2-2-1");
            tv_formation_2.setText("3-1-1");
            tv_formation_3.setText("3-2");
            tv_formation_4.setText("2-1-2");

        } else if (ChooseNumberOfPlayersActivity.choosen_number.equals("7")) {
            tv_formation_1.setText("4-1-1");
            tv_formation_2.setText("2-2-2");
            tv_formation_3.setText("3-2-1");
            tv_formation_4.setText("3-1-2");
        } else if (ChooseNumberOfPlayersActivity.choosen_number.equals("8")) {
            tv_formation_1.setText("3-2-1-1");
            tv_formation_2.setText("3-3-1");
            tv_formation_3.setText("3-2-2");
            tv_formation_4.setText("3-1-2-1");

        } else if (ChooseNumberOfPlayersActivity.choosen_number.equals("9")) {
            tv_formation_1.setText("4-3-1");
            tv_formation_2.setText("3-3-2");
            tv_formation_3.setText("3-4-1");
            tv_formation_4.setText("3-2-1-2");

        } else if (ChooseNumberOfPlayersActivity.choosen_number.equals("10")) {
            tv_formation_1.setText("4-4-1");
            tv_formation_2.setText("3-4-2");
            tv_formation_3.setText("3-3-1-2");
            tv_formation_4.setText("3-2-2-2");
        } else if (ChooseNumberOfPlayersActivity.choosen_number.equals("11")) {
            tv_formation_1.setText("3-4-3");
            tv_formation_2.setText("4-2-4");
            tv_formation_3.setText("4-4-2");
            tv_formation_4.setText("4-3-3");
        }
    }

    private void setClickListener() {

        tv_formation_1.setOnClickListener(this);
        tv_formation_2.setOnClickListener(this);
        tv_formation_3.setOnClickListener(this);
        tv_formation_4.setOnClickListener(this);
        iv_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_formation_1:
                formation=tv_formation_1.getText().toString().trim();
                goToChooseTsirts();
                break;
            case R.id.tv_formation_2:
                formation=tv_formation_2.getText().toString().trim();
                goToChooseTsirts();
                break;
            case R.id.tv_formation_3:
                formation=tv_formation_3.getText().toString().trim();
                goToChooseTsirts();
                break;
            case R.id.tv_formation_4:
                formation=tv_formation_4.getText().toString().trim();
                goToChooseTsirts();
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    private void goToChooseTsirts() {
        startActivity(new Intent(ChooseFormationActivity.this, ChooseTShirtActivity.class));
//        finish();
    }
}
