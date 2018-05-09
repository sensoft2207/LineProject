package com.mxi.lineapp.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.mxi.lineapp.R;
import com.mxi.lineapp.network.CommonClass;

public class Splash extends AppCompatActivity {
    ProgressBar progressbar;
    CommonClass cc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        cc=new CommonClass(this);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        if(!cc.loadPrefBoolean("isFirstTime")){
            cc.clearData();
            cc.savePrefBoolean("isFirstTime",true);
            cc.savePrefBoolean("isFirstTimeDisplay",true);
            Log.e("InSplash","in if akshay");
        }


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // only for marshmallow and newer versions
            checkReadWritePermission();
        } else {
            CountDown();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /*    int audioPermission = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
                    if (audioPermission != PackageManager.PERMISSION_GRANTED) {
                        checkAudioPermission();
                    }else{

                    }*/
                    CountDown();
                } else {
                    showErrorDialog("Please allow the permission for better performance", true);
                }
                break;

           /* case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    int audioPermission = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
                    if (audioPermission != PackageManager.PERMISSION_GRANTED) {
                        checkAudioPermission();
                    }else{
                        CountDown();
                    }
                }

                break;
*/
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void checkReadWritePermission() {
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                return;
            }
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
        } else {
//            checkAudioPermission();
            CountDown();

        }
    }
/*
    @TargetApi(Build.VERSION_CODES.M)
    public void checkAudioPermission() {
        int audioPermission = checkSelfPermission(Manifest.permission.RECORD_AUDIO);

        if (audioPermission != PackageManager.PERMISSION_GRANTED) {
            Log.e("Line", "In check permission If part ");
            if (!shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                return;
            }
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            return;
        } else {
            CountDown();
        }
    }*/

    public void showErrorDialog(String msg, final boolean isFromPermission) {
        progressbar.setVisibility(View.INVISIBLE);
        AlertDialog.Builder alert = new AlertDialog.Builder(Splash.this);
        alert.setTitle("Line");
        alert.setMessage(msg);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (isFromPermission) {
                    checkReadWritePermission();
                } else {
                    dialog.dismiss();
                }
            }
        });
        alert.show();
    }

    public void CountDown() {
        CountDownTimer mCountDownTimer = new CountDownTimer(2000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                progressbar.setVisibility(View.INVISIBLE);
                startActivity(new Intent(Splash.this, MainActivity.class));
                finish();
            }
        };
        mCountDownTimer.start();
    }

}
