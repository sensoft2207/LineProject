package com.mxi.lineapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mxi.lineapp.R;
import com.mxi.lineapp.adapter.ImageAdapter;
import com.mxi.lineapp.network.CommonClass;

public class ChooseTShirtActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_toolbar;
    ImageView iv_back;
    ImageAdapter imageAdapter;
    CommonClass cc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_tshirt);
        cc = new CommonClass(this);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        tv_toolbar = (TextView) findViewById(R.id.tv_toolbar_title);
        iv_back.setVisibility(View.VISIBLE);

        tv_toolbar.setText(getResources().getString(R.string.create_formation_title));
        iv_back.setOnClickListener(this);


        AdView av_main= (AdView) findViewById(R.id.av_t_shirt);
        AdRequest adRequest = new AdRequest.Builder().build();
        av_main.loadAd(adRequest);

        imageAdapter = new ImageAdapter(ChooseTShirtActivity.this);
        GridView gridView = (GridView) findViewById(R.id.gv_main);

        gridView.setAdapter(imageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Intent i = new Intent(ChooseTShirtActivity.this, ChooseTeamName.class);
                i.putExtra("t_shirt_position", position);
                cc.savePrefInt("t_shirt_position", position);
                startActivity(i);
//                finish();

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }
}
